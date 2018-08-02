package gr.ypes.qnationality.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Long id;

    @Column(name="role", unique=true)
    @NotEmpty
    private String role;

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role1 = (Role) o;

        return getRole().equals(role1.getRole());
    }

    @Override
    public int hashCode() {
        return getRole().hashCode();
    }

    @Override
    public String toString() {
        return role.split("ROLE_")[1].toLowerCase();
    }
}

