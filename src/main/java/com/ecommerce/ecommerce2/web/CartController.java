package com.ecommerce.ecommerce2.web;

import java.lang.ProcessBuilder.Redirect;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.ProduitService;
import com.ecommerce.ecommerce2.Service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private final CustomerService customerService;

    private final ShoppingCartService cartService;

    private final ProduitService produitService;

    public CartController(CustomerService customerService, ShoppingCartService cartService, ProduitService produitService) {
        this.customerService = customerService;
        this.cartService = cartService;
        this.produitService = produitService;
    }

    @GetMapping("/cart")
    public String cart(Model model,Principal principal, HttpSession session){

        if(principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingcart();
        if(shoppingCart == null){
            model.addAttribute("check", "FOULE KAN Y'a rien dans ton Panier");    
        }
        if(shoppingCart != null){
        session.setAttribute("totalItem", shoppingCart.getTotalItems());
        double subTotal = shoppingCart.getPrixTotal();
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shoppingCart", shoppingCart);}
        return "cart";

    }





    @GetMapping("/cart/nbpanier")
    @ResponseBody
    public int nbPannier(Model model,Principal principal, HttpSession session){


        Customer customer = customerService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingcart();
        if(shoppingCart == null){
//            model.addAttribute("check", "FOULE KAN Y'a rien dans ton Panier");
            return 0;
        }else{

        return shoppingCart.getTotalItems();

    }
    }
    // Endpoint Ajax pour récupérer le nombre total d'articles
    @GetMapping("/cart/total-items")
    @ResponseBody
    public int getTotalItems(HttpSession session) {


        ShoppingCart shoppingCart = (ShoppingCart)session.getAttribute("shoppingCart");
        if (shoppingCart != null) {
            return shoppingCart.getTotalItems();
        } else {
            return 0;
        }
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("id")Long produitId,
                            @RequestParam(value = "quantite", required = false, defaultValue = "1") int quantite,
                            HttpServletRequest request,
                            Principal principal,
                            Model model,
                            RedirectAttributes redirectAttributes){

        if(principal == null){
            return "Redirect:/login";
        }
        Produit produit = produitService.getProduitById(produitId);
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

        ShoppingCart cart = cartService.addItemToCart(produit, quantite, customer);
        redirectAttributes.addFlashAttribute("success","Ajout au panier reussie Vas voir si tu veut");
        // redirige vers la page precedente vers laquelle cet requete a ete emise
        return "redirect:"+ request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", params = "action=update", method = RequestMethod.POST)
    public String updateCart(@RequestParam("quantite")int quantite,
                            @RequestParam("id") Long produitId,
                            Model model,
                            Principal principal ){

        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Produit produit = produitService.getProduitById(produitId);
            ShoppingCart cart = cartService.updateItemInCart(produit, quantite, customer);

            model.addAttribute("shoppingCart", cart);
            return "redirect:/cart";
        }

        

    }

   @RequestMapping(value = "/update-cart", params = "action=delete", method = RequestMethod.POST)
   public String deleteItemFromCart(@RequestParam("id")Long produitId,
                                    Model model,
                                    Principal principal){

        if(principal == null){
            return "/login";
        }else{
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            Produit produit = produitService.getProduitById(produitId);
            ShoppingCart cart = cartService.deleteItemFromCart(produit, customer);
            model.addAttribute("shoppingCart", cart);

            return "redirect:/cart";

        }

   }
}
