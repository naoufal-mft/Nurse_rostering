package Optimisation;
import Nurse_Restoring.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe Solve encapsule les méthodes pour résoudre le problème de planification des infirmières.
 * Elle fournit des fonctionnalités pour construire une solution initiale faisable, vérifier sa faisabilité,
 * calculer son coût et résoudre le problème en utilisant ces fonctionnalités.
 *
 * @author MEFTAHI
 */
public class Solve {

    /**
     * Méthode principale pour résoudre le problème de planification des infirmières.
     * @param problem Le problème de planification des infirmières à résoudre.
     * @return Une solution au problème donné.
     */
    public static Solution solve(NurseSchedulingProblem problem) {
        // Initialiser la solution avec une méthode de construction de planning faisable
        Solution solution = buildFeasibleSolution(problem);

        // Vérifier les contraintes après la construction pour la faisabilité de la solution
        boolean isFeasible = verifyIsFeasible(problem, solution.getEmployeeAssignments());

        // Calculer le coût de la solution
        int cost = calculateSolutionCost(problem, solution.getEmployeeAssignments());

        // Créer une nouvelle instance de Solution avec les paramètres mis à jour
        solution = new Solution(solution.getEmployeeAssignments(), isFeasible, cost);

        return solution;
    }

    // Méthode pour vérifier la faisabilité de la solution
    private static boolean verifyIsFeasible(NurseSchedulingProblem problem, List<Solution.EmployeeAssignment> solutionAssignments) {
        // Vérifier toutes les contraintes pour déterminer la faisabilité de la solution
        try {
            verify.verifierToutesLesContraintes(problem, solutionAssignments);
            return true;  // Aucune exception n'a été levée, la solution est faisable
        } catch (Exception e) {
            return false;  // Une exception a été levée, la solution n'est pas faisable
        }
    }

    // Méthode pour calculer le coût de la solution
    private static int calculateSolutionCost(NurseSchedulingProblem problem, List<Solution.EmployeeAssignment> solutionAssignments) {
        // Implémentez la logique pour calculer le coût de la solution
        return 0;  // Remplacer par votre logique réelle
    }

    // Méthode pour construire une solution faisable initiale
    private static Solution buildFeasibleSolution(NurseSchedulingProblem problem) {
        List<Solution.EmployeeAssignment> assignments = new ArrayList<>();
        Random random = new Random();

        for (Employee employee : problem.getEmployees()) {
            List<Solution.Assign> employeeAssignments = new ArrayList<>();

            for (int day = 0; day < problem.getHorizon(); day++) {
                for (Nurse_Restoring.Shift shift : problem.getShifts()) {
                    // Générer une affectation aléatoire
                    boolean isAssigned = random.nextBoolean();

                    // Ajouter l'assignation à la liste d'assignations de l'employé
                    employeeAssignments.add(new Solution.Assign(day, (char) shift.getId()));
                }
            }

            // Ajouter l'EmployeeAssignment à la liste d'assignations globale
            assignments.add(new Solution.EmployeeAssignment((char) employee.getEmployeeID(), employeeAssignments));
        }

        // Retourner la solution complète avec les valeurs par défaut pour isFeasible et cost
        Solution solution = new Solution(assignments);

        return solution;
    }
}
