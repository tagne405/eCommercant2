package com.ecommerce.ecommerce2.web;


import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Service.CategoryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.CustomerDto;
import com.ecommerce.ecommerce2.Service.CustomerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

@Controller
@AllArgsConstructor
public class authController {

    CustomerService customerService;

    private CategoryService categoryService;

    private PasswordEncoder passwordEncoder;
    
    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String login(Model model) {
        List<Category> category = categoryService.findAlList();
        model.addAttribute("categories",category);
        return "login";
    }

    @GetMapping("/Forbidden")
    public String notAuthorized(){
        return "acceuill2";
    }

    @GetMapping("/register")
    public String register(Model model){

        model.addAttribute("customerDto",new CustomerDto());

        return "createAccount2";
    }



    @PostMapping("/do-register")
    public String proccesRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                  BindingResult result,
                                  Model model ){
        
         Customer customer2 = new Customer();
        
        try {
            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "createAccount2";
            }
            Customer customer = customerService.findByUsername(customerDto.getUsername());
            if(customer != null){
                model.addAttribute("username", "Username Deja Utiliser Cherche Un Autre");
                model.addAttribute("customerDto",customerDto);
                return "createAccount2";
            }
            if(customerDto.getPassword().equals(customerDto.getRepeatPassword())){
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()) );

                

                customerService.save(customerDto);
                model.addAttribute("success", "Enregistrement Reussie Mon Petit");
                return "createAccount2";
            }else{
                model.addAttribute("password", "LE MOT DE PASSE NE SONT PAS LES MEME");
                model.addAttribute("customerDto",customerDto);
                System.out.println("Password is not same");
                return "createAccount2";
            }
        }catch (Exception e){
            model.addAttribute("error", "Probleme de Serveur");
            model.addAttribute("customerDto",customerDto);
        }

        customerService.addRoleToUser(customer2.getUsername(),"USER");
        return "createAccount2";
    
    }


    


}
