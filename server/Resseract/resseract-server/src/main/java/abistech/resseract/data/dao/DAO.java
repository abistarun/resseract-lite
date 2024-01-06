package abistech.resseract.data.dao;

import abistech.resseract.data.frame.Data;
import abistech.resseract.config.Config;
import abistech.resseract.data.DataInfo;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.ResseractException;

import java.util.List;
import java.util.Properties;

/**
 * @author abisTarun
 */
public interface DAO {

    void initialize(Properties properties);

    void writeData(Data data, SourceType sourceType, Config configurations) throws ResseractException;

    Data readData(DataKey dataKey) throws ResseractException;

    List<DataInfo> getAllDataInfo() throws ResseractException;

    void addColumn(DataKey dataKey, Column<?> column) throws ResseractException;

    boolean isDataPresent(DataKey dataKey);

    Config readDataConfig(DataKey dataKey) throws ResseractException;

    void deleteData(DataKey dataKey) throws ResseractException;

    void deleteColumn(DataKey dataKey, String columnName) throws ResseractException;

    void saveDashboard(String name, String data);

    String getDashboardData(String name) throws ResseractException;

    void deleteDashboard(String name);

    List<String> getAllDashboards();

    DataInfo readDataInfo(DataKey dataKey) throws ResseractException;

    void close();
}
