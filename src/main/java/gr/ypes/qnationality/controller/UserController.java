package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.dto.UserEditDTO;
import gr.ypes.qnationality.model.Role;
import gr.ypes.qnationality.model.User;
import gr.ypes.qnationality.service.IRoleService;
import gr.ypes.qnationality.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView searchUsers(@RequestParam(value = "user", required=false, defaultValue = "") String user,
                                    @RequestParam(value = "role", required=false, defaultValue = "") String role,
                                    ModelAndView modelAndView,
                                    Pageable pageable){
        Page<User> usersPage;
        if(user.equals("") && role.equals("")) {
            usersPage = userService.findAll(pageable);
        }else if(user.equals("")){
            usersPage = userService.findUsersByRoles_Role(role,pageable);
        }else if(role.equals("")){
            usersPage = userService.findUsersByEmailContaining(user,pageable);
        }else{
            usersPage = userService.findUsersByEmailContainingAndRoles_Role(user,role,pageable);
        }
        modelAndView.addObject("roles", roleService.findAll());
        modelAndView.addObject("users", usersPage.getContent());
        modelAndView.addObject("page",usersPage);
        modelAndView.setViewName("admin/user/showUsers");
        return modelAndView;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView showUser(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserById(id);
        modelAndView.addObject("users", user);
        modelAndView.setViewName("admin/user/showUser");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserById(id);
        List<Role> roles = roleService.findAll();
        if(user == null){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        UserEditDTO userDto = new UserEditDTO();
        userDto.init(user);
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("/admin/user/editUser");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView editUser(@PathVariable("id") long id, @Valid @ModelAttribute("user") UserEditDTO userDto, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        User edituser = userService.findUserById(id);
        if(edituser == null){
            throw new EntityNotFoundException();
        }
        else if (bindingResult.hasErrors()){
            modelAndView.addObject("roles", roleService.findAll());
            modelAndView.setViewName("/admin/user/editUser");
        }
        else{
            edituser.setActive(userDto.isActive());
            edituser.setName(userDto.getName());
            edituser.setLastName(userDto.getLastName());
            edituser.setForeas(userDto.getForeas());
            edituser.setRoles(userDto.getRoles());
            userService.save(edituser);
            modelAndView.setViewName("redirect:/admin/user/" + edituser.getId());
        }
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public ModelAndView newUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("roles", roleService.findAll());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin/user/newUser");
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        modelAndView.addObject("roles", roleService.findAll());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/user/newUser");
        } else {
            String randpass = userService.generateRandomPassword();
            user.setPassword(randpass);
            userService.save(user);
            modelAndView.addObject("successMessageBox", "User has been created successfully with password: " + randpass);
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("admin/user/newUser");

        }
        return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteUser(@PathVariable("id") long id){
        ModelAndView modelAndView = new ModelAndView();
        userService.delete(id);
        modelAndView.setViewName("redirect:/admin/user/");
        return modelAndView;
    }

    @RequestMapping(value="/resetpass", method = RequestMethod.POST)
    public ModelAndView setResetPassword(@RequestParam(value = "email") String email){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(email);
        if(user == null){
            throw new EntityNotFoundException("Could not find user");
        }else{
            String randpass = userService.generateRandomPassword();
            userService.resetPassword(user.getId(),randpass,true);
            UserEditDTO userDto = new UserEditDTO();
            userDto.init(user);
            modelAndView.addObject("roles", roleService.findAll());
            modelAndView.addObject("user", userDto);
            modelAndView.addObject("successMessageBox","User password changed to password: " + randpass);
        }
        modelAndView.setViewName("/admin/user/editUser");
        return modelAndView;
    }

}
