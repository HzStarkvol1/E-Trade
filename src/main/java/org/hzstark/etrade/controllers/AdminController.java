package org.hzstark.etrade.controllers;

import jakarta.servlet.http.HttpSession;
import org.hzstark.etrade.data.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AdminController(AdminRepository adminRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminLoginPage(HttpSession session) {
        if (session.getAttribute("loggedInAdmin") != null) {
            return "redirect:/admin/dashboard";
        }
        return "adminLogin";
    }

    @PostMapping("/login")
    public String adminLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        AdminEntity foundAdmin = adminRepository.findByUsername(username);
        
        if (foundAdmin != null && foundAdmin.getPassword().equals(password)) {
            session.setAttribute("loggedInAdmin", foundAdmin);
            return "redirect:/admin/dashboard";
        }
        
        redirectAttributes.addFlashAttribute("errorMessage", "Geçersiz admin bilgileri!");
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("loggedInAdmin");
        redirectAttributes.addFlashAttribute("successMessage", "Admin çıkışı yapıldı.");
        return "redirect:/admin";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/admin";
        }
        
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "adminDashboard";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductEntity product, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admin";
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Ürün başarıyla eklendi.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admin";
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı başarıyla silindi.");
        return "redirect:/admin/dashboard";
    }
}
