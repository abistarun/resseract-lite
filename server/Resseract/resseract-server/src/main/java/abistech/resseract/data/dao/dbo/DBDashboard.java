package abistech.resseract.data.dao.dbo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DASHBOARD")
public class DBDashboard {

    @Id
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="sqliteTestTable")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DATA", nullable = false)
    private String data;

    public DBDashboard(String name, String data) {
        this.name = name;
        this.data = data;
    }
}
