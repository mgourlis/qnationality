package gr.ypes.qnationality.service;

import gr.ypes.qnationality.model.Role;
import gr.ypes.qnationality.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RoleService")
public class RoleServiceImpl implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Role findByName(String rolename) {
        String roleQuery = "ROLE_" + rolename.toUpperCase();
        return roleRepository.findByRole(roleQuery);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
}
