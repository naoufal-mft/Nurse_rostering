import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class NurseSchedulingProblem{
    private Set<Post> posts;
    private Set<Employee> employees;
    private int nbDays;
    private int nbWeekends;

    // Variables de décision
    private boolean[][][] assignment;  // xejp, égale à true si l'employé e est affecté au type de poste p le jour j, false sinon
    private boolean[][] workingWeekends;  // tew, égale à true si l'employé e travaille le week-end w, false sinon
    private int[][] understaffing;  // yjp, égale au manque de personnel affecté au type de poste p le jour j (un entier)
    private int[][] overstaffing;  // yjp, égale à l'excédent de personnel affecté au type de poste p le jour j (un entier)

    // Pénalités
    private int[][] penaltyNotPreferred;  // qejp, la pénalité si l'employé e n'est pas affecté au type de poste p le jour j alors qu'il le souhaitait
    private int[][] penaltyPreferred;  // pejp, la pénalité si l'employé e est affecté au type de poste p le jour j alors qu'il ne le souhaitait pas
    private int[][] penaltyUnderstaffing;  // vminjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est inférieur à ujp
    private int[][] penaltyOverstaffing;  // vmaxjp, la pénalité si le nombre de personnels affectés au type de poste p le jour j est supérieur à ujp

    // Modèle Choco
    private Model model;

    public NurseSchedulingProblem(Set<Post> typesPostes, Set<Employee> employees, int nbDays, int nbWeekends) {
        this.posts = typesPostes;
        this.employees = employees;
        this.nbDays = nbDays;
        this.nbWeekends = nbWeekends;
        this.model = new Model();
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
            for (int day = 0; day < nbDays; day++) {
                for (Post post : posts) {
                    // xejp représente l'affectation de l'employé e au type de poste p le jour j
                    BoolVar x = model.boolVar("affectation_" + employee + "_" + day + "_" + post);

                    // Ajouter la contrainte xejp <= 1
                    model.arithm(x, "<=", 1).post();
                }
            }
        }
    }

    public void contrainte2() {
        for (Employee employee : employees) {
            for (int day = 0; day < nbDays - 1; day++) {
                for (Post post : posts) {
                    for (TypePoste poste : post.getTypesInterdits()) {
                        // xejp représente l'affectation de l'employé e au type de poste p le jour j
                        // xe(j+1)p' représente l'affectation de l'employé e au type de poste p' le jour j+1
                        BoolVar xejp = model.boolVar("affectation_" + employee + "_" + day + "_" + post);
                        BoolVar xe_j1_p_prime = model.boolVar("affectation_" + employee + "_" + (day + 1) + "_" + poste);

                        // Ajouter la contrainte
                        model.ifThen(
                                model.arithm(xejp, "=", 1),
                                model.arithm(xe_j1_p_prime, "=", 0)
                        );
                    }
                }
            }
        }
    }

    public void contrainte3() {
        for (Employee employee : employees) {
            for (Post post : posts) {
                // xejp représente l'affectation de l'employé e au type de poste p le jour j
                BoolVar[] affectations = new BoolVar[nbDays];
                for (int day = 0; day < nbDays; day++) {
                    affectations[day] = model.boolVar("affectation_" + employee + "_" + day + "_" + post);
                }

                // Ajouter la contrainte de somme
                model.sum(affectations, "<=", post.getDuree()).post();
            }
        }
    }
    public void contrainte4() {
        for (Employee employee : employees) {
            // xejp représente l'affectation de l'employé e au type de poste p le jour j
            for (int day = 0; day < nbDays; day++) {
                for (Post post : posts) {
                    BoolVar xejp = model.boolVar("affectation_" + employee + "_" + day + "_" + post);
                    IntVar tempsTravail = model.intVar("tempsTravail_" + employee + "_" + day + "_" + post, 0, 1);

                    // Ajouter la contrainte de durée d'affectation
                    model.arithm(xejp, "*", tempsTravail, "=", post.getDuree()).post();
                }
            }

            // Temps total minimum de travail pour l'employé e
            model.sum(
                    Arrays.stream(new Set[]{posts})
                            .flatMap(post -> Arrays.stream(dernierAffectation(employee, (Post) post,0,0)))
                            .toArray(BoolVar[]::new),
                    ">=",
                    employee.getTempsTotalMinimum()
            ).post();

            // Temps total maximum de travail pour l'employé e
            model.sum(
                    Arrays.stream(new Set[]{posts})
                            .flatMap(post -> Arrays.stream(dernierAffectation(employee, (Post) post, 0,0)))
                            .toArray(BoolVar[]::new),
                    "<=",
                    employee.getTempsTotalMaximum()
            ).post();
        }
    }
    /**
     * Crée et retourne un tableau de variables booléennes représentant les affectations de l'employé
     * à un type de poste spécifique sur chaque jour.
     *
     * @param employee L'employé concerné.
     * @param post     Le type de poste concerné.
     * @return Un tableau de variables booléennes pour chaque jour représentant les affectations.
     */
    private BoolVar[] dernierAffectation(Employee employee, Post post, int day, int consecutiveDays) {
        if (consecutiveDays == 0) {
            // Utilisez la première méthode si consecutiveDays est égal à 0
            return IntStream.range(0, nbDays)
                    .mapToObj(j -> model.boolVar("affectation_" + employee + "_" + j + "_" + post))
                    .toArray(BoolVar[]::new);
        } else {
            // Utilisez la deuxième méthode si consecutiveDays est différent de 0
            return IntStream.rangeClosed(day, day + consecutiveDays - 1)
                    .mapToObj(j -> model.boolVar("affectation_" + employee + "_" + j + "_" + post))
                    .toArray(BoolVar[]::new);
        }
    }




    public void contrainte5() {
        for (Employee employee : employees) {
            for (int day = 1; day <= nbDays - employee.getNombreMaxPostesConsecutifs(); day++) {
                for (Post post : posts) {
                    BoolVar[] consecutiveAffectations = dernierAffectation(employee, post, day, employee.getNombreMaxPostesConsecutifs());

                    // Contrainte de somme avec puissance maximale
                    model.scalar(consecutiveAffectations, Arrays.stream(consecutiveAffectations).mapToInt(BoolVar::getValue).toArray(), "<=", (int) Math.pow(employee.getNombreMaxPostesConsecutifs(), employee.getNombreMaxPostesConsecutifs())).post();
                }
            }
        }
    }
    public void contrainte6() {
        for (Employee employee : employees) {
            for (int s = 1; s < Math.min(employee.getNombreMinPostesConsecutifs(), employee.getNombreMaxPostesConsecutifs()); s++) {
                for (int day = 1; day <= nbDays - (s + 1); day++) {
                    for (Post post : posts) {
                        BoolVar[] x_idp = dernierAffectation(employee, post, day, 1);

                        IntVar sumPart1 = model.intVar("sumPart1", 0, 1);
                        model.sum(Arrays.toString(x_idp)).eq(sumPart1).post();

                        IntVar sumPart2 = model.intVar("sumPart2", 0, s);
                        BoolVar[] x_ijp = Arrays.copyOfRange(x_idp, day + 1, day + s + 1);
                        model.sum(Arrays.toString(x_ijp)).min(s).eq(sumPart2).post();

                        BoolVar[] x_i_dsp1_p = dernierAffectation(employee, post, day + s + 1, 1);
                        IntVar sumPart3 = model.intVar("sumPart3", 0, 1);
                        model.sum(Arrays.toString(x_i_dsp1_p)).eq(sumPart3).post();

                        model.sum(Arrays.toString(new IntVar[]{sumPart1, sumPart2, sumPart3})).gt(0).post();
                    }
                }
            }
        }
    }
    public void contrainte7() {
        for (Employee employee : employees) {
            for (Post post : posts) {
                for (int s = 1; s < Math.min(employee.getNombreMinJoursReposConsecutifs(), employee.getNombreMaxJoursTravaillesSurPoste()); s++) {
                    for (int day = 1; day <= nbDays - (s + 1); day++) {
                        BoolVar[] x_idp = dernierAffectation(employee, post, day, 1);

                        IntVar part1 = model.intVar("part1", 0, 1);
                        model.arithm(model.sum(Arrays.toString(x_idp)), "!=", 1).post();

                        IntVar part2 = model.intVar("part2", 0, posts.size());
                        for (int j = day + 1; j <= day + s; j++) {
                            BoolVar[] x_jejp = dernierAffectation(employee, post, j, 1);
                            model.sum(Arrays.toString(x_jejp)).eq(1).post();  // Supposant que chaque jour a exactement un poste affecté
                            part2 = (IntVar) part2.add(model.sum(Arrays.toString(x_jejp)));
                        }

                        BoolVar[] x_i_dsp1_p = dernierAffectation(employee, post, day + s + 1, 1);
                        IntVar part3 = model.intVar("part3", 0, 1);
                        model.arithm(model.sum(Arrays.toString(x_i_dsp1_p)), "!=", 1).post();

                        model.sum(String.valueOf(part1), part2, part3).gt(0).post();
                    }
                }
            }
        }
    }
    public void contrainte8() {
        for (Employee employee : employees) {
            for (int w = 1; w <= employee.getNombreMaxWeekendsTravailles(); w++) {
                // Partie 1 de la contrainte
                IntVar part1 = model.intVar("part1", 0, 2);
                IntVar part2 = model.intVar("part2", 0, 2);

                for (Post post : posts) {
                    IntVar[] weekendDays = dernierAffectation(employee, post, Jour.SAMEDI.ordinal(), 2);
                    part1 = part1.add(weekendDays[0].eq(1).or(weekendDays[1].eq(1)).not()).intVar();
                }

                for (Post post : posts) {
                    IntVar[] weekendDays = dernierAffectation(employee, post, Jour.DIMANCHE.ordinal(), 2);
                    part2 = part2.add(weekendDays[0].eq(1).or(weekendDays[1].eq(1)).not()).intVar();
                }

                model.arithm((IntVar) part1.add(part2), ">", 0).post();

                // Partie 2 de la contrainte
                IntVar sumWeekendDays = model.intVar("sumWeekendDays", 0, 2);
                for (Post post : posts) {
                    IntVar[] weekendDays = dernierAffectation(employee, post, Jour.SAMEDI.ordinal(), 2);
                    sumWeekendDays = sumWeekendDays.add(weekendDays[0].add(weekendDays[1])).intVar();
                }

                model.arithm(sumWeekendDays, "<=", employee.getNombreMaxWeekendsTravailles()).post();
            }
        }
    }
    public void contrainte9() {
        for (Employee employee : employees) {
            for (Jour jourDeRepos : employee.getJoursDeRepos()) {
                for (Post post : posts) {
                    BoolVar[] affectations = dernierAffectation(employee, post, jourDeRepos.ordinal(), 1);
                    model.arithm(affectations[0], "=", 0).post();
                }
            }
        }
    }
    public void contrainte10() {
        for (Post post : posts) {
            for (int day = 1; day <= nbDays; day++) {
                // Liste pour stocker les affectations des employés pour le poste et le jour donnés
                List<BoolVar> affectations = new ArrayList<>();

                for (Employee employee : employees) {
                    BoolVar x = model.boolVar("affectation_" + employee + "_" + day + "_" + post);
                    BoolVar yManque = model.boolVar("manque_" + employee + "_" + day + "_" + post);
                    BoolVar yExcedent = model.boolVar("excedent_" + employee + "_" + day + "_" + post);

                    // Ajouter la variable d'affectation à la liste
                    affectations.add(x);

                    // Contrainte pour yManque et yExcedent
                    model.ifThenElse(
                            model.arithm(x, "=", 1),
                            model.arithm(yManque, "=", 0),
                            model.arithm(yExcedent, "=", 0)
                    );
                }

                // Contrainte d'équilibre des affectations, excédents et manques
                model.sum(affectations.toArray(new BoolVar[0]), "=", post.getPersonnelRequis()).post();
            }
        }
    }



    // Ajoutez d'autres méthodes pour accéder aux variables de décision, aux paramètres et aux pénalités du modèle
}
