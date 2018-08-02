package gr.ypes.qnationality.controller;

import gr.ypes.qnationality.model.Role;
import gr.ypes.qnationality.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Secured("ADMIN")
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView getRoles(ModelAndView modelAndView, Pageable pageable){
        Page<Role> rolesPage = roleService.findAll(pageable);
        modelAndView.addObject("roles", rolesPage.getContent());
        modelAndView.addObject("page",rolesPage);
        modelAndView.setViewName("admin/role/showRoles");
        return modelAndView;
    }

}

