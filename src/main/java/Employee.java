import java.util.Set;

public class Employee {
    private int id;

    private Set<Jour> joursDeRepos; // Re, l'ensemble des jours où l'employé ne travaille pas
    private int tempsTotalMinimum; // tmin, le temps total minimum de travail à faire
    private int tempsTotalMaximum; // tmax, le temps total maximum de travail à faire
    private int nombreMinPostesConsecutifs; // cmin, le nombre minimum de postes de travail consécutifs
    private int nombreMaxPostesConsecutifs; // cmax, le nombre maximum de postes de travail consécutifs
    private int nombreMinJoursReposConsecutifs; // rmin, le nombre minimum de jours de repos consécutifs
    private int nombreMaxWeekendsTravailles; // wmax, le nombre maximum de week-ends travaillés
    private int nombreMaxJoursTravaillesSurPoste; // mmax, le nombre maximum de jours où l'employé travaille sur le poste de type p (p ∈ P)

    private Set<TypePoste> souhaitsAffectation; // Ensemble des types de postes préférés
    private Set<TypePoste> souhaitsEvitement; // Ensemble des types de postes à éviter

    public Employee(int id,
            Set<Jour> joursDeRepos,
            int tempsTotalMinimum,
            int tempsTotalMaximum,
            int nombreMinPostesConsecutifs,
            int nombreMaxPostesConsecutifs,
            int nombreMinJoursReposConsecutifs,
            int nombreMaxWeekendsTravailles,
            int nombreMaxJoursTravaillesSurPoste,
            Set<TypePoste> souhaitsAffectation,
            Set<TypePoste> souhaitsEvitement) {
        this.id = id;
        this.joursDeRepos = joursDeRepos;
        this.tempsTotalMinimum = tempsTotalMinimum;
        this.tempsTotalMaximum = tempsTotalMaximum;
        this.nombreMinPostesConsecutifs = nombreMinPostesConsecutifs;
        this.nombreMaxPostesConsecutifs = nombreMaxPostesConsecutifs;
        this.nombreMinJoursReposConsecutifs = nombreMinJoursReposConsecutifs;
        this.nombreMaxWeekendsTravailles = nombreMaxWeekendsTravailles;
        this.nombreMaxJoursTravaillesSurPoste = nombreMaxJoursTravaillesSurPoste;
        this.souhaitsAffectation = souhaitsAffectation;
        this.souhaitsEvitement = souhaitsEvitement;
    }
    public int getId() {
        return id;
    }

    public Set<Jour> getJoursDeRepos() {
        return joursDeRepos;
    }

    public int getTempsTotalMinimum() {
        return tempsTotalMinimum;
    }

    public int getTempsTotalMaximum() {
        return tempsTotalMaximum;
    }

    public int getNombreMinPostesConsecutifs() {
        return nombreMinPostesConsecutifs;
    }

    public int getNombreMaxPostesConsecutifs() {
        return nombreMaxPostesConsecutifs;
    }

    public int getNombreMinJoursReposConsecutifs() {
        return nombreMinJoursReposConsecutifs;
    }

    public int getNombreMaxWeekendsTravailles() {
        return nombreMaxWeekendsTravailles;
    }

    public int getNombreMaxJoursTravaillesSurPoste() {
        return nombreMaxJoursTravaillesSurPoste;
    }

    public Set<TypePoste> getSouhaitsAffectation() {
        return souhaitsAffectation;
    }

    public Set<TypePoste> getSouhaitsEvitement() {
        return souhaitsEvitement;
    }
}
