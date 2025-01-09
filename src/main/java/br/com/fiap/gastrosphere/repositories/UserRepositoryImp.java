package br.com.fiap.gastrosphere.repositories;

import br.com.fiap.gastrosphere.entities.Address;
import br.com.fiap.gastrosphere.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	@Override
	public Optional<Integer> deleteById(UUID id) {
		int result = this.jdbcClient.sql("DELETE FROM gastrosphere.users WHERE id = :id")
				.param("id", id)
				.update();
		return Optional.of(result);
	}

	@Override
	public Integer create(User user) {
		return this.jdbcClient.sql("INSERT INTO gastrosphere.users " +
						"(name, email, login, password, user_type, cpf, address_id, address_number, address_complement, created_at, last_modified_at) " +
						"VALUES (:name, :email, :login, :password, :user_type, :cpf, :address_id, :address_number, :address_complement," +
						":created_at, :last_modified_at)")
				.param("name", user.getName())
				.param("email", user.getEmail())
				.param("login", user.getLogin())
				.param("password", user.getPassword())
				.param("user_type", user.getUserType())
				.param("cpf", user.getCpf())
				.param("address_id", user.getAddressId())
				.param("address_number", user.getAddressNumber())
				.param("address_complement", user.getAddressComplement())
				.param("created_at", LocalDate.now())
				.param("last_modified_at", LocalDate.now())
				.update();
	}

	@Override
	public Integer update(User user, UUID id) {
		return this.jdbcClient.sql("UPDATE gastrosphere.users " +
						"SET name = :name, email = :email, login = :login, password = :password, user_type = :user_type, " +
						"cpf = :cpf, address_id = :address_id, address_number = :address_number, address_complement = :address_complement, " +
						"last_modified_at = :last_modified_at " +
						"WHERE id = :id")
				.param("id", id)
				.param("name", user.getName())
				.param("email", user.getEmail())
				.param("login", user.getEmail())
				.param("password", user.getPassword())
				.param("user_type", user.getUserType())
				.param("cpf", user.getCpf())
				.param("address_id", user.getAddressId() != null ? user.getAddressId() : null)
				.param("address_number", user.getAddressNumber())
				.param("address_complement", user.getAddressComplement())
				.param("last_modified_at", LocalDate.now())
				.update();
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
//		user.setAddressId(rs.getString("address_id"));
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
