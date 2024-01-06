package abistech.resseract.data.frame;

import lombok.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
@Setter
public class DataProperty {

    private String indexColumnName;
    private String primaryColumnName;

    public static DataProperty build(String indexColumnName, String primaryColumnName) {
        DataProperty dataProperty = new DataProperty();
        dataProperty.setIndexColumnName(indexColumnName);
        dataProperty.setPrimaryColumnName(primaryColumnName);
        return dataProperty;
    }

    public DataProperty(DataProperty dataProperty) {
        this.indexColumnName = dataProperty.getIndexColumnName();
        this.primaryColumnName = dataProperty.getPrimaryColumnName();
    }

    public String getIndexColumnName() {
        return indexColumnName;
    }

    public void setIndexColumnName(String indexColumnName) {
        this.indexColumnName = indexColumnName;
    }

    public String getPrimaryColumnName() {
        return primaryColumnName;
    }

    public void setPrimaryColumnName(String primaryColumnName) {
        this.primaryColumnName = primaryColumnName;
    }
}
