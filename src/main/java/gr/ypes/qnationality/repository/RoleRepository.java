package gr.ypes.qnationality.repository;

import gr.ypes.qnationality.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("RoleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
