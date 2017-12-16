package program;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import entities.Exercise;
import entities.Group;
import utils.Connector;

public class AdminGroupManager {
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
			AllGroups();
			Menu();
			String opt = scan.nextLine();
			if (opt.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			} else if (opt.equalsIgnoreCase("add")) {
				System.out.println("Podaj nazwe grupy:");
				String name = scan.nextLine();
				Group group = new Group(name);
				try {
					group.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("edit")) {
				System.out.println("Podaj id grupy, którą chcesz zmienić:");
				int id = getNumber();
				Group group = null;
				try {
					group = group.loadGroupById(conn, id);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				System.out.println("Podaj nazwe grupy:");
				String name = scan.nextLine();
				group.setName(name);
				try {
					group.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("delete")) {
				System.out.println("Podaj id grupy, którą chcesz usunąć:");
				int id = getNumber();
				try {
					Group group = Group.loadGroupById(conn, id);
					group.delete(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Nie ma takiej opcji. Podaj poprawną:");
			}
		}

	}

	public static void printAllGroups(Group[] allGroups) {
		System.out.println("id\t|nazwa grupy\t|");
		for (Group group : allGroups) {
			System.out.print(group.getId() + "\t|");
			System.out.print(group.getName() + "\t\t|");
			System.out.println();
		}
	}

	public static void Menu() {
		System.out.println();
		System.out.println("Wybierz jedną opcję: ");
		System.out.println("add - dodawanie grupy");
		System.out.println("edit - edycja grupy");
		System.out.println("delete - edycja grupy");
		System.out.println("quit - zakończenie programu");
	}

	public static void AllGroups() {
		System.out.println("Lista wszystkich grup: ");
		System.out.println();
		try {
			Connection conn = Connector.getConnection();
			Group[] allGroups = Group.loadAllGroups(conn);
			printAllGroups(allGroups);
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
