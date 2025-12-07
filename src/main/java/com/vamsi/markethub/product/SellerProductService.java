package com.vamsi.markethub.product;

import com.vamsi.markethub.user.User;
import com.vamsi.markethub.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SellerProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getLoggedInSeller(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        User seller = userRepository.findByUsername(auth.getName())
                .orElseThrow(()->new RuntimeException("Seller Not found"));

        if( !seller.getRole().name().equals("SELLER")){
            throw new RuntimeException("Only Seller can manage these products");
        }

        return seller;
    }

    public Product addProduct(CreateProductRequest request){
        User seller = getLoggedInSeller();
        Product product = new Product(
                seller,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getCategory()
        );
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, UpdateProductRequest request){
        User seller = getLoggedInSeller();
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        if(! product.getSeller().getId().equals(seller.getId())){
            throw new RuntimeException("You cannot modify another seller's product");
        }

        if(request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        if (request.getCategory() != null) product.setCategory(request.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId){
        User seller = getLoggedInSeller();
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        if(! product.getSeller().getId().equals(seller.getId())){
            throw new RuntimeException("You cannot delete this product");
        }

        productRepository.delete(product);
    }

    public List<Product> getMyProducts(){
        User seller = getLoggedInSeller();
        return productRepository.findBySeller(seller);
    }

    public List<Product> searchMyProducts(String keyword){
        User seller = getLoggedInSeller();
        return productRepository.findBySellerAndNameContainingIgnoreCase(seller,keyword);
    }
}
