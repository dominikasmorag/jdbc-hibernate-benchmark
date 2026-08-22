package com.smorag.jdbc_hibernate_benchmark.repository.jdbc;


import com.smorag.jdbc_hibernate_benchmark.entity.Order;
import com.smorag.jdbc_hibernate_benchmark.entity.User;
import enums.OrderStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderJdbcRepository {
    private final DataSource dataSource;
    private static final String INSERT_SQL = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM orders";
    private static final String DELETE_SQL = "DELETE FROM orders";
    private static final String UPDATE_SQL = "UPDATE orders SET status = ?";
    private final UserJdbcRepository userJdbcRepository;

    public OrderJdbcRepository(DataSource dataSource, UserJdbcRepository userJdbcRepository) {
        this.dataSource = dataSource;
        this.userJdbcRepository = userJdbcRepository;
    }

    public void saveAll(List<Order> orders) {
        //batch
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL) ) {
            connection.setAutoCommit(false);
            for(Order order : orders) {
                ps.setLong(1, order.getUser().getId());
                ps.setBigDecimal(2, order.getTotalAmount());
                ps.setString(3, order.getStatus().name());
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Order> findAll() {
        Map<Long, User> usersCache = new HashMap<>();
        List<Order> ordersFromDb = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_SQL);
            while(rs.next()) {
                Long userId = rs.getLong("user_id");
                User user = usersCache.get(userId);

                if(user==null) {
                    user = userJdbcRepository.findById(userId);
                    usersCache.put(userId, user);
                }
                Order order = new Order();
                order.setId(rs.getLong(1));
                order.setUser(user);
                order.setTotalAmount(rs.getBigDecimal(3));
                order.setStatus(OrderStatus.valueOf(rs.getString(4)));
                order.setCreatedAt(rs.getTimestamp(5).toLocalDateTime());
                ordersFromDb.add(order);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ordersFromDb;
    }


    public void updateAllStatuses(OrderStatus status) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)
        ) {
            ps.setString(1, status.name());
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAll() {
        System.out.println("delete orders JDBC");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
