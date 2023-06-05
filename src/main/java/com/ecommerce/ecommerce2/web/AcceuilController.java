package com.ecommerce.ecommerce2.web;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Service.CategoryService;
import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.ProduitService;

import jakarta.servlet.http.HttpSession;




@Controller
public class AcceuilController {
    @Autowired
    private ProduitService produitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CustomerService customerService;

    
    @RequestMapping(value = {"/index", "/"},method = RequestMethod.GET)
    public String index(Model model, Principal principal, HttpSession session){
        if(principal != null){
            session.setAttribute("username", principal.getName());

            List<Category> categoryList = categoryService.findAlList();
            List<Produit> produitList=produitService.find();
            model.addAttribute("categories", categoryList);
            model.addAttribute("produit", produitList);
            
            
            Customer customer = customerService.findByUsername(principal.getName());
           
            model.addAttribute("role", customer);
            // ajout du nbre de commande

            ShoppingCart cart = customer.getShoppingcart();
            if(cart != null){
                session.setAttribute("totalItem", cart.getTotalItems());}
        }else{
            List<Category> categoryList = categoryService.findAlList();
            List<Produit> produitList=produitService.find();
            model.addAttribute("categories", categoryList);
            model.addAttribute("produit", produitList);
            session.removeAttribute("username");
        }
        
        return "Acceuil2";
    }

    @GetMapping("/admin/AcceuilAdmin")
    public String AcceuilAdmin(Model model){
        Long client = customerService.countUser();
        Long produit = produitService.countProduct();
        model.addAttribute("client",client);
        model.addAttribute("produit",produit);
        return "AcceuilAdmin";
    }

    // @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    // public String home(Model model, Principal principal, HttpSession session){
    //     if(principal != null){
    //         session.setAttribute("username", principal.getName());
    //         Customer customer = customerService.findByUsername(principal.getName());
    //         ShoppingCart cart = customer.getShoppingCart();
    //         session.setAttribute("totalItems", cart.getTotalItems());
    //     }else{
    //         session.removeAttribute("username");
    //     }
    //     return "home";
    // }

    
    

    
}
