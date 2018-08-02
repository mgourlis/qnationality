package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Role;
import gr.ypes.qnationality.model.User;
import gr.ypes.qnationality.repository.UserRepository;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("UserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoleService IRoleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findUsersByEmailContainingAndRoles_Role(String email, String role) {
        String roleQuery = "ROLE_" + role.toUpperCase();
        return userRepository.findUsersByEmailContainingAndRoles_Role(email,roleQuery);
    }

    @Override
    public Page<User> findUsersByEmailContainingAndRoles_Role(String email, String role, Pageable pageable) {
        String roleQuery = "ROLE_" + role.toUpperCase();
        return userRepository.findUsersByEmailContainingAndRoles_Role(email,roleQuery,pageable);
    }

    @Override
    public List<User> findUsersByEmailContaining(String email) {
        return userRepository.findUsersByEmailContaining(email);
    }

    @Override
    public Page<User> findUsersByEmailContaining(String email, Pageable pageable) {
        return userRepository.findUsersByEmailContaining(email,pageable);
    }

    @Override
    public List<User> findUsersByRoles_Role(String role) {
        String roleQuery = "ROLE_" + role.toUpperCase();
        return findUsersByRoles_Role(roleQuery);
    }

    @Override
    public Page<User> findUsersByRoles_Role(String role, Pageable pageable) {
        String roleQuery = "ROLE_" + role.toUpperCase();
        return userRepository.findUsersByRoles_Role(roleQuery,pageable);
    }

    @Override
    public User findUserById(Long id) throws EntityNotFoundException {
        return userRepository.getOne(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        if(user.getId() == null){
            User emailUser = userRepository.findByEmail(user.getEmail());
            if(emailUser == null) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                Role expired = IRoleService.findByName("expired");
                Role admin = IRoleService.findByName("admin");
                if (user.getRoles() == null) {
                    Set<Role> roles = new HashSet<Role>();
                    roles.add(expired);
                    user.setRoles(roles);
                } else {
                    if(!user.getRoles().contains(admin)) {
                        user.getRoles().add(expired);
                    }
                }
                userRepository.save(user);
            }else{
                throw new IllegalArgumentException("Email already exists");
            }
        }else{
            User oldUser = userRepository.getOne(user.getId());
            if(oldUser != null) {
                user.setEmail(oldUser.getEmail());
                user.setPassword(oldUser.getPassword());
                userRepository.save(user);
            }else{
                throw new EntityNotFoundException("Can't save user. Invalid user");
            }
        }

    }

    @Override
    public void resetPassword(Long id, String password, boolean expire) {
        User user = userRepository.getOne(id);
        if(user != null) {
            Role expired = IRoleService.findByName("expired");
            if(expire) {
                user.getRoles().add(expired);
                user.setPassword(bCryptPasswordEncoder.encode(password));
                userRepository.save(user);
            }else{
                user.getRoles().remove(expired);
                user.setPassword(bCryptPasswordEncoder.encode(password));
                userRepository.save(user);
            }
        }else{
            throw new EntityNotFoundException("Can't save user. Invalid user");
        }
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.getOne(id);
        if (user != null){
            userRepository.delete(user);
        }
        else{
            throw new EntityNotFoundException("Invalid user");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user;
    }

    @Override
    public String generateRandomPassword() {
        List rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1));

        PasswordGenerator generator = new PasswordGenerator();
        String password = generator.generatePassword(8, rules);
        return password;
    }


}

