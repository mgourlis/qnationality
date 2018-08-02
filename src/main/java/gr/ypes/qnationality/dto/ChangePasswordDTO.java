package gr.ypes.qnationality.dto;

import gr.ypes.qnationality.constraints.FieldMatch;
import gr.ypes.qnationality.constraints.ValidPassword;

import javax.validation.constraints.NotEmpty;

@FieldMatch(first = "password", second = "passwordConfirm", message = "The password fields must match")
public class ChangePasswordDTO {

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    @ValidPassword
    private String password;

    @NotEmpty
    private String passwordConfirm;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}

