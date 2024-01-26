package metaheuristiques;

import Nurse_Restoring.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
/**
 * La classe FixAndOptimize représente une partie de notre métaheuristique qui vise à améliorer une solution initiale
 * pour le problème de planification des infirmières. Elle utilise une approche de fixation et d'optimisation
 * pour explorer différentes solutions en décomposant le problème initial par jour.
 *
 * La méthode fixAndOptimize prend une solution initiale en entrée et tente de trouver une meilleure solution
 * en résolvant des sous-problèmes pour chaque jour, en réinitialisant certaines variables et en optimisant
 * les affectations d'employés.
 *
 * Cette classe utilise la classe Solution pour représenter les solutions du problème de planification des infirmières.
 * Les fonctions solve, verifyIsFeasible et calculateSolutionCost sont utilisées pour construire, vérifier la faisabilité
 * et calculer le coût d'une solution.
 *
 * @author MEFTAHI
 */
public class FixAndOptimize {

    static Solution fixAndOptimize(Solution initialSolution) {
        //la meilleure solution est initialisée avec la solution initiale
        Solution bestSolution = initialSolution;

        // Décomposer le problème par jour
        List<NurseSchedulingProblem> subProblems = decompose(new NurseSchedulingProblem());//Nurse_Restoring.NurseSchedulingProblem prendra en input les valeurs qu on a depuis chaque instance

        //mise à jour des variables avec les valeurs de la solution initiale
        for (Solution.EmployeeAssignment employeeAssignment : initialSolution.getEmployeeAssignments()) {
            // Récupérez l'identifiant de l'employé
            char employeeId_fixed = employeeAssignment.getEmployeeId();

            // Parcourez les affectations de l'employé
            for (Solution.Assign assign : employeeAssignment.getAssignments()) {
                // Récupérez le jour et le shiftId
                int day_fixed = assign.getDay();
                char shiftId_fixed = assign.getShiftId();
            }
        }
        //pour chaque sous-problème correspondant à un jour
        for (NurseSchedulingProblem subProblem : subProblems) {
            //réinitialisation des variables de solution pour le sous-problème
            for (Solution.EmployeeAssignment employeeAssignment : bestSolution.getEmployeeAssignments()) {
                // Parcourez les affectations de l'employé
                for (Solution.Assign assign : employeeAssignment.getAssignments()) {
                    // Réinitialisez les variables à optimiser (shiftId)
                    assign.setShiftId(' ');
                }
            }
            //résolution du sous-problème
            Solution currentSolution = solve(subProblem);

            // Vérifiez si la solution actuelle est meilleure que la meilleure solution actuelle
            if (currentSolution.isFeasible() ) {
                //si le coût de la nouvelle solution est meilleur
                if (currentSolution.getCost() < bestSolution.getCost()) {
                    // Mettez à jour la meilleure solution
                    bestSolution = currentSolution;
                    //mise à jour des variables avec les nouvelles valeurs
                    for (Solution.EmployeeAssignment employeeAssignment : currentSolution.getEmployeeAssignments()) {
                        // Récupérez l'identifiant de l'employé
                        char employeeId_fixed = employeeAssignment.getEmployeeId();

                        // Parcourez les affectations de l'employé
                        for (Solution.Assign assign : employeeAssignment.getAssignments()) {
                            // Récupérez le jour et le shiftId
                            int day_fixed = assign.getDay();
                            char shiftId_fixed = assign.getShiftId();
                        }
                    }

                } else {
                    //Restore Set of x variables that are to be optimized in future iterations

                }
            }
        }
        //la meilleure solution est renvoyée
        return bestSolution;
    }

    public List<NurseSchedulingProblem> decompose(NurseSchedulingProblem globalProblem) {
        List<NurseSchedulingProblem> subProblems = new ArrayList<>();

        // Pour chaque jour dans dayIndexes
        for (int day : globalProblem.getDayIndexes()) {
            // Copier le problème global
            NurseSchedulingProblem subProblem = new NurseSchedulingProblem(
                    new HashSet<>(globalProblem.getShifts()),
                    new HashSet<>(globalProblem.getEmployees()),
                    globalProblem.getHorizon()
            );

            // Appliquer la restriction sur le jour particulier
            subProblem.setDayIndexes(new int[]{day});

            // Ajouter le sous-problème à la liste
            subProblems.add(subProblem);
        }

        return subProblems;
    }
    public static Solution solve(NurseSchedulingProblem problem) {
        // Initialiser la solution avec une méthode de construction de planning faisable
        Solution solution = buildFeasibleSolution(problem);

        // Vérifier les contraintes après la construction pour la faisabilité de la solution
        boolean isFeasible = verifyIsFeasible(problem, solution.getEmployeeAssignments());

        // Calculer le coût de la solution
        int cost = calculateSolutionCost(problem, solution.getEmployeeAssignments());

        // Créer une nouvelle instance de Nurse_Restoring.Solution avec les paramètres mis à jour
        solution = new Solution(solution.getEmployeeAssignments(), isFeasible, cost);


        return solution;
    }

    private static boolean verifyIsFeasible(NurseSchedulingProblem problem, List<Solution.EmployeeAssignment> solutionAssignments) {
        // Vérifier toutes les contraintes pour déterminer la faisabilité de la solution
        try {
            verify.checkAllConstraints(problem, solutionAssignments);
            return true;  // Aucune exception n'a été levée, la solution est faisable
        } catch (Exception e) {
            return false;  // Une exception a été levée, la solution n'est pas faisable
        }
    }

    private static int calculateSolutionCost(NurseSchedulingProblem problem, List<Solution.EmployeeAssignment> solutionAssignments) {
        //  logique pour calculer le coût de la solution
        return 0;  // Remplacer par votre logique réelle
    }

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
