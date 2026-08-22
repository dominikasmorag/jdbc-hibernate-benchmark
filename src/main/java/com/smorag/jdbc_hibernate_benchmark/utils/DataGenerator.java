package com.smorag.jdbc_hibernate_benchmark.utils;

import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.OrderItem;
import com.smorag.jdbc_hibernate_benchmark.entity.Product;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import enums.OrderStatus;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private final static Faker faker = new Faker();

    public static List<User> generateUsers(int amount) {
        List<User> listOfUsers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(i + faker.internet().emailAddress());
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    public static List<Product> generateProducts(int amount) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName());
            product.setCategory(faker.commerce().department());
            product.setPrice(new BigDecimal(faker.commerce().price()));
            product.setStockQuantity((int) (Math.random() * (100 - 1) + 1));
            products.add(product);
        }
        return products;
    }

    public static List<Order> generateOrders(int amount) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            Order order = new Order();
            order.setStatus(OrderStatus.NEW);
            order.setTotalAmount(BigDecimal.valueOf((long) random.nextInt(6000) + 40));
            orders.add(order);
        }
        return orders;
    }

    public static List<OrderItem> generateOrderItems(List<Order> orders, List<Product> products, int amount) {
        List<OrderItem> orderItems = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            Order randomOrder = orders.get(random.nextInt(orders.size()));
            Product randomProduct = products.get(random.nextInt(products.size()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(randomOrder);
            orderItem.setProduct(randomProduct);
            orderItem.setQuantity(random.nextInt(10) + 1);
            orderItem.setUnitPrice(BigDecimal.valueOf((long) random.nextInt(600) + 40));

            orderItems.add(orderItem);
        }
        return orderItems;
    }

}
