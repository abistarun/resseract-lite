package abistech.resseract.data.dao;

import abistech.resseract.data.dao.impl.DBDAO;

public class DAOFactory {

    private static final DAO defaultDAO = new DBDAO();

    public static DAO getDefaultDAO() {
        return defaultDAO;
    }
}
