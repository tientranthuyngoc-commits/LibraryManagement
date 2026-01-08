package com.example.library.controller.admin;

import com.example.library.domain.User;
import com.example.library.repository.RoleRepository;
import com.example.library.service.UserService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserAdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/user-form";
    }

    @PostMapping("/users/create")
    public String create(User user, @RequestParam("roleNames") List<String> roleNames) {
        userService.createUser(user, roleNames);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/user-form";
    }

    @PostMapping("/users/{id}/edit")
    public String edit(@PathVariable Long id, User user, @RequestParam("roleNames") List<String> roleNames) {
        userService.updateUser(id, user, roleNames);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        userService.toggleStatus(id);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}