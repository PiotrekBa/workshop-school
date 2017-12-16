package program;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import entities.User;
import utils.Connector;

public class AdminUserManager {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Connection conn = null;
		try {
			conn = Connector.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true) {
			AllUsers();
			Menu();
			String opt = scan.nextLine();
			if (opt.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			} else if (opt.equalsIgnoreCase("add")) {
				System.out.println("Podaj nazwę użytkownika:");
				String username = scan.nextLine();
				System.out.println("Podaj emaila użytkownika:");
				String email = scan.nextLine();
				System.out.println("Podaj hasło użytkownika:");
				String password = scan.nextLine();
				User user = new User(username, email, password);
				System.out.println("Podaj id grupy użytkownika:");
				int groupId = getNumber();
				user.setUser_group_id(groupId);
				try {
					user.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("edit")) {
				System.out.println("Podaj id użytkownika, którego chcesz zmienić:");
				int id = getNumber();
				User user = null;
				try {
					user = User.loadUserById(conn, id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Podaj nazwę użytkownika:");
				String username = scan.nextLine();
				System.out.println("Podaj emaila użytkownika:");
				String email = scan.nextLine();
				System.out.println("Podaj hasło użytkownika:");
				String password = scan.nextLine();
				System.out.println("Podaj id grupy użytkownika:");
				int groupId = getNumber();
				user.setUsername(username);
				user.setEmail(email);
				user.setPassword(password);
				user.setUser_group_id(groupId);
				try {
					user.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("delete")) {
				System.out.println("Podaj id użytkownika, którego chcesz usunąć:");
				int id = getNumber();
				try {
					User user = User.loadUserById(conn, id);
					user.delete(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Nie ma takiej opcji. Podaj poprawną:");
			}
		}

	}

	public static void printAllUsers(User[] allUsers) {
		System.out.println("id\t|nazwa\t\t\t|email\t\t\t|");
		for (User user : allUsers) {
			System.out.print(user.getId() + "\t|");
			System.out.print(user.getUsername() + "\t\t\t|");
			System.out.print(user.getEmail() + "\t\t|");
			System.out.println();
		}
	}

	public static void Menu() {
		System.out.println();
		System.out.println("Wybierz jedną opcję: ");
		System.out.println("add - dodawanie użytkownika");
		System.out.println("edit - edycja użytkownika");
		System.out.println("delete - edycja użytkownika");
		System.out.println("quit - zakończenie programu");
	}

	public static void AllUsers() {
		System.out.println("Lista wszystkich użytkowników: ");
		System.out.println();
		try {
			Connection conn = Connector.getConnection();
			User[] allUsers = User.loadAllUsers(conn);
			printAllUsers(allUsers);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getNumber() {
		Scanner scan = new Scanner(System.in);
		while (!scan.hasNextInt()) {
			scan.nextLine();
			System.out.println("To nie jest liczba. Podaj jeszcze raz");
		}
		return scan.nextInt();
	}

}
