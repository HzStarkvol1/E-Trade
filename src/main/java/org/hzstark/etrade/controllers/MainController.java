package org.hzstark.etrade.controllers;

import jakarta.servlet.http.HttpSession;
import org.hzstark.etrade.data.UserEntity;
import org.hzstark.etrade.data.UserRepository;
import org.hzstark.etrade.data.ProductEntity;
import org.hzstark.etrade.data.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class MainController {
    
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public MainController(UserRepository userRepository, ProductRepository productRepository)
    {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    
    @GetMapping("/")
    public String logInPage(HttpSession session)
    {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/homePage"; 
        }
        return "loginPage";
    }

    @GetMapping("/homePage")
    public String homePage(Model model, HttpSession session)
    {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/"; 
        }
        
        List<ProductEntity> products = productRepository.findAll();
        model.addAttribute("products", products);
        UserEntity user = (UserEntity) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        
        return "homePage";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerUser") UserEntity user, RedirectAttributes redirectAttributes)
    {
        if(!userRepository.existsByUsername(user.getUsername()))
        {
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Kayıt başarılı, giriş yapabilirsiniz!");
            return "redirect:/";
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Bu kullanıcı adı zaten kullanımda!");
            return "redirect:/"; 
        }
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginUser") UserEntity user, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam(name = "remember-me", required = false) String rememberMe)
    {
        UserEntity foundUser = userRepository.findByUsername(user.getUsername());
        
        if(foundUser != null && user.getPassword().equals(foundUser.getPassword()))
        {
            session.setAttribute("loggedInUser", foundUser);

            if (rememberMe != null) {
                session.setMaxInactiveInterval(30 * 24 * 60 * 60);
            } else {
                session.setMaxInactiveInterval(30 * 60);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Giriş başarılı");
            return "redirect:/homePage"; 
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Kullanıcı adı veya şifre hatalı!");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Başarıyla çıkış yaptınız.");
        return "redirect:/";
    }
}
