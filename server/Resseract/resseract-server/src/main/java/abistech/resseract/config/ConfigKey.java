package abistech.resseract.config;

import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.step.elements.CategoricalAggregationType;
import abistech.resseract.step.elements.DateAggregationType;
import abistech.resseract.step.elements.NumericalAggregationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author abisTarun
 */
@Getter
@EqualsAndHashCode
public class ConfigKey {

    public static final ConfigKey DATA_KEY = new ConfigKey("Data Key", "DATA_KEY", null, ConfigValueType.STRING, null, false);
    public static final ConfigKey GROUPBY_COLUMN_NAME = new ConfigKey("Group By Column", "GROUPBY_COLUMN_NAME", null, ConfigValueType.COLUMN_NAME, null, true, Arrays.asList(DataType.CATEGORICAL, DataType.NUMERICAL, DataType.DATE));
    public static final ConfigKey SORT_COLUMN = new ConfigKey("Sort Column", "SORT_COLUMN", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey SORT_ASCENDING = new ConfigKey("Sort Ascending", "SORT_ASCENDING", Boolean.TRUE, ConfigValueType.LIST, Boolean.values(), true);
    public static final ConfigKey NUMERICAL_AGGREGATION = new ConfigKey("Numerical Aggregation", "NUMERICAL_AGGREGATION", NumericalAggregationType.SUM, ConfigValueType.LIST, NumericalAggregationType.values(), true);
    public static final ConfigKey CATEGORICAL_AGGREGATION = new ConfigKey("Categorical Aggregation", "CATEGORICAL_AGGREGATION", CategoricalAggregationType.FIRST_VALUE, ConfigValueType.LIST, CategoricalAggregationType.values(), true);
    public static final ConfigKey DATE_AGGREGATION = new ConfigKey("Date Aggregation", "DATE_AGGREGATION", DateAggregationType.FIRST_VALUE, ConfigValueType.LIST, DateAggregationType.values(), true);
    public static final ConfigKey CSV_FILE = new ConfigKey("CSV File", "CSV_FILE", null, ConfigValueType.FILE, null, true);
    public static final ConfigKey TARGET_COLUMNS = new ConfigKey("Target Columns", "TARGET_COLUMNS", null, ConfigValueType.COLUMN_NAMES, null, true, Arrays.asList(DataType.NUMERICAL, DataType.CATEGORICAL));
    public static final ConfigKey EXPRESSION = new ConfigKey("Expression", "EXPRESSION", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey SLICE_EXPRESSION = new ConfigKey("Slice Expression", "SLICE_EXPRESSION", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey USERNAME = new ConfigKey("Username", "USERNAME", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey PASSWORD = new ConfigKey("Password", "PASSWORD", null, ConfigValueType.PASSWORD, null, true);
    public static final ConfigKey DB_NAME = new ConfigKey("Database Name", "DB_NAME", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey DB_PORT = new ConfigKey("Port", "DB_PORT", null, ConfigValueType.STRING, null, true);
    public static final ConfigKey HOST = new ConfigKey("Host", "HOST", "localhost", ConfigValueType.STRING, null, true);
    public static final ConfigKey QUERY = new ConfigKey("Query", "QUERY", "select * from <TABLE_NAME>", ConfigValueType.STRING, null, true);
    public static final ConfigKey COLUMN_DATA_TYPES = new ConfigKey("Column Data Types", "COLUMN_DATA_TYPES", null, ConfigValueType.HIDDEN, null, true);

    private String name;
    private String key;
    private Object defaultValue;
    private final ConfigValueType valueType;
    private final Object[] possibleValues;
    private boolean isEditable;
    private List<DataType> dataTypes;

    public ConfigKey(String name, String key, Object defaultValue, ConfigValueType valueType, Object[] possibleValues, boolean isEditable) {
        this.name = name;
        this.key = key;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
        this.possibleValues = possibleValues;
        this.isEditable = isEditable;
    }

    public ConfigKey(ConfigKey oldKey) {
        this.name = oldKey.name;
        this.key = oldKey.key;
        this.defaultValue = oldKey.defaultValue;
        this.valueType = oldKey.valueType;
        this.possibleValues = oldKey.possibleValues;
        this.isEditable = oldKey.isEditable;
        this.dataTypes = oldKey.dataTypes;
    }

    public ConfigKey(String name, String key, Object defaultValue, ConfigValueType valueType, Object[] possibleValues, boolean isEditable, List<DataType> dataTypes) {
        this(name, key, defaultValue, valueType, possibleValues, isEditable);
        this.dataTypes = dataTypes;
    }

    public ConfigKey setEditable(boolean editable) {
        isEditable = editable;
        return this;
    }

    public ConfigKey setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
