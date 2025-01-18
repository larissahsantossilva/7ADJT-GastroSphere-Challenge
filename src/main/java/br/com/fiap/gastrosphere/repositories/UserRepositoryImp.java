package br.com.fiap.gastrosphere.repositories;

import static java.util.UUID.fromString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.dtos.UserResponse;
import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.User;

@Repository
public class UserRepositoryImp implements UserRepository {

	private final JdbcClient jdbcClient;

	public UserRepositoryImp(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public List<UserResponse> findAll(int size, int offset) {
		return this.jdbcClient.sql(""" 
			SELECT
				u.id AS user_id,
				u."name",
				u.email,
				u.login,
				u."password",
				u.user_type,
				u.document,
				u.created_at,
				u.last_modified_at,
				u.address_number,
				u.address_complement,
				a.id as address_id,
				a.country,
				a.state,
				a.city,
				a.zip_code,
				a.street
				FROM gastrosphere.users u
				INNER JOIN gastrosphere.addresses a
				ON u.address_id  = a.id
				LIMIT :size OFFSET :offset
			""")
				.param("size", size)
				.param("offset", offset)
				.query(this::buildUser).list();
	}

	@Override
	public Optional<UserResponse> findById(UUID id) {
		return this.jdbcClient
				.sql("""
						SELECT
						u.id AS user_id,
						u."name",
						u.email,
						u.login,
						u."password",
						u.user_type,
						u.document,
						u.created_at,
						u.last_modified_at,
						u.address_number,
						u.address_complement,
						a.id as address_id,
						a.country,
						a.state,
						a.city,
						a.zip_code,
						a.street
						FROM gastrosphere.users u
						INNER JOIN gastrosphere.addresses a
						ON u.address_id  = a.id
					WHERE u.id = :id
					    		""")
				.param("id", id)
				.query(this::buildUser)
				.optional();
	}

	@Override
	public Optional<UserResponse> findByAddressId(UUID id) {
		return this.jdbcClient
				.sql("""
						SELECT
						u.id AS user_id,
						u."name",
						u.email,
						u.login,
						u."password",
						u.user_type,
						u.document,
						u.created_at,
						u.last_modified_at,
						u.address_number,
						u.address_complement,
						a.id as address_id,
						a.country,
						a.state,
						a.city,
						a.zip_code,
						a.street
						FROM gastrosphere.users u
						INNER JOIN gastrosphere.addresses a
						ON u.address_id  = a.id
					WHERE a.id = :id
					    		""")
				.param("id", id)
				.query(this::buildUser)
				.optional();
	}

	@Override
	public Optional<Integer> deleteById(UUID id) {
		 return Optional.of(this.jdbcClient.sql("DELETE FROM gastrosphere.users WHERE id = :id")
				.param("id", id)
			.update());
	}

	@Override
	public Optional<Integer> create(User user) {
		return Optional.of(
			this.jdbcClient.sql("INSERT INTO gastrosphere.users " +
						"(name, email, login, password, user_type, document, address_id, address_number, address_complement, created_at, last_modified_at) " +
						"VALUES (:name, :email, :login, :password, :user_type, :document, :address_id, :address_number, :address_complement," +
						":created_at, :last_modified_at)")
				.param("name", user.getName())
				.param("email", user.getEmail())
				.param("login", user.getLogin())
				.param("password", user.getPassword())
				.param("user_type", user.getUserType())
				.param("document", user.getDocument())
				.param("address_id", user.getAddressId())
				.param("address_number", user.getAddressNumber())
				.param("address_complement", user.getAddressComplement())
				.param("created_at", LocalDate.now())
				.param("last_modified_at", LocalDate.now())
			.update());
	}

	@Override
	public Optional<Integer> update(User user, UUID id) {
		return Optional.of(this.jdbcClient.sql("UPDATE gastrosphere.users " +
						"SET name = :name, email = :email, login = :login, password = :password, user_type = :user_type, " +
						"document = :document, address_id = :address_id, address_number = :address_number, address_complement = :address_complement, " +
						"last_modified_at = :last_modified_at " +
						"WHERE id = :id")
				.param("id", id)
				.param("name", user.getName())
				.param("email", user.getEmail())
				.param("login", user.getEmail())
				.param("password", user.getPassword())
				.param("user_type", user.getUserType())
				.param("document", user.getDocument())
				.param("address_id", user.getAddressId() != null ? user.getAddressId() : null)
				.param("address_number", user.getAddressNumber())
				.param("address_complement", user.getAddressComplement())
				.param("last_modified_at", LocalDate.now())
			.update());
	}

	private UserResponse buildUser(ResultSet rs, int rowNum) throws SQLException {
		UserResponse user = new UserResponse(
				fromString(rs.getString("user_id")),
				rs.getString("name"), 
				rs.getString("email"),
				rs.getString("login"),
				rs.getString("password"), 
				rs.getString("user_type"), 
				rs.getString("document"),
				rs.getString("address_number"),
				rs.getString("address_complement"), 
				rs.getDate("created_at").toLocalDate(), 
				rs.getDate("last_modified_at").toLocalDate(),
				this.buildAddress(rs));
		return user;
	}

	private Address buildAddress(ResultSet rs) throws SQLException {
		Address address = new Address();
		address.setId(fromString(rs.getString("address_id")));
		address.setCountry(rs.getString("country"));
		address.setState(rs.getString("state"));
		address.setCity(rs.getString("city"));
		address.setZipCode(rs.getString("zip_code"));
		address.setStreet(rs.getString("street"));
		return address;
	}

	@Override
	public Integer updatePassword(UUID id, String newPassword) {
		return this.jdbcClient.sql("""
						    UPDATE gastrosphere.users
						    SET password = :newPassword, last_modified_at = :lastModifiedAt
						    WHERE id = :id
						""")
				.param("id", id)
				.param("newPassword", newPassword)
				.param("lastModifiedAt", LocalDate.now())
				.update();
	}

}
