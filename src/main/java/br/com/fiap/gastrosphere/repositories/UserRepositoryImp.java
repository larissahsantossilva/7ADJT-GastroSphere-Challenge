package br.com.fiap.gastrosphere.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.User;

@Repository
public class UserRepositoryImp implements UserRepository {

	private static final String FIND_USER_BY_ID_SQL = """
			SELECT
				u.id AS user_id,
				u."name",
				u.email,
				u.login,
				u."password",
				u.user_type,
				u.cpf,
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
			    		""";
	
	private static final String FIND_ALL_USER_SQL = """ 
			SELECT
				u.id AS user_id,
				u."name",
				u.email,
				u.login,
				u."password",
				u.user_type,
				u.cpf,
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
			""";
	
	private final JdbcClient jdbcClient;

	public UserRepositoryImp(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	@Override
	public List<User> findAll(int size, int offset) {
		return this.jdbcClient.sql(FIND_ALL_USER_SQL).param("size", size)
				.param("offset", offset).query(this::buildUser).list();
	}

	@Override
	public Optional<User> findById(UUID id) {
		return this.jdbcClient
				.sql(FIND_USER_BY_ID_SQL)
				.param("id", id)
				.query(this::buildUser)
				.optional();
	}

	private User buildUser(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(UUID.fromString(rs.getString("user_id")));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setUserType(rs.getString("user_type"));
		user.setCpf(rs.getString("cpf"));
		user.setCreatedAt(rs.getDate("created_at").toLocalDate());
		user.setLastModifiedAt(rs.getDate("last_modified_at").toLocalDate());
		user.setAddressNumber(rs.getString("address_number"));
		user.setAddressComplement(rs.getString("address_complement"));
		user.setAddressId(rs.getString("address_id"));
		user.setAddress(buildAddress(rs));
		return user;
	}

	private Address buildAddress(ResultSet rs) throws SQLException {
		Address address = new Address();
		address.setId(UUID.fromString(rs.getString("address_id")));
		address.setCountry(rs.getString("country"));
		address.setState(rs.getString("state"));
		address.setCity(rs.getString("city"));
		address.setZipCode(rs.getString("zip_code"));
		address.setStreet(rs.getString("street"));
		return address;
	}
}
