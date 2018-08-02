package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRoleService {
    public List<Role> findAll();
    public Page<Role> findAll(Pageable pageable);
    public Role findById(Long id);
    public Role findByName(String rolename);
    public void save(Role role);
}

