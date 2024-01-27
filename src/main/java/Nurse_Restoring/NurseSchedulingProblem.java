package Nurse_Restoring;

import java.util.HashSet;
import java.util.Set;
/**
 * Classe représentant le problème de planification des infirmières.
 * Cette classe définit les variables de décision, les contraintes et la fonction objectif
 * pour résoudre le problème de manière optimale.
 *
 * @author MEFTAHI
 */
public class NurseSchedulingProblem{
    private int horizon; //le nombre de jours de l’horizon de planification
    private Set<Shift> shifts;//l’ensemble des types de postes
    private Set<Employee> employees; //l’ensemble des employés
    private Set<Integer> Weekends; // l’ensemble des week-ends concernés par la planification
    private int[] DayIndexes;//l’ensemble des jours concernés par la planification (J = {1, . . . , h})


    // Variables de décision
    private boolean[][][] assignment;  // xejp, égale à true si l'employé est affecté au type de poste p le jour j, false sinon
    private boolean[][] workingWeekends;  // tew, égale à true si l'employé travaille le week-end w, false sinon
    private int[][] understaffing;  // yjp, égale au manque de personnel affecté au type de poste p le jour j (un entier)
    private int[][] overstaffing;  // yjp, égale à l'excédent de personnel affecté au type de poste p le jour j (un entier)

    // Pénalités
    private int[][][] penaltyNotPreferred;  // qejp, la pénalité si l'employé n'est pas affecté au type de poste p le jour j alors qu'il le souhaitait
    private int[][][] penaltyPreferred;  // pejp, la pénalité si l'employé est affecté au type de poste p le jour j alors qu'il ne le souhaitait pas
    private int[][] penaltyUnderstaffing;  // vminjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est inférieur à ujp
    private int[][] penaltyOverstaffing;  // vmaxjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est supérieur à ujp



    public int getHorizon() {
        return horizon;
    }

    public Set<Shift> getShifts() {
        return shifts;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Set<Integer> getWeekends() {
        return Weekends;
    }

    public int[] getDayIndexes() {
        return DayIndexes;
    }
    public void setDayIndexes(int[] dayIndexes) {
        this.DayIndexes = dayIndexes;
    }

    public boolean[][][] getAssignment() {
        return assignment;
    }

    public boolean[][] getWorkingWeekends() {
        return workingWeekends;
    }

    public int[][] getUnderstaffing() {
        return understaffing;
    }

    public int[][] getOverstaffing() {
        return overstaffing;
    }

    public int[][][] getPenaltyNotPreferred() {
        return penaltyNotPreferred;
    }

    public int[][][] getPenaltyPreferred() {
        return penaltyPreferred;
    }

    public int[][] getPenaltyUnderstaffing() {
        return penaltyUnderstaffing;
    }

    public int[][] getPenaltyOverstaffing() {
        return penaltyOverstaffing;
    }


    public NurseSchedulingProblem(Set<Shift> shifts, Set<Employee> employees, int horizon) {
        this.horizon = horizon;
        this.shifts = shifts;
        this.employees = employees;
        this.DayIndexes = new int[horizon];
        for (int i = 0; i < horizon; i++) {
            DayIndexes[i] = i;
        }
        this.Weekends = new HashSet<>();
        for (int w = 1; w <= 7; w++) {
            Weekends.add(w);
        }

    }

    public void initializeModel() {
        // Initialisation des variables de décision
        assignment = new boolean[employees.size()][horizon][shifts.size()];
        workingWeekends = new boolean[employees.size()][Weekends.size()];
        understaffing = new int[horizon][shifts.size()];
        overstaffing = new int[horizon][shifts.size()];

        // Initialisation des pénalités
        penaltyNotPreferred = new int[employees.size()][horizon][shifts.size()];
        penaltyPreferred = new int[employees.size()][horizon][shifts.size()];
        penaltyUnderstaffing = new int[horizon][shifts.size()];
        penaltyOverstaffing = new int[horizon][shifts.size()];

    }

    public void contrainte1() {
        for (Employee employee : employees) {
            for (int j = 1; j < DayIndexes.length; j++) {
                for (int p = 0; p < shifts.size(); p++) {
                    // Contrainte d'affectation à la variable de décision assignment
                    if (assignment[employee.getEmployeeID()][j][p]) {
                        System.out.println("L'employé " + employee.getEmployeeID() + " est affecté au type de poste " + p + " le jour " + j);
                    } else {
                        System.out.println("L'employé " + employee.getEmployeeID() + " n'est pas affecté au type de poste " + p + " le jour " + j);
                    }
                }
                // La somme des assignations pour chaque employé et chaque jour doit être inférieure ou égale à 1
                int sum = 0;
                for (int p = 0; p < shifts.size(); p++) {
                    if (assignment[employee.getEmployeeID()][j][p]) {
                        sum++;
                    }
                }
                if (sum > 1) {
                    System.out.println("La contrainte d'une seule affectation n'est pas respectée pour l'employé " + employee.getEmployeeID() + " le jour " + j);
                }
            }
        }
    }

    public void contrainte2() {
        for (Employee employee : employees) {
            for (int j = 1; j < horizon - 1; j++) {
                for (Shift shift : shifts) {
                    for (Shift incompatibleType : shift.getTypesInterdits()) {
                        // Contrainte d'incompatibilité
                        if (assignment[employee.getEmployeeID()][j][shift.getId()] || assignment[employee.getEmployeeID()][j + 1][incompatibleType.getId()]) {
                            //si l'incompatibilité est violée
                            System.out.println("Incompatibilité entre les types de postes " + shift.getId() +
                                    " et " + incompatibleType.getId() +
                                    " pour l'employé " + employee.getEmployeeID() +
                                    " les jours " + j + " et " + (j + 1));
                        }
                    }
                }
            }
        }
    }

    public void contrainte3() {
        for (Employee employee : employees) {
            for (Shift shift : shifts) {
                int p = shift.getId();  // Supposant que p est l'indice ordinal du type de poste
                int countDaysWorked = 0;
                for (int j = 0; j < horizon; j++) {
                    if (assignment[employee.getEmployeeID()][j][p]) {
                        countDaysWorked++;
                    }
                }
                if (countDaysWorked > employee.getMaxShifts()) {
                    // La contrainte est violée, vous pouvez prendre une action appropriée
                    System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                            " a travaillé plus de " + employee.getMaxShifts() +
                            " jours sur le poste de type " + shift.getId());
                }
            }
        }
    }
    public void contrainte4() {
        for (Employee employee : employees) {
            int tmin = employee.getMinTotalMinutes();
            int tmax = employee.getMaxTotalMinutes();
            int totalWorkTime = 0;
            for (int j = 0; j < horizon; j++) {
                for (Shift shift : shifts) {
                    int p = shift.getId();
                    int dp = shift.getLength();  // Supposant que getDuree() retourne la durée du type de poste en minutes
                    // Vérifie si l'employé est affecté à ce type de poste ce jour-là
                    if (assignment[employee.getEmployeeID()][j][p]) {
                        totalWorkTime += dp;
                    }
                }
            }
            // Applique la contrainte
            if (totalWorkTime < tmin || totalWorkTime > tmax) {
                // Contrainte violée, prenez une action appropriée
                System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                        " a un temps de travail total de " + totalWorkTime +
                        " minutes en dehors de la plage [" + tmin + ", " + tmax + "]");
            }

        }
    }

    public void contrainte5() {
        for (Employee employee : employees) {
            int cmaxe = employee.getMaxConsecutiveShifts();

            for (int d = 0; d < horizon - cmaxe + 1; d++) {
                int consecutiveWorkDays = 0;

                for (int j = d; j < d + cmaxe; j++) {
                    // Vérifie si l'employé travaille ce jour
                    if (isEmployeeWorking(employee, j)) {
                        consecutiveWorkDays++;
                    }
                }
                // Applique la contrainte
                if (consecutiveWorkDays > cmaxe) {
                    // Contrainte violée, prenez une action appropriée
                    System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                            " travaille plus de " + cmaxe + " jours consécutifs à partir du jour " + (d + 1));
                }
            }
        }
    }
    // Méthode pour vérifier si un employé travaille un jour donné
    private boolean isEmployeeWorking(Employee employee, int day) {
        // Parcourir tous les types de postes (shifts)
        for (Shift shift : shifts) {
            // Récupérer l'identifiant du poste
            int p = shift.getId();
            // Vérifier si l'employé est affecté à ce poste le jour donné
            if (assignment[employee.getEmployeeID()][day][p]) {
                // L'employé travaille, retourner true
                return true;
            }
        }
        // Aucun poste n'a été trouvé pour cet employé ce jour-là
        return false;
    }


    public void contrainte6() {
        for (Employee employee : employees) {
            int cmin = employee.getMinConsecutiveDaysOff();
            for (int s = 1; s <= cmin - 1; s++) {
                for (int d = 0; d <= horizon - (s + 1); d++) {
                    int sumBeforeShift = 0;
                    int sumAfterShift = 0;
                    for (Shift shift : shifts) {
                        int p = shift.getId();
                        // Somme des postes avant le repos
                        for (int j = d + 1; j <= d + s; j++) {
                            sumBeforeShift += assignment[employee.getEmployeeID()][j][p] ? 1 : 0;
                        }
                        // Somme des postes après le repos
                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sumAfterShift += assignment[employee.getEmployeeID()][j][p] ? 1 : 0;
                        }
                    }
                    // Applique la contrainte
                    if (sumBeforeShift + (s - sumAfterShift) + sumBeforeShift > 0) {
                        // Contrainte violée, prenez une action appropriée
                        System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                                " ne respecte pas la contrainte de repos entre les shifts pour s = " + s +
                                " à partir du jour " + (d + 1));
                    }
                }
            }
        }
    }
    public void contrainte7() {
        for (Employee employee : employees) {
            int rmin = employee.getMinConsecutiveDaysOff();
            for (int s = 1; s <= rmin - 1; s++) {
                for (int d = 0; d <= horizon - (s + 1); d++) {
                    int sumWorkBeforeRest = 0;
                    int sumRest = 0;
                    int sumWorkAfterRest = 0;
                    for (Shift shift : shifts) {
                        int p = shift.getId();
                        // Somme des postes avant le repos
                        for (int j = d + 1; j <= d + s; j++) {
                            sumWorkBeforeRest += assignment[employee.getEmployeeID()][j][p] ? 1 : 0;
                        }
                        // Somme des postes pendant le repos
                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sumRest += assignment[employee.getEmployeeID()][j][p] ? 1 : 0;

                            // Somme des postes après le repos
                            for (int i = d + s + 2; i <= d + s + rmin; i++) {
                                sumWorkAfterRest += assignment[employee.getEmployeeID()][i][p] ? 1 : 0;
                            }
                        }
                        // Applique la contrainte
                        if ((1 - sumWorkBeforeRest) + sumRest + (1 - sumWorkAfterRest) > 0) {
                            // Contrainte violée, prenez une action appropriée
                            System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                                    " ne respecte pas la contrainte de repos minimal pour s = " + s +
                                    " à partir du jour " + (d + 1));
                        }
                    }
                }
            }
        }
    }
    public void contrainte8() {
        for (Employee employee : employees) {
            int wmaxe = employee.getMaxWeekends();
            int totalWeekendsWorked = 0;

            for (int w = 1; w <= Weekends.size(); w++) {
                int weekendWorked = 0;
                for (int dayIndex : DayIndexes) {
                    for (Shift shift : shifts) {
                        int p = shift.getId();
                        // Vérifie si l'employé travaille au moins un des deux jours du week-end
                        if (assignment[employee.getEmployeeID()][(7 * w - 1) + dayIndex][p] ||
                                assignment[employee.getEmployeeID()][(7 * w) + dayIndex][p]) {
                            weekendWorked++;
                        }
                    }
                }
                // Incrémente le nombre total de week-ends travaillés
                totalWeekendsWorked += weekendWorked;

                // Applique la contrainte
                if (weekendWorked > 0 && weekendWorked <= 2) {
                    // Contrainte satisfaite
                } else {
                    // Contrainte violée, prenez une action appropriée
                    System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                            " a travaillé " + weekendWorked + " jours pendant le week-end " + w);
                }
            }
            // Applique la contrainte
            if (totalWeekendsWorked <= wmaxe) {
                // Contrainte satisfaite
            } else {
                // Contrainte violée, prenez une action appropriée
                System.out.println("Contrainte violée : L'employé " + employee.getEmployeeID() +
                        " a travaillé " + totalWeekendsWorked + " week-ends, dépassant la limite de " + wmaxe);
            }
        }
    }

    public void contrainte9() {
        for (Employee employee : employees) {
            int[] joursDeReposIndexes = employee.getJoursDeReposIndexes();

            for (int dayIndex : joursDeReposIndexes) {
                for (Shift shift : shifts) {
                    int p = shift.getId();
                    // Contrainte : l'employé ne travaille pas certains jours
                    assignment[employee.getEmployeeID()][dayIndex][p] = false;
                }
            }
        }
    }



    public void contrainte10() {
        for (int j = 0; j < DayIndexes.length; j++) {
            for (Shift shift : shifts) {
                int p = shift.getId();
                int personnelRequis = shift.getPersonnelRequis();  // Utilisez la méthode de la classe Nurse_Restoring.Shift pour obtenir le nombre de personnel requis

                int totalEmployesAffectes = 0;

                for (Employee employee : employees) {
                    if (assignment[employee.getEmployeeID()][j][p]) {
                        totalEmployesAffectes++;
                    }
                }

                int manquePersonnel = understaffing[j][p];
                int excedentPersonnel = overstaffing[j][p];

                // Contrainte : somme(xejp) - yjp + yjp = ujp
                if (totalEmployesAffectes - manquePersonnel + excedentPersonnel != personnelRequis) {
                    // La contrainte est violée, prenez une action appropriée
                    System.out.println("Contrainte violée pour le jour " + j + ", le type de poste " + shift + ": " +
                            "Affectations=" + totalEmployesAffectes + ", Manque=" + manquePersonnel +
                            ", Excédent=" + excedentPersonnel + ", Personnel requis=" + personnelRequis);
                }
            }
        }
    }
    public int ObjectiveFunction() {
        int objectiveValue = 0;

        // Partie1
        for (Employee employee : employees) {
            for (int day : DayIndexes) {
                for (Shift shift : shifts) {
                    int p = shift.getId();
                    int qejp = penaltyNotPreferred[employee.getEmployeeID()][day][p];
                    boolean xejp = assignment[employee.getEmployeeID()][day][p];

                    objectiveValue += qejp * (1 - (xejp ? 1 : 0));
                }
            }
        }

        // Partie2
        for (Employee employee : employees) {
            for (int day : DayIndexes) {
                for (Shift shift : shifts) {
                    int p = shift.getId();
                    int pejpxejp = penaltyPreferred[employee.getEmployeeID()][day][p];
                    boolean xejp = assignment[employee.getEmployeeID()][day][p];

                    objectiveValue += pejpxejp * (xejp ? 1 : 0);
                }
            }
        }

        // Partie3
        for (int day : DayIndexes) {
            for (Shift shift : shifts) {
                int p = shift.getId();
                int v_min_jp = penaltyUnderstaffing[day][p];
                int y_jp = understaffing[day][p];

                objectiveValue += v_min_jp * y_jp;
            }
        }

        // Partie4
        for (int day : DayIndexes) {
            for (Shift shift : shifts) {
                int p = shift.getId();
                int v_max_jp = penaltyOverstaffing[day][p];
                int y_jp = overstaffing[day][p];

                objectiveValue += v_max_jp * y_jp;
            }
        }

        return objectiveValue;
    }
}





