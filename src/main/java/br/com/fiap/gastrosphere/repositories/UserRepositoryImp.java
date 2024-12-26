package br.com.fiap.gastrosphere.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.entities.User;

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
    
    public Optional<User> findById(UUID id) {
		return this.jdbcClient.sql("SELECT * FROM gastrosphere.users WHERE id = :id")
				.param("id", id)
				.query(User.class)
				.optional();
	}
}
