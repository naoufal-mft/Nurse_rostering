import java.util.Arrays;
import java.util.Set;

public class NurseSchedulingProblem{
    private Set<Post> posts;
    private Set<Employee> employees;
    private int nbDays;
    private int nbWeekends;

    // Variables de décision
    private boolean[][][] assignment;  // xejp, égale à true si l'employé est affecté au type de poste p le jour j, false sinon
    private boolean[][] workingWeekends;  // tew, égale à true si l'employé travaille le week-end w, false sinon
    private int[][] understaffing;  // yjp, égale au manque de personnel affecté au type de poste p le jour j (un entier)
    private int[][] overstaffing;  // yjp, égale à l'excédent de personnel affecté au type de poste p le jour j (un entier)

    // Pénalités
    private int[][] penaltyNotPreferred;  // qejp, la pénalité si l'employé n'est pas affecté au type de poste p le jour j alors qu'il le souhaitait
    private int[][] penaltyPreferred;  // pejp, la pénalité si l'employé est affecté au type de poste p le jour j alors qu'il ne le souhaitait pas
    private int[][] penaltyUnderstaffing;  // vminjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est inférieur à ujp
    private int[][] penaltyOverstaffing;  // vmaxjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est supérieur à ujp



    public NurseSchedulingProblem(Set<Post> typesPostes, Set<Employee> employees, int nbDays, int nbWeekends) {
        this.posts = typesPostes;
        this.employees = employees;
        this.nbDays = nbDays;
        this.nbWeekends = nbWeekends;

    }

    public void initializeModel() {
        // Initialisation des variables de décision
        assignment = new boolean[employees.size()][nbDays][posts.size()];
        workingWeekends = new boolean[employees.size()][nbWeekends];
        understaffing = new int[nbDays][posts.size()];
        overstaffing = new int[nbDays][posts.size()];

        // Initialisation des pénalités
        penaltyNotPreferred = new int[employees.size()][nbDays];
        penaltyPreferred = new int[employees.size()][nbDays];
        penaltyUnderstaffing = new int[nbDays][posts.size()];
        penaltyOverstaffing = new int[nbDays][posts.size()];

    }

    public void contrainte1() {
        for (Employee employee : employees) {
            for (int j = 0; j < nbDays; j++) {
                for (int p = 0; p < posts.size(); p++) {
                    // Contrainte d'affectation à la variable de décision assignment
                    if (assignment[employee.getId()][j][p]) {
                        System.out.println("L'employé " + employee.getId() + " est affecté au type de poste " + p + " le jour " + j);
                    } else {
                        System.out.println("L'employé " + employee.getId() + " n'est pas affecté au type de poste " + p + " le jour " + j);
                    }
                }
                // La somme des assignations pour chaque employé et chaque jour doit être inférieure ou égale à 1
                int sum = 0;
                for (int p = 0; p < posts.size(); p++) {
                    if (assignment[employee.getId()][j][p]) {
                        sum++;
                    }
                }
                if (sum > 1) {
                    System.out.println("La contrainte d'une seule affectation n'est pas respectée pour l'employé " + employee.getId() + " le jour " + j);
                }
            }
        }
    }

    public void contrainte2() {
        for (Employee employee : employees) {
            for (int j = 0; j < nbDays - 1; j++) {
                for (Post post : posts) {
                    for (TypePoste incompatibleType : post.getTypesInterdits()) {
                        // Contrainte d'incompatibilité
                        if (assignment[employee.getId()][j][post.getId()] || assignment[employee.getId()][j + 1][incompatibleType.ordinal()]) {
                            //si l'incompatibilité est violée
                            System.out.println("Incompatibilité entre les types de postes " + post.getId() +
                                    " et " + incompatibleType.getLibelle() +
                                    " pour l'employé " + employee.getId() +
                                    " les jours " + j + " et " + (j + 1));
                        }
                    }
                }
            }
        }
    }

    public void contrainte3() {
        for (Employee employee : employees) {
            for (Post post : posts) {
                int p = post.getId();  // Supposant que p est l'indice ordinal du type de poste
                int countDaysWorked = 0;
                for (int j = 0; j < nbDays; j++) {
                    if (assignment[employee.getId()][j][p]) {
                        countDaysWorked++;
                    }
                }
                if (countDaysWorked > employee.getNombreMaxJoursTravaillesSurPoste()) {
                    // La contrainte est violée, vous pouvez prendre une action appropriée
                    System.out.println("Contrainte violée : L'employé " + employee.getId() +
                            " a travaillé plus de " + employee.getNombreMaxJoursTravaillesSurPoste() +
                            " jours sur le poste de type " + post.getId());
                }
            }
        }
    }
    public void contrainte4() {
        for (Employee employee : employees) {
            int tmin = employee.getTempsTotalMinimum();
            int tmax = employee.getTempsTotalMaximum();
            int totalWorkTime = 0;
            for (int j = 0; j < nbDays; j++) {
                for (Post post : posts) {
                    int p = post.getId();  // Supposant que p est l'indice ordinal du type de poste
                    int dp = post.getDuree();  // Supposant que getDuree() retourne la durée du type de poste en minutes
                    // Vérifie si l'employé est affecté à ce type de poste ce jour-là
                    if (assignment[employee.getId()][j][p]) {
                        totalWorkTime += dp;
                    }
                }
            }
            // Applique la contrainte
            if (totalWorkTime < tmin || totalWorkTime > tmax) {
                // Contrainte violée, prenez une action appropriée
                System.out.println("Contrainte violée : L'employé " + employee.getId() +
                        " a un temps de travail total de " + totalWorkTime +
                        " minutes en dehors de la plage [" + tmin + ", " + tmax + "]");
            }

        }
    }

    public void contrainte5() {
        for (Employee employee : employees) {
            int cmaxe = employee.getNombreMaxPostesConsecutifs();

            for (int d = 0; d < nbDays - cmaxe + 1; d++) {
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
                    System.out.println("Contrainte violée : L'employé " + employee.getId() +
                            " travaille plus de " + cmaxe + " jours consécutifs à partir du jour " + (d + 1));
                }
            }
        }
    }
    private boolean isEmployeeWorking(Employee employee, int day) {
        for (Post post : posts) {
            int p = post.getId();
            if (assignment[employee.getId()][day][p]) {
                return true;
            }
        }
        return false;
    }

    public void contrainte6() {
        for (Employee employee : employees) {
            int cmin = employee.getNombreMinJoursReposConsecutifs();
            for (int s = 1; s <= cmin - 1; s++) {
                for (int d = 0; d <= nbDays - (s + 1); d++) {
                    int sumBeforeShift = 0;
                    int sumAfterShift = 0;
                    for (Post post : posts) {
                        int p = post.getId();  // Supposant que p est l'indice ordinal du type de poste
                        // Somme des postes avant le repos
                        for (int j = d + 1; j <= d + s; j++) {
                            sumBeforeShift += assignment[employee.getId()][j][p] ? 1 : 0;
                        }
                        // Somme des postes après le repos
                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sumAfterShift += assignment[employee.getId()][j][p] ? 1 : 0;
                        }
                    }
                    // Applique la contrainte
                    if (sumBeforeShift + (s - sumAfterShift) + sumBeforeShift > 0) {
                        // Contrainte violée, prenez une action appropriée
                        System.out.println("Contrainte violée : L'employé " + employee.getId() +
                                " ne respecte pas la contrainte de repos entre les shifts pour s = " + s +
                                " à partir du jour " + (d + 1));
                    }
                }
            }
        }
    }
    public void contrainte7() {
        for (Employee employee : employees) {
            int rmin = employee.getNombreMinJoursReposConsecutifs();
            for (int s = 1; s <= rmin - 1; s++) {
                for (int d = 0; d <= nbDays - (s + 1); d++) {
                    int sumWorkBeforeRest = 0;
                    int sumRest = 0;
                    int sumWorkAfterRest = 0;
                    for (Post post : posts) {
                        int p = post.getId();
                        // Somme des postes avant le repos
                        for (int j = d + 1; j <= d + s; j++) {
                            sumWorkBeforeRest += assignment[employee.getId()][j][p] ? 1 : 0;
                        }
                        // Somme des postes pendant le repos
                        for (int j = d + s + 1; j <= d + s + 1; j++) {
                            sumRest += assignment[employee.getId()][j][p] ? 1 : 0;

                            // Somme des postes après le repos
                            for (int i = d + s + 2; i <= d + s + rmin; i++) {
                                sumWorkAfterRest += assignment[employee.getId()][i][p] ? 1 : 0;
                            }
                        }
                        // Applique la contrainte
                        if ((1 - sumWorkBeforeRest) + sumRest + (1 - sumWorkAfterRest) > 0) {
                            // Contrainte violée, prenez une action appropriée
                            System.out.println("Contrainte violée : L'employé " + employee.getId() +
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
                int wmaxe = employee.getNombreMaxWeekendsTravailles();
                int totalWeekendsWorked = 0;

                for (int w = 1; w <= nbWeekends; w++) {
                    int weekendWorked = 0;
                    for (Jour jour : Jour.values()) {
                        int dayIndex = Arrays.asList(Jour.values()).indexOf(jour);
                        for (Post post : posts) {
                            int p = post.getId();
                            // Vérifie si l'employé travaille au moins un des deux jours du week-end
                            if (assignment[employee.getId()][(7 * w - 1) + dayIndex][p] ||
                                    assignment[employee.getId()][(7 * w) + dayIndex][p]) {
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
                        System.out.println("Contrainte violée : L'employé " + employee.getId() +
                                " a travaillé " + weekendWorked + " jours pendant le week-end " + w);
                    }
                }
                // Applique la contrainte
                if (totalWeekendsWorked <= wmaxe) {
                    // Contrainte satisfaite
                } else {
                    // Contrainte violée, prenez une action appropriée
                    System.out.println("Contrainte violée : L'employé " + employee.getId() +
                            " a travaillé " + totalWeekendsWorked + " week-ends, dépassant la limite de " + wmaxe);
                }
            }
    }
    public void contrainte9() {
        for (Employee employee : employees) {
            Set<Jour> joursDeRepos = employee.getJoursDeRepos();
            for (Jour jour : joursDeRepos) {
                int dayIndex = Arrays.asList(Jour.values()).indexOf(jour);
                for (Post post : posts) {
                    int p = post.getId();  // Supposant que p est l'indice ordinal du type de poste
                    // Contrainte : l'employé ne travaille pas certains jours
                    assignment[employee.getId()][dayIndex][p] = false;
                }
            }
        }
    }
    public void contrainte10() {
        for (int j = 0; j < nbDays; j++) {
            for (Post post : posts) {
                int p = post.getId();
                int personnelRequis = post.getPersonnelRequis();  // Utilisez la méthode de la classe Post pour obtenir le nombre de personnel requis

                int totalEmployesAffectes = 0;

                for (Employee employee : employees) {
                    if (assignment[employee.getId()][j][p]) {
                        totalEmployesAffectes++;
                    }
                }

                int manquePersonnel = understaffing[j][p];
                int excedentPersonnel = overstaffing[j][p];

                // Contrainte : somme(xejp) - yjp + yjp = ujp
                if (totalEmployesAffectes - manquePersonnel + excedentPersonnel != personnelRequis) {
                    // La contrainte est violée, prenez une action appropriée
                    System.out.println("Contrainte violée pour le jour " + j + ", le type de poste " + post + ": " +
                            "Affectations=" + totalEmployesAffectes + ", Manque=" + manquePersonnel +
                            ", Excédent=" + excedentPersonnel + ", Personnel requis=" + personnelRequis);
                }
            }
        }
    }
}





