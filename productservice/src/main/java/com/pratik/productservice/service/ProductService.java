package com.pratik.productservice.service;

import com.pratik.productservice.Exception.ResourceNotFoundException;
import com.pratik.productservice.model.Product;
import com.pratik.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts(int page, String sort){
        Sort sortBy = Sort.by(Sort.Order.desc(sort));
        Pageable pageable = Pageable.ofSize(10);
        pageable.withPage(page);
        pageable.getSortOr(sortBy);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.stream().toList();
    }

    public Product getProduct(String id){
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProduct(Product product){
        Product originalProduct  = productRepository.getReferenceById(product.getId());
        originalProduct.setAmount(product.getAmount());
        originalProduct.setEan(product.getEan());
        originalProduct.setSku(product.getSku());
        originalProduct.setTitle(product.getTitle());
        originalProduct.setDescription(product.getDescription());
        originalProduct.setImagePath(product.getImagePath());
        return productRepository.save(originalProduct);
    }

    public Product deleteProduct(String productId){
        Optional<Product> originalProductOptional  = Optional.of(productRepository.getReferenceById(productId));
        if(originalProductOptional.isPresent()) {
            Product originalProduct = originalProductOptional.get();
            originalProduct.setIsDeleted(1);
            return productRepository.save(originalProduct);
        }
        throw new ResourceNotFoundException("");
    }
}
