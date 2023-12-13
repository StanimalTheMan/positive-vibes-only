package com.jiggycode.author;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                "password",
                rs.getInt("age")
        );
        return author;
    }
}
