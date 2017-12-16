package program;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import entities.Exercise;
import entities.Solution;
import entities.User;
import utils.Connector;

public class UserProgram {

	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		try {
			conn = Connector.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Integer userId = Integer.parseInt(args[0]);
		User user = null;
		if (userId != null) {
			try {
				user = User.loadUserById(conn, userId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (user != null) {
				Scanner scan = new Scanner(System.in);
				while (true) {
					Menu();
					String opt = scan.nextLine();
					if (opt.equalsIgnoreCase("quit")) {
						scan.close();
						return;
					} else if (opt.equalsIgnoreCase("add")) {
						List<Exercise> userExercises = Arrays.asList(Exercise.loadAllByUserId(conn, userId));
						List<Exercise> allExercises = Arrays.asList(Exercise.loadAllExercises(conn));
						List<Exercise> undoneExercises = new ArrayList<>();
						undoneExercises.addAll(allExercises);
						Iterator<Exercise> it1 = undoneExercises.iterator();
						while(it1.hasNext()) {
							Iterator<Exercise> it2 = userExercises.iterator();
							Exercise e1 = it1.next();
							while(it2.hasNext()) {
								if (e1.getId() == it2.next().getId()) {
									it1.remove();
									break;
								}
							}
						}
						Exercise[] unEx = new Exercise[undoneExercises.size()];
						unEx = undoneExercises.toArray(unEx);
						AdminExerciseManager.printAllExercises(unEx);
						System.out.println("Podaj id rozwiązania, które chcesz dodać: ");
						int exerciseId = AdminExerciseManager.getNumber();
						boolean canAdd = false;
						for  (Exercise e : unEx) {
							if (e.getId() == exerciseId) {
								canAdd = true;
								break;
							}
						}
						if (canAdd) {
							System.out.println("Podaj rozwiązanie zadania");
							String description = scan.nextLine();
							Solution solution = new Solution(null, null, description);
							solution.setExercise_id(exerciseId);
							solution.setUser_id(userId);
							solution.saveToDB(conn);
							
						} else {
							System.out.println("Nie można dodać rozwiązania");
						}
						
					} else if (opt.equalsIgnoreCase("view")) {
						Solution[] solutions = Solution.loadAllByUserId(userId, conn);
						AdminSolutionManager.printAllSolutions(solutions, conn);
						
					} else {
						System.out.println("Nie ma takiej opcji. Podaj poprawną:");
					}
				}
			}
		}
		
	}

	public static void Menu() {
		System.out.println();
		System.out.println("Wybierz jedną opcję: ");
		System.out.println("add - dodawanie rozwiązania");
		System.out.println("view - przeglądanie swoich rozwiązań");
		System.out.println("quit - zakończenie programu");
	}
}
