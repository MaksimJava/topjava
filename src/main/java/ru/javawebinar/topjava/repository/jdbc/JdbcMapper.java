package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ru.javawebinar.topjava.model.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Максим on 13.08.2017.
 */
public class JdbcMapper implements RowMapper<Meal> {
    @Override
    public Meal mapRow(ResultSet rs, int rowNum) throws SQLException {
        Meal meal = new Meal();
        meal.setCalories(rs.getInt("calories"));
        meal.setDescription(rs.getString("description"));
        meal.setId(rs.getInt("id"));
        meal.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        return meal;
    }
}