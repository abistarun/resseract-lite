package abistech.resseract.data.dao.impl;

import abistech.resseract.auth.AccessRole;
import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.config.Config;
import abistech.resseract.data.DataInfo;
import abistech.resseract.data.dao.DAO;
import abistech.resseract.data.dao.dbo.*;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.*;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.util.JSONHandler;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.*;
import java.util.stream.Collectors;

public class DBDAO implements DAO {

    @Override
    public void initialize(Properties properties) {
        HibernateUtil.initialize(properties);
    }

    @Override
    public void writeData(Data data, SourceType sourceType, Config configurations) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            String dataKeyStr = data.getDataKey().getKey();

            List<DBColumn> columns = new ArrayList<>(data.noOfCols() + 1);
            for (Column<?> column : data.getAllColumns()) {
                columns.add(new DBColumn(column.getName(), JSONHandler.serialize(column.getProperties()),
                        JSONHandler.serialize(column.toList())));
            }
            session.beginTransaction();

            DBData dbData = new DBData(dataKeyStr, JSONHandler.serialize(configurations), sourceType, columns);
            Long dataId = (Long) session.save(dbData);

            DBDataAccess dbDataAccess = new DBDataAccess(dataId, AuthenticationService.getUserIdentifier(), AccessRole.OWNER.name());
            session.save(dbDataAccess);

            session.flush();
            session.getTransaction().commit();
        }
    }

    @Override
    public Data readData(DataKey dataKey) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Long dataId = getIdFromDataKey(dataKey, session);
            if (dataId == null)
                throw new ResseractException(CustomErrorReports.DATA_NOT_FOUND);

            DBData dbData = session.get(DBData.class, dataId);
            return buildData(dataKey, dbData);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DataInfo> getAllDataInfo() throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            List<Long> accessibleDataIds = getAccessibleDataIds(session);
            List<DBData> dbDataList = selectAll(session, DBData.class, accessibleDataIds);

            List<DataInfo> dataInfos = new ArrayList<>();
            for (DBData dbData : dbDataList) {
                Config config = JSONHandler.deserialize(dbData.getDataConfigurations(), Config.class);
                assert config != null;
                Map<String, Map<String, Object>> columnProperties = new HashMap<>(dbData.getColumns().size());
                for (DBColumn column : dbData.getColumns()) {
                    columnProperties.put(column.getColumnName(), JSONHandler.deserialize(column.getProperties(), HashMap.class));
                }
                DataInfo dataInfo = new DataInfo(new DataKey(dbData.getDataKey()), dbData.getSourceType(), columnProperties, config);
                dataInfos.add(dataInfo);
            }
            return dataInfos;
        }
    }

    @Override
    public void addColumn(DataKey dataKey, Column<?> column) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Long dataId = getIdFromDataKey(dataKey, session);
            if (dataId == null)
                throw new ResseractException(CustomErrorReports.DATA_NOT_FOUND);

            DBData dbData = session.get(DBData.class, dataId);
            dbData.getColumns().add(new DBColumn(column.getName(), JSONHandler.serialize(column.getProperties()),
                    JSONHandler.serialize(column.toList())));

            session.update(dbData);
            session.flush();
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean isDataPresent(DataKey dataKey) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            String dataKeyRet = session.createQuery("select dataKey from DBData where dataKey = :dataKey and id in (select dataId from DBDataAccess where uid = :uid)", String.class)
                    .setParameter("dataKey", dataKey.getKey())
                    .setParameter("uid", AuthenticationService.getUserIdentifier())
                    .uniqueResult();
            return dataKeyRet != null;
        }
    }

    @Override
    public Config readDataConfig(DataKey dataKey) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            String dataPropertyStr = session.createQuery("select dataConfigurations from DBData where dataKey = :key and id in (select dataId from DBDataAccess where uid = :uid)", String.class)
                    .setParameter("key", dataKey.getKey())
                    .setParameter("uid", AuthenticationService.getUserIdentifier())
                    .uniqueResult();
            return JSONHandler.deserialize(dataPropertyStr, Config.class);
        }
    }

    @Override
    public void deleteData(DataKey dataKey) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Long dataId = getIdFromDataKey(dataKey, session);
            List<Long> accessibleDataIds = getAccessibleDataIds(session);
            if (!accessibleDataIds.contains(dataId))
                throw new ResseractException(CustomErrorReports.DATA_NOT_FOUND);
            DBData data = session.get(DBData.class, dataId);
            if (data != null) {
                session.delete(data);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteColumn(DataKey dataKey, String columnName) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Long dataId = getIdFromDataKey(dataKey, session);
            List<Long> accessibleDataIds = getAccessibleDataIds(session);
            if (!accessibleDataIds.contains(dataId))
                throw new ResseractException(CustomErrorReports.DATA_NOT_FOUND);

            DBData dbData = session.get(DBData.class, dataId);
            List<DBColumn> columns = dbData.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                DBColumn column = columns.get(i);
                if (column.getColumnName().equals(columnName)) {
                    columns.remove(i);
                    break;
                }
            }

            session.update(dbData);
            session.flush();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveDashboard(String name, String data) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Long id = getIdFromDashboardName(name, session);
            if (id != null) {
                DBDashboard dbDashboard = session.get(DBDashboard.class, id);
                session.delete(dbDashboard);
            }

            DBDashboard dbDashboard = new DBDashboard(name, data);
            Long dashboardId = (Long) session.save(dbDashboard);

            DBDashboardAccess dbDashboardAccess = new DBDashboardAccess(dashboardId, AuthenticationService.getUserIdentifier(), AccessRole.OWNER.name());
            session.save(dbDashboardAccess);

            session.flush();
            session.getTransaction().commit();
        }
    }

    @Override
    public String getDashboardData(String name) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Long dashboardId = getIdFromDashboardName(name, session);
            if (dashboardId == null)
                throw new ResseractException(CustomErrorReports.DASHBOARD_NOT_FOUND);
            DBDashboard dbDashboard = session.get(DBDashboard.class, dashboardId);
            return dbDashboard.getData();
        }
    }

    @Override
    public void deleteDashboard(String name) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            Long id = getIdFromDashboardName(name, session);
            if (id != null) {
                DBDashboard dbDashboard = session.get(DBDashboard.class, id);
                session.remove(dbDashboard);
            }
            session.flush();
            session.getTransaction().commit();
        }
    }

    @Override
    public List<String> getAllDashboards() {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            List<Long> dashboardIds = getAccessibleDashboardIds(session);
            List<DBDashboard> dbDashboards = selectAll(session, DBDashboard.class, dashboardIds);
            return dbDashboards.stream().map(DBDashboard::getName).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataInfo readDataInfo(DataKey dataKey) throws ResseractException {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            Long dataId = getIdFromDataKey(dataKey, session);
            if (dataId == null)
                throw new ResseractException(CustomErrorReports.DATA_NOT_FOUND);

            DBData dbData = session.get(DBData.class, dataId);
            Config config = JSONHandler.deserialize(dbData.getDataConfigurations(), Config.class);
            assert config != null;
            Map<String, Map<String, Object>> columnProperties = new HashMap<>(dbData.getColumns().size());
            for (DBColumn column : dbData.getColumns()) {
                columnProperties.put(column.getColumnName(), JSONHandler.deserialize(column.getProperties(), HashMap.class));
            }
            return new DataInfo(new DataKey(dbData.getDataKey()), dbData.getSourceType(), columnProperties, config);
        }
    }

    @Override
    public void close() {
        HibernateUtil.getSessionFactory().close();
    }

    private Long getIdFromDataKey(DataKey dataKey, Session session) {
        return session.createQuery("select id from DBData where dataKey = :key and id in (select dataId from DBDataAccess where uid = :uid)", Long.class)
                .setParameter("key", dataKey.getKey())
                .setParameter("uid", AuthenticationService.getUserIdentifier())
                .uniqueResult();
    }

    private Long getIdFromDashboardName(String dashboardName, Session session) {
        return session.createQuery("select id from DBDashboard where name = :name and id in (select dashboardId from DBDashboardAccess where uid = :uid)", Long.class)
                .setParameter("name", dashboardName)
                .setParameter("uid", AuthenticationService.getUserIdentifier())
                .uniqueResult();
    }

    private List<Long> getAccessibleDataIds(Session session) {
        return session.createQuery("select dataId from DBDataAccess where role = :role and uid = :uid", Long.class)
                .setParameter("role", AccessRole.OWNER.name())
                .setParameter("uid", AuthenticationService.getUserIdentifier())
                .getResultList();
    }

    private List<Long> getAccessibleDashboardIds(Session session) {
        return session.createQuery("select dashboardId from DBDashboardAccess where role = :role and uid = :uid", Long.class)
                .setParameter("role", AccessRole.OWNER.name())
                .setParameter("uid", AuthenticationService.getUserIdentifier())
                .getResultList();
    }

    private <T> List<T> selectAll(Session session, Class<T> clazz, List<Long> accessibleParams) {
        if (accessibleParams.isEmpty())
            return Collections.emptyList();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<T> criteriaQuery = builder.createQuery(clazz);
        JpaRoot<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root).where(root.get("id").in(accessibleParams));
        Query<T> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @SuppressWarnings("rawtypes")
    private Data buildData(DataKey dataKey, DBData dbData) throws ResseractException {
        Config config = JSONHandler.deserialize(dbData.getDataConfigurations(), Config.class);
        assert config != null;

        Data data = new DataFrame(dataKey);
        for (DBColumn dbColumn : dbData.getColumns()) {
            List list = JSONHandler.deserialize(dbColumn.getData(), ArrayList.class);
            Column<?> column = buildColumn(dbColumn, list);
            data.addColumn(column);
        }

        return data;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Column<?> buildColumn(DBColumn dbColumn, List list) throws ResseractException {
        Column<?> column = null;
        Map properties = JSONHandler.deserialize(dbColumn.getProperties(), HashMap.class);
        assert properties != null;
        DataType dataType = DataType.valueOf((String) properties.get(Constants.DATA_TYPE));
        switch (dataType) {
            case DATE:
                List<Date> dates = new ArrayList<>(list.size());
                for (Object o : list) {
                    dates.add(o == null ? null : new Date(Long.parseLong(o.toString())));
                }
                list = dates;
                column = new DateColumn(dbColumn.getColumnName(), properties);
                break;
            case NUMERICAL:
                column = new DoubleColumn(dbColumn.getColumnName(), properties);
                break;
            case CATEGORICAL:
                column = new StringColumn(dbColumn.getColumnName(), properties);
                break;
            case BOOLEAN:
                column = new BooleanColumn(dbColumn.getColumnName(), properties);
                break;
        }
        column.addAll(list);
        return column;
    }
}
