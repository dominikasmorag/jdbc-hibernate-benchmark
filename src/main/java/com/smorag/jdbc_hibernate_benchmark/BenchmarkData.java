package com.smorag.jdbc_hibernate_benchmark;

import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.OrderItem;
import com.smorag.jdbc_hibernate_benchmark.entity.Product;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import com.smorag.jdbc_hibernate_benchmark.utils.DataGenerator;

import java.util.List;

public class BenchmarkData {
    public static final List<User> REFERENCE_USERS_100 = DataGenerator.generateUsers(100);
    public static final List<User> REFERENCE_USERS_1K = DataGenerator.generateUsers(1000);
    public static final List<User> REFERENCE_USERS_5K = DataGenerator.generateUsers(5000);
    public static final List<User> REFERENCE_USERS_10K = DataGenerator.generateUsers(10000);

    public static final List<Product> REFERENCE_PRODUCTS_1K = DataGenerator.generateProducts(1000);
    public static final List<Product> REFERENCE_PRODUCTS_5K = DataGenerator.generateProducts(5000);
    public static final List<Product> REFERENCE_PRODUCTS_10K = DataGenerator.generateProducts(10000);

    public static final List<Order> REFERENCE_ORDERS_1K = DataGenerator.generateOrders(1000);
    public static final List<Order> REFERENCE_ORDERS_5K = DataGenerator.generateOrders(5000);
    public static final List<Order> REFERENCE_ORDERS_10K = DataGenerator.generateOrders(10000);

    public static final List<User> USERS_10K = DataGenerator.generateUsers(10000);
    public static final List<User> USERS_50K = DataGenerator.generateUsers(50000);
    public static final List<User> USERS_100K = DataGenerator.generateUsers(100000);

    public static final List<Product> PRODUCTS10 = DataGenerator.generateProducts(10);
    public static final List<Product> PRODUCTS_10K = DataGenerator.generateProducts(10000);

    public static final List<Order> ORDERS_10K = DataGenerator.generateOrders(10000);
    public static final List<Order> ORDERS_50K = DataGenerator.generateOrders(50000);
    public static final List<Order> ORDERS_100K = DataGenerator.generateOrders(100000);

//    public static final List<OrderItem> ORDER_ITEMS_10K = DataGenerator.generateOrderItems()

    public static List<User> cloneUsers(List<User> users) {
        return users.stream()
                .map(user -> {
                    User copy = new User();

                    copy.setFirstName(user.getFirstName());
                    copy.setLastName(user.getLastName());
                    copy.setEmail(user.getEmail());

                    return copy;
                })
                .toList();
    }

    public static List<Order> cloneOrders(List<Order> orders) {
        return orders.stream()
                .map(order -> {
                    Order copy = new Order();

                    copy.setUser(order.getUser());
                    copy.setTotalAmount(order.getTotalAmount());
                    copy.setStatus(order.getStatus());

                    return copy;
                }).toList();
    }

    public static List<OrderItem> cloneOrderItems(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> {
            OrderItem copy = new OrderItem();

            copy.setOrder(orderItem.getOrder());
            copy.setProduct(orderItem.getProduct());
            copy.setQuantity(orderItem.getQuantity());
            copy.setUnitPrice(orderItem.getUnitPrice());

            return copy;
        }).toList();
    }


}
