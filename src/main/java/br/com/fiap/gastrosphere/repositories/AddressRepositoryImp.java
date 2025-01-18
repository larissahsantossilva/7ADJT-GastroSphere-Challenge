package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressRepositoryImp implements AddressRepository {

    private final JdbcClient jdbcClient;

    public AddressRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Address> findAll(int size, int offset) {
        return this.jdbcClient
                .sql("SELECT * FROM gastrosphere.addresses LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query(Address.class)
                .list();
    }

    @Override
    public Optional<Address> findById(UUID id) {
        return this.jdbcClient.sql("SELECT * FROM gastrosphere.addresses WHERE id = :id")
                .param("id", id)
                .query(Address.class)
                .optional();
    }

    @Override
    public Optional<Address> findByZipCode(String zipCode) {
        return this.jdbcClient
                .sql("SELECT * FROM gastrosphere.addresses WHERE zip_code = :zipCode")
                .param("zipCode", zipCode)
                .query(Address.class)
                .optional();
    }

    @Override
    public Optional<Integer> create(Address address) {
        return Optional.of(this.jdbcClient.sql("INSERT INTO gastrosphere.addresses (country, state, city, zip_code, street) VALUES (:country, :state, :city, :zipCode, :street)")
                .param("country", address.getCountry())
                .param("state", address.getState())
                .param("city", address.getCity())
                .param("zipCode", address.getZipCode())
                .param("street", address.getStreet())
                .update());
    }

    @Override
    public Optional<Integer> update(UUID id, Address address) {
        return Optional.of(this.jdbcClient.sql("UPDATE gastrosphere.addresses SET country = :country, state = :state, city = :city, zip_code = :zipCode, street = :street WHERE id = :id")
                .param("id", id)
                .param("country", address.getCountry())
                .param("state", address.getState())
                .param("city", address.getCity())
                .param("zipCode", address.getZipCode())
                .param("street", address.getStreet())
                .update());
    }

    @Override
    public Integer delete(UUID id) {
        return this.jdbcClient.sql("DELETE FROM gastrosphere.addresses WHERE id = :id")
                .param("id", id)
                .update();
    }
}
