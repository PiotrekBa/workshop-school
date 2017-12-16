package program;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import entities.Exercise;
import entities.User;
import utils.Connector;

public class AdminExerciseManager {

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
			AllExercises();
			Menu();
			String opt = scan.nextLine();
			if (opt.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			} else if (opt.equalsIgnoreCase("add")) {
				System.out.println("Podaj tytuł zadania:");
				String title = scan.nextLine();
				System.out.println("Podaj opis zadania:");
				String description = scan.nextLine();
				Exercise exercise = new Exercise(title, description);
				System.out.println("Podaj id grupy użytkownika:");
				try {
					exercise.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("edit")) {
				System.out.println("Podaj id zadania, którego chcesz zmienić:");
				int id = getNumber();
				Exercise exercise = null;
				try {
					exercise = Exercise.loadExerciseById(conn, id);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Podaj tytuł zadania:");
				String title = scan.nextLine();
				System.out.println("Podaj opis zadania:");
				String description = scan.nextLine();
				exercise.setTitle(title);
				exercise.setDescription(description);
				try {
					exercise.saveToDB(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("delete")) {
				System.out.println("Podaj id zadania, które chcesz usunąć:");
				int id = getNumber();
				try {
					Exercise exercise = Exercise.loadExerciseById(conn, id);
					exercise.delete(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Nie ma takiej opcji. Podaj poprawną:");
			}
		}

	}

	public static void printAllExercises(Exercise[] allExercises) {
		System.out.println("id\t|tytuł\t\t\t|opis\t\t\t|");
		for (Exercise exercise : allExercises) {
			System.out.print(exercise.getId() + "\t|");
			System.out.print(exercise.getTitle() + "\t\t|");
			System.out.print(exercise.getDescription() + "\t\t\t|");
			System.out.println();
		}
	}

	public static void Menu() {
		System.out.println();
		System.out.println("Wybierz jedną opcję: ");
		System.out.println("add - dodawanie zadania");
		System.out.println("edit - edycja zadania");
		System.out.println("delete - edycja zadania");
		System.out.println("quit - zakończenie programu");
	}

	public static void AllExercises() {
		System.out.println("Lista wszystkich zadań: ");
		System.out.println();
		try {
			Connection conn = Connector.getConnection();
			Exercise[] allExercises = Exercise.loadAllExercises(conn);
			printAllExercises(allExercises);
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
