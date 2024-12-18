package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImp implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImp(JdbcClient jdbcClient) {this.jdbcClient = jdbcClient; }


    @Override
    public List<User> findAll(int size, int offset) {
        return this.jdbcClient
                .sql("SELECT * FROM gastrosphere.users LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query(User.class)
                .list();
    }
}
