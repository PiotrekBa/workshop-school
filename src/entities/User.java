package entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import utils.Connector;

public class User {
	private int id;
	private int user_group_id;
	private String username;
	private String email;
	private String password;

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.setPassword(password);
	}

	public User() {
	}

	public int getId() {
		return id;
	}

	public int getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(int user_group_id) {
		this.user_group_id = user_group_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {

		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.user_group_id = resultSet.getInt("user_group_id");
			users.add(loadedUser);
		}
		User[] uArray = new User[users.size()];
		uArray = users.toArray(uArray);
		return uArray;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user_group_id=" + user_group_id + ", username=" + username + ", email=" + email
				+ ", password=" + password + "]";
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.user_group_id);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE users SET username=?, email=?, password=?, user_group_id=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.user_group_id);
			preparedStatement.setInt(5, this.id);
			preparedStatement.executeUpdate();
		}
	}

	static public User loadUserById(Connection conn, int id) throws SQLException {

		String sql = "SELECT * FROM users where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {

			User loadedUser = new User();
			loadedUser.id = resultSet.getInt("id");
			loadedUser.username = resultSet.getString("username");
			loadedUser.password = resultSet.getString("password");
			loadedUser.email = resultSet.getString("email");
			loadedUser.user_group_id = resultSet.getInt("user_group_id");
			return loadedUser;
		}
		return null;
	}

	public void delete(Connection conn) throws SQLException {

		if (this.id != 0) {

			String sql = "DELETE FROM users WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public User[] loadAllByGroupId(Connection conn, int id) throws SQLException {
		String sql = "select * from users where user_group_id =?;";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet rs = preparedStatement.executeQuery();
		List<User> users = new ArrayList<>();
		while (rs.next()) {
			User user = new User(rs.getString("username"), rs.getString("email"), "");
			user.id = rs.getInt("id");
			users.add(user);
		}
		User[] loadedUsers = new User[users.size()];
		loadedUsers = users.toArray(loadedUsers);

		return loadedUsers;

	}

	public static void main(String[] args) throws SQLException {
		// User user = new User("frankee", "frankee@wp.pl", "paczek");
		// user.setUser_group_id(1);

		Connection conn = Connector.getConnection();
		// User user = loadUserById(conn, 3);
		// user.saveToDB(conn);
		// user.delete(conn);
		System.out.println(Arrays.toString(loadAllUsers(conn)));
	}
}
