package ru.ivmiit.db.dao;

import ru.ivmiit.db.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoJdbcImpl implements UsersDao {

    //language=sql
    private final String SQL_SELET_ALL =
            "SELECT * FROM fix_users;";

    //language=sql
    private  final String SQL_SELET_BY_ID =
            "SELECT * FROM fix_users WHERE id = ?";

    private Connection connection;

    public UserDaoJdbcImpl(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<User> findAllByFirstName(String firstName) {
        return null;
    }

    @Override
    public Optional<User> find(Integer id) {
        try{
            PreparedStatement statement = connection.prepareStatement(SQL_SELET_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("name");
                String lastName = resultSet.getString("password");

                return  Optional.of(new User(id,firstName,lastName));

            }
            return Optional.empty();
        }catch (SQLException e){
            throw  new IllegalStateException(e);
        }
    }

    @Override
    public void save(User model) {

    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<User> findAll() {
        try{
            List<User> users = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELET_ALL);
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("name");
                String lastName = resultSet.getString("password");

                User user = new User(id,firstName,lastName);

                users.add(user);
            }
            return users;
        }catch (SQLException e){
            throw  new IllegalStateException(e);
        }
    }
}
