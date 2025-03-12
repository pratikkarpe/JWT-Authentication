package com.pratik.productservice.controller;

import com.pratik.productservice.model.Product;
import com.pratik.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PreAuthorize("hasPermission('PRODUCT','READ')")
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts(0,"updatedBy");
    }

    @PreAuthorize("hasPermission('PRODUCT','CREATE')")
    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @PreAuthorize("hasPermission('PRODUCT','UPDATE')")
    @PatchMapping("/product")
    public Product updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @PreAuthorize("hasPermission('PRODUCT','DELETE')")
    @DeleteMapping("/product/{id}")
    public Product deleteProduct(@PathVariable("id") String id){
        return productService.deleteProduct(id);
    }

    @PreAuthorize("hasPermission('PRODUCT','READ')")
    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable("id") String id){
        return productService.getProduct(id);
    }
}
