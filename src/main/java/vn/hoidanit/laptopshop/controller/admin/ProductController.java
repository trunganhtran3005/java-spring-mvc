package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {
    public final ProductService productService;
    public final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("product", products);
        return "admin/product/show_product";
    }

    // create product
    @GetMapping("/admin/product/create")
    public String formcreate(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create_product";
    }

    // button_create_user
    @PostMapping("/admin/product/create")
    public String create(Model model, @ModelAttribute("newProduct") @Valid Product anhtrung,
            BindingResult newProductbindingResult,
            @RequestParam("file") MultipartFile file) {
        List<FieldError> errors = newProductbindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newProductbindingResult.hasErrors()) {
            return "admin/product/create_product";
        } else {
            //
            String imgProduct = this.uploadService.handleUploadFile(file, "product");
            anhtrung.setImage(imgProduct);
            this.productService.handleSaveProduct(anhtrung);
        }
        return "redirect:/admin/product";
    }

    // detail_product
    @RequestMapping("/admin/product/{id}")
    public String ProductDetail(Model model, @PathVariable long id) {
        Product product = this.productService.getProductbyID(id);
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail_product";
    }

    // update_product
    @RequestMapping("/admin/product/update/{id}")
    public String UpdateProduct(Model model, @PathVariable long id) {
        Product CurrentProduct = this.productService.getProductbyID(id);
        model.addAttribute("newProduct", CurrentProduct);
        return "admin/product/update_product";
    }

    // button_update_product
    @PostMapping("/admin/product/update")
    public String PostUpdateUser(Model model, @ModelAttribute("newProduct") @Valid Product anhtrung,
            BindingResult newProductbindingResult,
            @RequestParam("file") MultipartFile file) {
        Product CurrentProduct = this.productService.getProductbyID(anhtrung.getId());
        // validate
        if (newProductbindingResult.hasErrors()) {
            return "admin/product/update_product";
        }
        if (CurrentProduct != null) {

            if (!file.isEmpty()) {
                String img = this.uploadService.handleUploadFile(file, "product");
                CurrentProduct.setImage(img);
            }
            CurrentProduct.setName(anhtrung.getName());
            CurrentProduct.setPrice(anhtrung.getPrice());
            CurrentProduct.setDetailDesc(anhtrung.getDetailDesc());
            CurrentProduct.setShortDesc(anhtrung.getShortDesc());
            CurrentProduct.setQuantity(Long.valueOf(anhtrung.getQuantity()));
            CurrentProduct.setFactory(anhtrung.getFactory());
            CurrentProduct.setTarget(anhtrung.getTarget());
            this.productService.handleSaveProduct(CurrentProduct);
        }
        return "redirect:/admin/product";
    }

    // delete_product
    @GetMapping("/admin/product/delete/{id}")
    public String DeleteProduct(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete_product";
    }

    // button_delete_product
    @PostMapping("/admin/product/delete")
    public String PostDeleteProduct(Model model, @ModelAttribute("newProduct") Product anhtrung) {
        this.productService.deleteProductById(anhtrung.getId());
        return "redirect:/admin/product";
    }

}
