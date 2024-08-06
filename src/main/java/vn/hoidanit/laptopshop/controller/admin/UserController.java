package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService UploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService UploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.UploadService = UploadService;
        this.passwordEncoder = passwordEncoder;
    }

    // homepage
    @RequestMapping("/")
    public String gethomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUserByEmail("anhtrung@gmail.com");
        System.out.println(arrUsers);
        model.addAttribute("trung", "test");
        model.addAttribute("anhtrung", "Hello anhtrungtran");
        return "hello";
    }

    // list_user
    @RequestMapping("/admin/user")
    public String ListUser(Model model) {
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users1", users);
        return "admin/user/show_user";
    }

    // detail_user
    @RequestMapping("/admin/user/{id}")
    public String UserDetail(Model model, @PathVariable long id) {
        User user = this.userService.get1UserByID(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    // create_user
    @GetMapping("/admin/user/create")
    public String form(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    // button_create_user
    @PostMapping("/admin/user/create")
    public String create(Model model, @ModelAttribute("newUser") @Valid User anhtrung,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file) {

        // validate
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getObjectName() + " - " + error.getDefaultMessage());
        }
        //
        String avatar = this.UploadService.handleUploadFile(file, "avatar");
        String hashpassword = this.passwordEncoder.encode(anhtrung.getPassword());
        anhtrung.setAvatar(avatar);
        anhtrung.setPassword(hashpassword);
        anhtrung.setRole(this.userService.getRolebyName(anhtrung.getRole().getName()));
        this.userService.handleSaveUser(anhtrung);

        return "redirect:/admin/user";
    }

    // update_user
    @RequestMapping("/admin/user/update/{id}")
    public String UpdateUser(Model model, @PathVariable long id) {
        User CurrentUser = this.userService.get1UserByID(id);
        model.addAttribute("newUser", CurrentUser);
        return "admin/user/update";
    }

    // button_update_user
    @PostMapping("/admin/user/update")
    public String PostUpdateUser(Model model, @ModelAttribute("newUser") User anhtrung) {
        User CurrentUser = this.userService.get1UserByID(anhtrung.getId());
        if (CurrentUser != null) {
            CurrentUser.setFullName(anhtrung.getFullName());
            CurrentUser.setAddress(anhtrung.getAddress());
            CurrentUser.setPhone(anhtrung.getPhone());
            this.userService.handleSaveUser(CurrentUser);
        }
        return "redirect:/admin/user";
    }

    // delete_user
    @GetMapping("/admin/user/delete/{id}")
    public String DeleteUser(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    // button_delete_user
    @PostMapping("/admin/user/delete")
    public String PostDeleteUser(Model model, @ModelAttribute("newUser") User anhtrung) {
        this.userService.deleteUserById(anhtrung.getId());
        return "redirect:/admin/user";
    }

}
