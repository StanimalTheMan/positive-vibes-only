package com.jiggycode.author;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class AuthorJDBCDataAccessService implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorJDBCDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> selectAllAuthors() {
        var sql = """
                SELECT id, name, email, age
                FROM author
                """;
        RowMapper<Author> authorRowMapper = (rs, rowNum) -> {
            Author author = new Author(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("age")
            );
            return author;
        };

        List<Author> authors = jdbcTemplate.query(sql, authorRowMapper);

        return authors;
    }

    @Override
    public Optional<Author> selectAuthorById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void insertAuthor(Author author) {
        var sql = """
                INSERT INTO author(name, email, age)
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                author.getName(),
                author.getEmail(),
                author.getAge()
        );

        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsAuthorWithEmail(String email) {
        return false;
    }

    @Override
    public boolean existsAuthorWithId(Integer id) {
        return false;
    }

    @Override
    public void deleteAuthorById(Integer authorId) {

    }

    @Override
    public void updateAuthor(Author update) {

    }
}
