package com.ecommerce.ecommerce2.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.ecommerce2.Entity.Category;
import com.ecommerce.ecommerce2.Entity.Produit;
import com.ecommerce.ecommerce2.Service.CategoryService;
import com.ecommerce.ecommerce2.Service.ProduitService;

@Controller
public class ProduitController {

    @Autowired
    ProduitService produitService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/admin/listeProduit")
    public String listeProduit(Model model,Principal principal){ 
        if(principal == null){
            return "redirect:/login";
        }  
        List<Produit> listeProduit = produitService.findAllProduit();
        model.addAttribute("listeProduit", listeProduit);
        model.addAttribute("size", listeProduit.size());
        return "listeProduit";
    }

    @GetMapping("/admin/newProduit")
    public String newProduit(Model model){   
        List<Category> categories= categoryService.findAllByActivated();
        Produit produit=new Produit();
        model.addAttribute("categorie",categories);
        model.addAttribute("produit", produit);
        return "createProduit";
    }

    @PostMapping("/admin/saveProduct")
    public String saveProduit(@ModelAttribute("produit")Produit produit,
                              @RequestParam("image")MultipartFile imageProduct,
                              RedirectAttributes attributes){
        if (produit == null) {
            attributes.addFlashAttribute("error", "Le produit est manquant.");
            return "redirect:/admin/listeProduit";
        }
        if (imageProduct == null || imageProduct.isEmpty()) {
            attributes.addFlashAttribute("failed", "L'image du produit est manquante.");
            return "redirect:/admin/listeProduit";
        }
        try {
            produitService.save(imageProduct, produit);
            attributes.addFlashAttribute("success", "Le produit a été enregistré avec succès.");
        } catch (Exception e) {
            attributes.addFlashAttribute("failed", "Erreur lors de l'enregistrement du produit : " + e.getMessage());
        }                        
                                
        return "redirect:/admin/listeProduit";
    }


    @GetMapping("/admin/updateProduit/{id}")
    public String updateProdduit(@PathVariable("id") Long id, Model model ){
        model.addAttribute("title", "update produit");
        List<Category> categories = categoryService.findAllByActivated();
        Produit produit= produitService.findById(id);
        model.addAttribute("categories",categories);
        model.addAttribute("produit",produit);
        return "updateProduit";

    }

    @PostMapping("/admin/updateProduit/{id}")
    public String update(@PathVariable("id") Long id, 
                         @ModelAttribute("produit") Produit produit,
                         @RequestParam("image")MultipartFile image,
                         RedirectAttributes attributes){
        
        try{
            produitService.update(image, produit);
            attributes.addFlashAttribute("success", "Modification Reussie Salot");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Modif Echouer Batard");
        }
        return "redirect:/admin/listeProduit";

    }

    @RequestMapping(value = "/admin/enabledProduit/{id}", method = {RequestMethod.PUT,RequestMethod.GET})
    public String enabledProduit(@PathVariable("id")Long id, RedirectAttributes attributes){

        try {
            produitService.enabled(id);
            attributes.addFlashAttribute("success", "Activation reussi");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Erreur D'actiavtion vas Chercher L'erreur");
        }

        return "redirect:/admin/listeProduit";

    }

    @RequestMapping(value = "/admin/deleteProduit/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteProduit(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            produitService.deleteById(id);
            attributes.addFlashAttribute("success", "suppression reussie Bao");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "suppression echouer vas Chercher l'erreur");
        }

        return "redirect:/admin/listeProduit";
    }


    /*categoriers and product */

    @GetMapping("/admin/produit")
    public String products(Model model){
        List<Produit> produits = produitService.find();
        model.addAttribute("produits", produits);
        return "acceuil2";
    }

    @GetMapping("/findProduit/{id}")
    public String findProduitById(@PathVariable("id")Long id,Model model){
        Produit produit = produitService.getProduitById(id);
        List<Category> categories = categoryService.findAlList();
        model.addAttribute("categories", categories);
        model.addAttribute("detailproduit", produit);
        return "detail";
    }


    @GetMapping("/produitInCategory/{id}")
    public String findProduitByCategory(@PathVariable("id") Long categoryId,Model model){
        Category category = categoryService.findId(categoryId);
        List<Category> categories = categoryService.findAlList();
        List<Produit> produits = produitService.findProduitInCategory(categoryId);
        model.addAttribute("categories", categories);
        model.addAttribute("ProduitCategory", produits);
        model.addAttribute("categoryProduit", category);
        return "categoryInProduit";
    }

    //count Product
    public Long countproduct(){
        return produitService.countProduct();
    }
}
