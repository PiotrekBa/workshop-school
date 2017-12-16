package program;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;

import entities.Exercise;
import entities.Solution;
import entities.User;
import utils.Connector;

public class AdminSolutionManager {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Connection conn = null;
		try {
			conn = Connector.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		while (true) {
			Menu();
			String opt = scan.nextLine();
			if (opt.equalsIgnoreCase("quit")) {
				scan.close();
				return;
			} else if (opt.equalsIgnoreCase("add")) {
				AdminUserManager.AllUsers();
				System.out.println("Podaj id użytkownika:");
				int userId = getNumber();
				AdminExerciseManager.AllExercises();
				System.out.println("Podaj id zadania");
				int exerciseId = getNumber();
				Solution solution = new Solution(null, null, null );
				solution.setExercise_id(exerciseId);
				solution.setUser_id(userId);
				try {
					solution.saveToDB(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (opt.equalsIgnoreCase("view")) {
				AdminUserManager.AllUsers();
				System.out.println("Podaj id użytkownika, którego chcesz rozwiązania zobaczyć:");
				int id = getNumber();
				try {
					Solution[] solutions = Solution.loadAllByUserId(id, conn);
					printAllSolutions(solutions, conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			} else {
				System.out.println("Nie ma takiej opcji. Podaj poprawną:");
			}
		}

	}



	public static void Menu() {
		System.out.println();
		System.out.println("Wybierz jedną opcję: ");
		System.out.println("add - przypisywanie zadań do użytkowników");
		System.out.println("view - przeglądanie rozwiązań danego użytkownika");
		System.out.println("quit - zakończenie programu");
	}

	public static int getNumber() {
		Scanner scan = new Scanner(System.in);
		while (!scan.hasNextInt()) {
			scan.nextLine();
			System.out.println("To nie jest liczba. Podaj jeszcze raz");
		}
		return scan.nextInt();
	}
	
	public static void printAllSolutions(Solution[] solutions, Connection conn) throws SQLException {
		System.out.println("id\t|data\t\t|data akt.\t|tytuł zadania\t|opis\t\t\t");
		for (Solution solution : solutions) {
			System.out.print(solution.getId() + "\t|");
			System.out.print(solution.getCreated() + "\t|");
			System.out.print(solution.getUpdated() + "\t|");
			Exercise exercise = Exercise.loadExerciseById(conn,solution.getExercise_id());
			System.out.print(exercise.getTitle()+"\t|");
			System.out.print(solution.getDescription());
			System.out.println();
		}
	}
}
