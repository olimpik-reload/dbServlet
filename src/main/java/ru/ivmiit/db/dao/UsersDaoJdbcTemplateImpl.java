package ru.ivmiit.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.ivmiit.db.model.Car;
import ru.ivmiit.db.model.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UsersDaoJdbcTemplateImpl implements UsersDao {

    //language=sql
    private final String SQL_SELET_ALL =
            "SELECT fix_users.*,fix_car.id as car_id, fix_car.model FROM fix_users, fix_car " +
                    "WHERE fix_users.id = fix_car.owner_id;";

    private final String SQL_ALL_BY_NAME =
            "SELECT * FROM fix_users WHERE name = ?";

    //language=sql
    private final String SQL_SELECt_USER_WITH_CAR =
            "SELECT fix_users.*, fix_car.id as car_id, fix_car.model FROM fix_users LEFT JOIN fix_car ON fix_users.id = fix_car.owner_id WHERE fix_users.id = ?";

    private JdbcTemplate template;

    private Map<Integer, User> userMap = new HashMap<>();

    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userRowMapper
            = (ResultSet resultSet, int i) -> {
        Integer id = resultSet.getInt("id");

        if (!userMap.containsKey(id)) {
            String firstName = resultSet.getString("name");
            String lastName = resultSet.getString("password");
            User user = new User(id, firstName, lastName, new ArrayList<>());
            userMap.put(id, user);
        }

        Car car = new Car(resultSet.getInt("car_id"),
                resultSet.getString("model"), userMap.get(id));

        userMap.get(id).getCars().add(car);

        return userMap.get(id);
    };


    @Override
    public List<User> findAllByFirstName(String firstName) {
        return template.query(SQL_ALL_BY_NAME, userRowMapper, firstName);
    }

    @Override
    public Optional<User> find(Integer id) {
        template.query(SQL_SELECt_USER_WITH_CAR, userRowMapper,id);

        if (userMap.containsKey(id)) {
            return Optional.of(userMap.get(id));
        }
        return Optional.empty();
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
        return template.query(SQL_SELET_ALL, userRowMapper);
    }
}
