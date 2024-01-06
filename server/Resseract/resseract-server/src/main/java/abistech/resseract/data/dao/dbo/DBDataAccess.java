package abistech.resseract.data.dao.dbo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DATA_ACCESS")
public class DBDataAccess {

    @Id
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="sqliteTestTable")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "DATA_ID", nullable = false)
    private Long dataId;

    @Column(name = "USER_IDENTIFIER", nullable = false)
    private String uid;

    @Column(name = "ACCESS_ROLE", nullable = false)
    private String role;

    public DBDataAccess(Long dataId, String uid, String role) {
        this.dataId = dataId;
        this.uid = uid;
        this.role = role;
    }
}
