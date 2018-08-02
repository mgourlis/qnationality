package gr.ypes.qnationality.configuration;

import gr.ypes.qnationality.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import gr.ypes.qnationality.service.IUserService;

@Service
public class Initializer {

    @Autowired
    private IUserService IUserService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DifficultyRepository difficultyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;

    @Autowired
    private ExamSettingRepository examSettingRepository;

    @Autowired
    private ExamRepository examRepository;


    @EventListener
    public void inititialize(ApplicationReadyEvent event){

/*

        //-------------INIT ROLES---------------
        Role roleAdmin = new Role();
        roleAdmin.setRole("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
        Role roleModerator = new Role();
        roleModerator.setRole("ROLE_MODERATOR");
        roleRepository.save(roleModerator);
        Role roleUser = new Role();
        roleUser.setRole("ROLE_USER");
        roleRepository.save(roleUser);
        Role expiredUser = new Role();
        expiredUser.setRole("ROLE_EXPIRED");
        roleRepository.save(expiredUser);
        //--------------------------------------

        //-------------INIT USERS---------------
        Role role;

        User userAdmin = new User();
        userAdmin.setName("Myron");
        userAdmin.setLastName("Gourlis");
        userAdmin.setPassword("123456");
        userAdmin.setEmail("admin@test.gr");
        userAdmin.setForeas("Ypourgeio Eswterikwn");
        userAdmin.setActive(true);
        role = roleRepository.findByRole("ROLE_ADMIN");
        userAdmin.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userAdmin);


        User userModerator = new User();
        userModerator.setName("MyronMod");
        userModerator.setLastName("GourlisMod");
        userModerator.setPassword("123456");
        userModerator.setEmail("moderator@test.gr");
        userModerator.setForeas("Ypourgeio Eswterikwn");
        userModerator.setActive(true);
        role = roleRepository.findByRole("ROLE_MODERATOR");
        userModerator.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userModerator);

        User userUser = new User();
        userUser.setName("MyronUser");
        userUser.setLastName("GourlisUser");
        userUser.setPassword("123456");
        userUser.setEmail("user@test.gr");
        userUser.setForeas("Apokentromeni Dioikisi");
        userUser.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser);

        User userUser1 = new User();
        userUser1.setName("MyronUser");
        userUser1.setLastName("GourlisUser");
        userUser1.setPassword("123456");
        userUser1.setEmail("user1@test.gr");
        userUser1.setForeas("Apokentromeni Dioikisi");
        userUser1.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser1.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser1);

        User userUser2 = new User();
        userUser2.setName("MyronUser");
        userUser2.setLastName("GourlisUser");
        userUser2.setPassword("123456");
        userUser2.setEmail("user2@test.gr");
        userUser2.setForeas("Apokentromeni Dioikisi");
        userUser2.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser2.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser2);

        User userUser3 = new User();
        userUser3.setName("MyronUser");
        userUser3.setLastName("GourlisUser");
        userUser3.setPassword("123456");
        userUser3.setEmail("user3@test.gr");
        userUser3.setForeas("Apokentromeni Dioikisi");
        userUser3.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser3.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser3);

        User userUser4 = new User();
        userUser4.setName("MyronUser");
        userUser4.setLastName("GourlisUser");
        userUser4.setPassword("123456");
        userUser4.setEmail("user4@test.gr");
        userUser4.setForeas("Apokentromeni Dioikisi");
        userUser4.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser4.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser4);

        User userUser5 = new User();
        userUser5.setName("MyronUser");
        userUser5.setLastName("GourlisUser");
        userUser5.setPassword("123456");
        userUser5.setEmail("user5@test.gr");
        userUser5.setForeas("Apokentromeni Dioikisi");
        userUser5.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser5.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser5);

        User userUser6 = new User();
        userUser6.setName("MyronUser");
        userUser6.setLastName("GourlisUser");
        userUser6.setPassword("123456");
        userUser6.setEmail("user6@test.gr");
        userUser6.setForeas("Apokentromeni Dioikisi");
        userUser6.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser6.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser6);

        User userUser7 = new User();
        userUser7.setName("MyronUser");
        userUser7.setLastName("GourlisUser");
        userUser7.setPassword("123456");
        userUser7.setEmail("user7@test.gr");
        userUser7.setForeas("Apokentromeni Dioikisi");
        userUser7.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser7.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser7);

        User userUser8 = new User();
        userUser8.setName("MyronUser");
        userUser8.setLastName("GourlisUser");
        userUser8.setPassword("123456");
        userUser8.setEmail("user8@test.gr");
        userUser8.setForeas("Apokentromeni Dioikisi");
        userUser8.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser8.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser8);

        User userUser9 = new User();
        userUser9.setName("MyronUser");
        userUser9.setLastName("GourlisUser");
        userUser9.setPassword("123456");
        userUser9.setEmail("user9@test.gr");
        userUser9.setForeas("Apokentromeni Dioikisi");
        userUser9.setActive(true);
        role = roleRepository.findByRole("ROLE_USER");
        userUser9.setRoles(new HashSet<Role>(Arrays.asList(role)));
        IUserService.save(userUser9);
        //--------------------------------------

*/

    }
}
