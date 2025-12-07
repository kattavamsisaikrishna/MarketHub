package com.vamsi.markethub.order;

import com.vamsi.markethub.product.Product;
import com.vamsi.markethub.product.ProductRepository;
import com.vamsi.markethub.user.User;
import com.vamsi.markethub.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CustomerOrderService(OrderRepository orderRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ==============================
    // Helper: get logged-in customer
    // ==============================
    private User getLoggedInCustomer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        User customer = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getRole().name().equals("CUSTOMER")) {
            throw new RuntimeException("Only customers can place orders");
        }

        return customer;
    }

    // ==============================
    // Helper: map Order -> OrderResponse
    // ==============================
    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }

    // ==============================
    // Place order
    // ==============================
    public OrderResponse placeOrder(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        User customer = getLoggedInCustomer();

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        List<Product> productsToUpdate = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + itemReq.getProductId()));

            Integer quantity = itemReq.getQuantity();
            if (quantity == null || quantity <= 0) {
                throw new RuntimeException("Invalid quantity for product " + product.getId());
            }

            if (product.getStockQuantity() < quantity) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            BigDecimal price = product.getPrice();
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(quantity));

            totalAmount = totalAmount.add(lineTotal);

            // Create order item (priceAtPurchase is current product price)
            OrderItem orderItem = new OrderItem(product, quantity, price);
            orderItems.add(orderItem);

            // Decrease stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productsToUpdate.add(product);
        }

        // Create order
        Order order = new Order(customer, totalAmount, "PLACED");

        // Link items to order
        for (OrderItem item : orderItems) {
            order.addItem(item); // sets order in item and adds to order.items
        }

        // Save order (and items via cascade)
        Order savedOrder = orderRepository.save(order);

        // Save product stock updates
        productRepository.saveAll(productsToUpdate);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getMyOrders() {
        User customer = getLoggedInCustomer();
        List<Order> orders = orderRepository.findByCustomerOrderByCreatedAtDesc(customer);

        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
