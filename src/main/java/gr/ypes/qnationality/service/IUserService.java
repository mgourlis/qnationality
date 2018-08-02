package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    public List<User> findAll();
    public Page<User> findAll(Pageable pageable);
    public List<User> findUsersByEmailContainingAndRoles_Role(String email, String role);
    public Page<User> findUsersByEmailContainingAndRoles_Role(String email, String role, Pageable pageable);
    public List<User> findUsersByEmailContaining(String email);
    public Page<User> findUsersByEmailContaining(String email, Pageable pageable);
    public List<User> findUsersByRoles_Role(String role);
    public Page<User> findUsersByRoles_Role(String role, Pageable pageable);
    public User findUserById(Long id);
    public User findUserByEmail(String email);
    public void save(User user);
    public void resetPassword(Long idr, String password, boolean expire);
    public void delete(Long id);
    public String generateRandomPassword();
}
