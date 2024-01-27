package Optimisation;

import Nurse_Restoring.Employee;
import Nurse_Restoring.NurseSchedulingProblem;
import Nurse_Restoring.Shift;
import Nurse_Restoring.Solution;

import java.util.List;

/**
 * La classe verify fournit des méthodes pour vérifier les contraintes spécifiques
 * d'un problème de planification des infirmières. Ces contraintes portent sur des
 * aspects tels que les affectations d'employés, les limites de temps de travail,
 * les jours de repos, etc.
 *
 * Chaque méthode de vérification cible une contrainte spécifique et affiche un message
 * d'erreur approprié si la contrainte est violée.
 *
 * @author TANANI
 */
public class verify {

    // Méthode principale pour vérifier toutes les contraintes
    public static void verifierToutesLesContraintes(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        verifierContrainte1(nurseProblem, solution);
        verifierContrainte2(nurseProblem, solution);
        verifierContrainte3(nurseProblem, solution);
        verifierContrainte4(nurseProblem, solution);
        verifierContrainte5(nurseProblem, solution);
        verifierContrainte6(nurseProblem, solution);
        verifierContrainte7(nurseProblem, solution);
        verifierContrainte8(nurseProblem, solution);
        verifierContrainte9(nurseProblem, solution);
        verifierContrainte10(nurseProblem, solution);
    }

    // Contrainte 1: Un employé ne peut être affecté qu'à un poste par jour
    private static void verifierContrainte1(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            for (int j = 0; j < nurseProblem.getNbDays(); j++) {
                int somme = 0;
                for (int p = 0; p < nurseProblem.getPosts().size(); p++) {
                    if (solution[employee.getId()][j][p]) {
                        somme++;
                    }
                }
                if (somme > 1) {
                    System.out.println("Contrainte 1 violée : L'employé " + employee.getId() +
                            " est affecté à plus d'un poste le jour " + j);
                }
            }
        }
    }

    // Contrainte 2: Incompatibilité entre les postes pour un employé sur deux jours consécutifs
    private static void verifierContrainte2(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            for (int j = 0; j < nurseProblem.getNbDays() - 1; j++) {
                for (Post post : nurseProblem.getPosts()) {
                    int p = post.getId();
                    for (Post typeIncompatible : post.getTypesIncompatibles()) {
                        if (solution[employee.getId()][j][p]
                                || solution[employee.getId()][j + 1][typeIncompatible.getId()]) {
                            System.out.println("Contrainte 2 violée : Incompatibilité entre les postes " + p +
                                    " et " + typeIncompatible.getId() +
                                    " pour l'employé " + employee.getId() +
                                    " les jours " + j + " et " + (j + 1));
                        }
                    }
                }
            }
        }
    }

    // Contrainte 3: Limite du nombre de jours de travail pour chaque employé sur chaque poste
    private static void verifierContrainte3(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            for (Post post : nurseProblem.getPosts()) {
                int p = post.getId();
                int joursTravailles = 0;
                for (int j = 0; j < nurseProblem.getNbDays(); j++) {
                    if (solution[employee.getId()][j][p]) {
                        joursTravailles++;
                    }
                }
                if (joursTravailles > employee.getMaxJoursTravaillesPourPoste(post)) {
                    System.out.println("Contrainte 3 violée : L'employé " + employee.getId() +
                            " a travaillé plus de jours autorisés sur le poste " + post.getId());
                }
            }
        }
    }

    // Contrainte 4: Limite du temps total de travail pour chaque employé
    private static void verifierContrainte4(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int tmin = employee.getMinTempsTotalTravail();
            int tmax = employee.getMaxTempsTotalTravail();
            int tempsTotalTravail = 0;

            for (int j = 0; j < nurseProblem.getNbDays(); j++) {
                for (Post post : nurseProblem.getPosts()) {
                    int p = post.getId();
                    int dp = post.getDuree();
                    if (solution[employee.getId()][j][p]) {
                        tempsTotalTravail += dp;
                    }
                }
            }

            if (tempsTotalTravail < tmin || tempsTotalTravail > tmax) {
                System.out.println("Contrainte 4 violée : L'employé " + employee.getId() +
                        " a un temps total de travail en dehors de la plage [" + tmin + ", " + tmax + "]");
            }
        }
    }

    // Contrainte 5: Limite du nombre de jours de travail consécutifs
    private static void verifierContrainte5(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int cmaxe = employee.getMaxJoursTravaillesConsecutifs();
            for (int d = 0; d < nurseProblem.getNbDays() - cmaxe + 1; d++) {
                int joursTravaillesConsecutifs = 0;
                for (int j = d; j < d + cmaxe; j++) {
                    if (isEmployeAuTravail(solution, employee, j)) {
                        joursTravaillesConsecutifs++;
                    }
                }
                if (joursTravaillesConsecutifs > cmaxe) {
                    System.out.println("Contrainte 5 violée : L'employé " + employee.getId() +
                            " travaille plus de " + cmaxe + " jours consécutifs à partir du jour " + (d + 1));
                }
            }
        }
    }

    // Contrainte 6: Respect des jours de repos consécutifs
    private static void verifierContrainte6(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int cmin = employee.getMinJoursReposConsecutifs();
            for (int s = 1; s <= cmin - 1; s++) {
                for (int d = 0; d <= nurseProblem.getNbDays() - (s + 1); d++) {
                    int sommeAvantShift = 0;
                    int sommeApresShift = 0;

                    for (Post post : nurseProblem.getPosts()) {
                        int p = post.getId();
                        for (int j = d + 1; j <= d + s; j++) {
                            sommeAvantShift += solution[employee.getId()][j][p] ? 1 : 0;
                        }

                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sommeApresShift += solution[employee.getId()][j][p] ? 1 : 0;
                        }
                    }

                    if (sommeAvantShift + (s - sommeApresShift) + sommeAvantShift > 0) {
                        System.out.println("Contrainte 6 violée : L'employé " + employee.getId() +
                                " ne respecte pas les jours de repos consécutifs pour s = " + s +
                                " à partir du jour " + (d + 1));
                    }
                }
            }
        }
    }

    // Contrainte 7: Respect des jours de repos après des jours de travail
    private static void verifierContrainte7(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int rmin = employee.getMinJoursRepos();
            for (int s = 1; s <= rmin - 1; s++) {
                for (int d = 0; d <= nurseProblem.getNbDays() - (s + 1); d++) {
                    int sommeTravailAvantRepos = 0;
                    int sommeRepos = 0;
                    int sommeTravailApresRepos = 0;

                    for (Post post : nurseProblem.getPosts()) {
                        int p = post.getId();
                        for (int j = d + 1; j <= d + s; j++) {
                            sommeTravailAvantRepos += solution[employee.getId()][j][p] ? 1 : 0;
                        }

                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sommeRepos += solution[employee.getId()][j][p] ? 1 : 0;

                            for (int i = d + s + 2; i <= d + s + rmin; i++) {
                                sommeTravailApresRepos += solution[employee.getId()][i][p] ? 1 : 0;
                            }
                        }

                        if ((1 - sommeTravailAvantRepos) + sommeRepos + (1 - sommeTravailApresRepos) > 0) {
                            System.out.println("Contrainte 7 violée : L'employé " + employee.getId() +
                                    " ne respecte pas les jours de repos minimaux pour s = " + s +
                                    " à partir du jour " + (d + 1));
                        }
                    }
                }
            }
        }
    }

// Contrainte 8: Limite du nombre de weekends travaillés
    private static void verifierContrainte8(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int wmaxe = employee.getMaxWeekendsTravailles();
            int totalWeekendsTravailles = 0;
    
            for (int w = 1; w <= nurseProblem.getWeekends().size(); w++) {
                int weekendsTravailles = 0;
    
                for (int dayIndex : nurseProblem.getDayIndexes()) {
                    for (Shift shift : nurseProblem.getShifts()) {
                        int p = shift.getId();
                        // Vérifie si l'employé travaille au moins un des deux jours du week-end
                        if (solution[employee.getId()][(7 * w - 1) + dayIndex][p] ||
                                solution[employee.getId()][(7 * w) + dayIndex][p]) {
                            weekendsTravailles++;
                        }
                    }
                }
    
                // Incrémente le nombre total de week-ends travaillés
                totalWeekendsTravailles += weekendsTravailles;
    
                // Applique la contrainte
                if (weekendsTravailles > 0 && weekendsTravailles <= 2) {
                    // Contrainte satisfaite
                } else {
                    // Contrainte violée, prenez une action appropriée
                    System.out.println("Contrainte 8 violée : L'employé " + employee.getId() +
                            " a travaillé " + weekendsTravailles + " jours pendant le week-end " + w);
                }
            }
    
            // Applique la contrainte
            if (totalWeekendsTravailles <= wmaxe) {
                // Contrainte satisfaite
            } else {
                // Contrainte violée, prenez une action appropriée
                System.out.println("Contrainte 8 violée : L'employé " + employee.getId() +
                        " a travaillé " + totalWeekendsTravailles + " week-ends, dépassant la limite de " + wmaxe);
            }
        }
    }


    // Contrainte 9: Jours de repos spécifiés pour chaque employé
    private static void verifierContrainte9(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (Employee employee : nurseProblem.getEmployees()) {
            int[] joursDeReposIndexes = employee.getJoursDeReposIndexes();
    
            for (int dayIndex : joursDeReposIndexes) {
                for (Post post : nurseProblem.getPosts()) {
                    int p = post.getId();
                    // Contrainte : L'employé ne travaille pas les jours spécifiés
                    if (solution[employee.getId()][dayIndex][p]) {
                        System.out.println("Contrainte 9 violée : L'employé " + employee.getId() +
                                " est affecté à travailler le jour spécifié pour le poste " + post.getId());
                    }
                }
            }
        }
    }


    // Contrainte 10: Nombre d'employés requis pour chaque poste sur chaque jour
    private static void verifierContrainte10(NurseSchedulingProblem nurseProblem, List<EmployeeAssignment> solution) {
        for (int j = 0; j < nurseProblem.getNbDays(); j++) {
            for (Post post : nurseProblem.getPosts()) {
                int p = post.getId();
                int personnelRequis = post.getPersonnelRequis();
                int totalEmployesAffectes = 0;
    
                for (Employee employee : nurseProblem.getEmployees()) {
                    if (solution[employee.getId()][j][p]) {
                        totalEmployesAffectes++;
                    }
                }
    
                int sousEffectif = nurseProblem.getSousEffectif()[j][p];
                int surEffectif = nurseProblem.getSurEffectif()[j][p];
    
                // Vérifier si la contrainte est violée
                if (totalEmployesAffectes < sousEffectif || totalEmployesAffectes > personnelRequis + surEffectif) {
                    System.out.println("Contrainte 10 violée : Jour " + j + ", Poste " + post.getId() +
                            ": Affectations=" + totalEmployesAffectes + ", Sous-effectif=" + sousEffectif +
                            ", Sur-effectif=" + surEffectif + ", Personnel Requis=" + personnelRequis);
                }
            }
        }
    }

}

