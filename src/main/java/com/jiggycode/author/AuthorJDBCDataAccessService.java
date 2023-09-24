package com.jiggycode.author;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class AuthorJDBCDataAccessService implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorRowMapper authorRowMapper;

    public AuthorJDBCDataAccessService(JdbcTemplate jdbcTemplate, AuthorRowMapper authorRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRowMapper = authorRowMapper;
    }

    @Override
    public List<Author> selectAllAuthors() {
        var sql = """
                SELECT id, name, email, age
                FROM author
                """;

        return jdbcTemplate.query(sql, authorRowMapper);

    }

    @Override
    public Optional<Author> selectAuthorById(Integer id) {
        var sql = """
                SELECT id, name, email, age
                FROM author
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, authorRowMapper, id)
                .stream()
                .findFirst();
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
        var sql = """
                SELECT count(id)
                FROM author
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsAuthorWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM author
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteAuthorById(Integer authorId) {
        var sql = """
                    DELETE
                    FROM author
                    WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, authorId);
        System.out.println("deleteAuthorById result = " + result);
    }

    @Override
    public void updateAuthor(Author update) {
        if (update.getName() != null) {
            String sql = "UPDATE author SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
            System.out.println("update author name result = " + result);
        }
        if (update.getAge() != null) {
            String sql = "UPDATE author SET age = ?  WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println("update author age result = " + result);
        }
        if (update.getEmail() != null) {
            String sql = "UPDATE author SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            System.out.println("update author email result = " + result);
        }
    }
}
