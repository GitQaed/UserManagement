package main.entities;

import jakarta.persistence.*;
import lombok.*;
import main.enums.RoleName;

import java.io.Serializable;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(RoleName rolename) {
        this.roleName = rolename;
    }

    public String getRoleName() {
        return roleName.toString();
    }
}
