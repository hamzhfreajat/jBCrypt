package com.example.jBCrypt.web;

import com.example.jBCrypt.domain.Customer;
import com.example.jBCrypt.domain.Posts;
import com.example.jBCrypt.infraStructure.CustomerRepo;
import com.example.jBCrypt.infraStructure.PostsRepo;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class authController {

    @Autowired
    private final CustomerRepo customerRepo;
    private final PostsRepo postsRepo;

    public authController(CustomerRepo customerRepo, PostsRepo postsRepo) {
        this.customerRepo = customerRepo;
        this.postsRepo = postsRepo;
    }


    @GetMapping("/")
    RedirectView getIndex(){
        return new RedirectView("/login");
    }
    @GetMapping("/home")
    String getHome(HttpServletRequest request , Model model){
        HttpSession session = request.getSession();
        String username = session.getAttribute("username").toString();
        if (username.isEmpty()) return "login";
        Set<Posts> userPosts = customerRepo.findCustomerByUsername(request.getSession().getAttribute("username").toString()).getPosts();
        model.addAttribute("postsList" , userPosts);
        return "index";
    }

    @GetMapping("/login")
    String getLoginView(){
        return "login";
    }
    @PostMapping("/login")
    RedirectView loginCustomer(@ModelAttribute Customer customer1 , HttpServletRequest request){
        String username = customer1.getUsername();
        Customer dataBaseCustomer = customerRepo.findCustomerByUsername(username);
        if (dataBaseCustomer == null || !BCrypt.checkpw(customer1.getPassword() , dataBaseCustomer.getPassword())){
            return  new RedirectView("/login");
        }
        HttpSession session = request.getSession();
        session.setAttribute("username" , username);
        return new RedirectView("/home");
    }
    @GetMapping("/signUp")
    String getSignUp(){
        return "signUp";
    }
    @PostMapping("/signUp")
    RedirectView createCustomer(@ModelAttribute Customer customer){
         customer.setPassword(BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(12)));
         customerRepo.save(customer);
         return new RedirectView("/login");
    }

    @PostMapping("/post")
    public RedirectView createPost(HttpServletRequest request , @ModelAttribute Posts posts){
        Customer dataBaseCustomer = customerRepo.findCustomerByUsername(request.getSession().getAttribute("username").toString());
        posts.setCustomer(dataBaseCustomer);
        postsRepo.save(posts);
        return new RedirectView("/home");
    }

}
