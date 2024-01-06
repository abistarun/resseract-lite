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
@Table(name = "DASHBOARD_ACCESS")
public class DBDashboardAccess {

    @Id
    @GeneratedValue(generator="sqlite")
    @TableGenerator(name="sqlite", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="sqliteTestTable")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "DASHBOARD_ID", nullable = false)
    private Long dashboardId;

    @Column(name = "USER_IDENTIFIER", nullable = false)
    private String uid;

    @Column(name = "ACCESS_ROLE", nullable = false)
    private String role;

    public DBDashboardAccess(Long dashboardId, String uid, String role) {
        this.dashboardId = dashboardId;
        this.uid = uid;
        this.role = role;
    }
}
