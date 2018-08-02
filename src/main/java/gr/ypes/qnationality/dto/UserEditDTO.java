package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.model.Role;
import gr.ypes.qnationality.model.User;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class UserEditDTO {

    private long id;

    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String foreas;

    private boolean active;

    @NotEmpty
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getForeas() {
        return foreas;
    }

    public void setForeas(String foreas) {
        this.foreas = foreas;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void init(User user) throws IllegalArgumentException{
        setId(user.getId());
        setEmail(user.getEmail());
        setName(user.getName());
        setLastName(user.getLastName());
        setForeas(user.getForeas());
        setActive(user.isActive());
        setRoles(user.getRoles());
    }
}
