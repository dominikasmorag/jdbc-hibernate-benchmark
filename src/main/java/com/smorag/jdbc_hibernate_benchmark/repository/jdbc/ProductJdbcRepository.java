package com.smorag.jdbc_hibernate_benchmark.repository.jdbc;

import com.smorag.jdbc_hibernate_benchmark.entity.Product;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductJdbcRepository {
    private final DataSource dataSource;

    public ProductJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static final String INSERT_SQL = "INSERT INTO products(name, category, price, stock_quantity)" +
            " VALUES (?, ?, ?, ?)";

    private static final String DELETE_SQL = "DELETE FROM products";

    private static final String SELECT_SQL = "SELECT * FROM products";

    public void saveAll(List<Product> products) {
        System.out.println("jdbc save all");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            connection.setAutoCommit(false);
            for(Product product : products) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getCategory());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getStockQuantity());

                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void findProducts() {
        List<Product> productsFromDb = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_SQL);
            while(rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong(1));
                product.setName(rs.getString(2));
                product.setCategory(rs.getString(3));
                product.setPrice(rs.getBigDecimal(4));
                product.setStockQuantity(rs.getInt(5));
                product.setCreatedAt(rs.getTimestamp(6).toLocalDateTime());
                productsFromDb.add(product);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAll() {
        System.out.println("delete products JDBC");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
