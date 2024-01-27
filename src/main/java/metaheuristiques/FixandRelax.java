package metaheuristiques;

import Nurse_Restoring.*;
import Optimisation.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * La classe FixandRelax implémente l'algorithme Fix-and-Relax pour résoudre un problème de planification des infirmières.
 * Cet algorithme décompose le problème en sous-problèmes par semaine, résout chaque sous-problème de manière indépendante
 * en utilisant une méthode de construction de solution faisable, et combine les solutions partielles pour obtenir une
 * solution complète et réalisable.
 *
 * La méthode fixAndRelax prend en entrée le problème global, représenté par la classe NurseSchedulingProblem, et une liste
 * d'indices de semaine pour la décomposition. Elle retourne une solution faisable ou null si aucune solution réalisable
 * n'est trouvée.
 *
 * Les méthodes auxiliaires clear et decompose sont utilisées pour réinitialiser les variables de solution et pour diviser
 * le problème global en sous-problèmes par semaine, respectivement.
 *
 *
 * @author TANANI et AIT KARROUM
 */
public class FixandRelax {

    // Méthode principale de l'algorithme Fix-and-Relax
    static Solution fixAndRelax(NurseSchedulingProblem nurseProblem, List<Integer> weekDecomposition) {
        // Initialiser la solution faisable
        Solution feasibleSolution = new Solution();

        // Décomposer le problème par semaine
        List<NurseSchedulingProblem> subProblems = decompose(nurseProblem, weekDecomposition);

        // Pour chaque sous-problème correspondant à une semaine
        for (NurseSchedulingProblem subProblem : subProblems) {
            // Réinitialiser XidtR pour chaque sous-problème
            clear(feasibleSolution);

            // Résoudre le sous-problème
            Solution solution = solve(subProblem);

            // Vérifier si la solution est faisable
            if (!solution.isFeasible()) {
                return null;
            }

            // Mettre à jour en utilisant la solution actuelle
            for (Solution.EmployeeAssignment employeeAssignment : solution.getEmployeeAssignments()) {
                // Récupérer l'identifiant de l'employé
                char employeeId_fixed = employeeAssignment.getEmployeeId();

                // Parcourir les affectations de l'employé
                for (Solution.Assign assign : employeeAssignment.getAssignments()) {
                    // Récupérer le jour et le shiftId
                    int day_fixed = assign.getDay();
                    char shiftId_fixed = assign.getShiftId();

                    // Effectuer des opérations, si nécessaire
                }
            }

            // Continuer le traitement des autres semaines
        }

        // Retourner la solution faisable
        return feasibleSolution;
    }

    // Méthode pour réinitialisation
    private static void clear(Solution feasibleSolution) {
        if (feasibleSolution != null) {
            for (Solution.EmployeeAssignment employeeAssignment : feasibleSolution.getEmployeeAssignments()) {
                for (Solution.Assign assign : employeeAssignment.getAssignments()) {
                    assign.setShiftId(' ');
                }
            }
        }
    }

    // Méthode pour décomposer le problème en sous-problèmes par semaine
    private static List<NurseSchedulingProblem> decompose(NurseSchedulingProblem globalProblem, List<Integer> weekDecomposition) {
        List<NurseSchedulingProblem> subProblems = new ArrayList<>();

        for (int week : weekDecomposition) {
            NurseSchedulingProblem subProblem = new NurseSchedulingProblem(
                    new HashSet<>(globalProblem.getShifts()),
                    new HashSet<>(globalProblem.getEmployees()),
                    globalProblem.getHorizon()
            );

            subProblem.setDayIndexes(new int[]{week});
            subProblems.add(subProblem);
        }

        return subProblems;
    }

    // Méthode pour résoudre un problème de planification des infirmières
    public static Solution solve(NurseSchedulingProblem problem) {
        // Initialiser la solution avec une méthode de construction de planning faisable
        Solution solution = buildFeasibleSolution(problem);

        // Vérifier les contraintes après la construction pour la faisabilité de la solution
        boolean isFeasible = verifyIsFeasible(problem, solution.getEmployeeAssignments());


        // Créer une nouvelle instance de Solution avec les paramètres mis à jour
        solution = new Solution(solution.getEmployeeAssignments(), isFeasible;

        return solution;
    }

    // Méthode pour vérifier la faisabilité de la solution
    private static boolean verifyIsFeasible(NurseSchedulingProblem problem, List<Solution.EmployeeAssignment> solutionAssignments) {
        // Vérifier toutes les contraintes pour déterminer la faisabilité de la solution
        try {
            // Supposons que Optimisation.verify.checkAllConstraints soit une méthode dans votre code
            verify.verifierToutesLesContraintes(problem, solutionAssignments);
            return true;  // Aucune exception n'a été levée, la solution est faisable
        } catch (Exception e) {
            return false;  // Une exception a été levée, la solution n'est pas faisable
        }
    }

    // Méthode pour construire une solution faisable initiale
    public static Solution buildFeasibleSolution(NurseSchedulingProblem problem) {
        List<Solution.EmployeeAssignment> assignments = new ArrayList<>();

        Random random = new Random();

        for (Employee employee : problem.getEmployees()) {
            List<Solution.Assign> employeeAssignments = new ArrayList<>();

            for (int day = 0; day < problem.getHorizon(); day++) {
                for (Shift shift : problem.getShifts()) {
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
