package abistech.resseract.data.dao.dbo;

import abistech.resseract.data.source.SourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DATA")
public class DBData {

    @Id
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="sqliteTestTable")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "DATA_KEY", nullable = false)
    private String dataKey;

    @Column(name = "DATA_PROPERTY", nullable = false)
    private String dataConfigurations;

    @Column(name = "SOURCE_TYPE", nullable = false)
    private SourceType sourceType;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "DATA_ID")
    private List<DBColumn> columns;

    public DBData(String dataKey, String dataConfigurations, SourceType sourceType, List<DBColumn> columns) {
        this.dataKey = dataKey;
        this.dataConfigurations = dataConfigurations;
        this.sourceType = sourceType;
        this.columns = columns;
    }
}
