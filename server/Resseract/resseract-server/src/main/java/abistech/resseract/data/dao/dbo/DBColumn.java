package abistech.resseract.data.dao.dbo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DATA_COLUMN")
public class DBColumn {

    @Id
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="sqliteTestTable")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "COLUMN_NAME", nullable = false)
    private String columnName;

    @Column(name = "PROPERTIES", nullable = false)
    private String properties;

    @Column(name = "DATA", nullable = false)
    private String data;

    public DBColumn(String columnName, String properties, String data) {
        this.columnName = columnName;
        this.properties = properties;
        this.data = data;
    }
}
