package com.ecommerce.ecommerce2.web;

import java.security.Principal;
import java.util.List;

import com.ecommerce.ecommerce2.Entity.OrderDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecommerce.ecommerce2.Entity.Customer;
import com.ecommerce.ecommerce2.Entity.Order;
import com.ecommerce.ecommerce2.Entity.ShoppingCart;
import com.ecommerce.ecommerce2.Service.CustomerService;
import com.ecommerce.ecommerce2.Service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class orderController {
    
    private final CustomerService customerService;

    private final OrderService orderService;

    public orderController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        // ajoute la methode trim()
        if(customer.getTelephone() == null || customer.getAddress() == null || customer.getCity() == null){

            model.addAttribute("customer", customer);
            model.addAttribute("error", "tu doit renseigner les informations ");
            return "monCompte";
        }else{
            model.addAttribute("success", "ce bon Bao tu peut maintenant Payer ton Kakou");

            // Ajout de nouvelle methode
            model.addAttribute("customer", customer);
            ShoppingCart cart = customer.getShoppingcart();
            model.addAttribute("cart", cart);
            
        }
        return "payement";
        

        // return "monCompte";
    }

    @GetMapping("/order")
    public String order(Principal principal, Model model){
        if(principal == null){
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        List<Order> orderList = customer.getOrder();
        model.addAttribute("orders", orderList);

        return "order";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(Principal principal,Model model){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingcart();
        orderService.saveOrder(cart);
        return "redirect:/order";
    }

    /*Order for Admin*/
//    @GetMapping("/admin/order")
//    public String ListOrder(Principal principal, Model model){
//        if(principal == null){
//            return "redirect:/login";
//        }
//        List<Order> order = orderService.findAllOrder();
//        model.addAttribute("orders",order);
//        return "ListOrder";
//    }

    @GetMapping("/admin/order")
    public String getOrder(Principal principal, Model model, RedirectAttributes redirectAttributes){
        if(principal == null){
            return "redirect:/login";
        }

        List<Order> orderDetails = orderService.findAllOrder();
        List<OrderDetail> order = orderService.findOrderDetail();
        model.addAttribute("orderDetails",orderDetails);
        if(order.isEmpty())
            redirectAttributes.addFlashAttribute("vide","La liste des Commandes est vide");

        model.addAttribute("orders",order);
        return "ListOrder";
    }
}
