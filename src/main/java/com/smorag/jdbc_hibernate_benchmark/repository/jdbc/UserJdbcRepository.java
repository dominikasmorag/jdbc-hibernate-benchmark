package com.smorag.jdbc_hibernate_benchmark.repository.jdbc;

import com.smorag.jdbc_hibernate_benchmark.entity.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserJdbcRepository {
    private final DataSource dataSource;

    public UserJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static final String INSERT_SQL = "INSERT INTO users(first_name, last_name, email)" +
            " VALUES (?, ?, ?)";

    public static final String DELETE_SQL = "DELETE FROM users";

    public static final String SELECT_SQL = "SELECT * FROM users";

    public static final String UPDATE_SQL = "UPDATE users SET first_name = ?";

    public List<User> saveAll(List<User> users) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            connection.setAutoCommit(false);
            for(User user : users) {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getEmail());

                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<User> findAll() {
        List<User> usersFromDb = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_SQL);
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setFirstName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setCreatedAt(rs.getTimestamp(5).toLocalDateTime());
                usersFromDb.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usersFromDb;
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    return user;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updateAllFirstNames(int iteration) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {
            connection.setAutoCommit(false);
            ps.setString(1, "Updated_" + iteration);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAll() {
        System.out.println("delete users JDBC");
        try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
