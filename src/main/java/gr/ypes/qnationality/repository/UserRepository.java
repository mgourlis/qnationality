package gr.ypes.qnationality.repository;

import gr.ypes.qnationality.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findUsersByEmailContainingAndRoles_Role(String email, String role);
    Page<User> findUsersByEmailContainingAndRoles_Role(String email, String role, Pageable pageable);
    List<User> findUsersByEmailContaining(String email);
    Page<User> findUsersByEmailContaining(String email, Pageable pageable);
    List<User> findUsersByRoles_Role(String role);
    Page<User> findUsersByRoles_Role(String role, Pageable pageable);
}

