import java.util.Set;
/**
 * La classe Post représente un type de poste dans le problème de planification des infirmières.
 * Chaque type de poste a une durée, des types de postes incompatibles immédiatement après,
 * et le nombre de personnels requis pour ce type de poste le jour j.
 */

public class Post {
    private int id;
    private int duree;
    private Set<TypePoste> typesInterdits;
    private int personnelRequis;

    public Post(int id, int duree, Set<TypePoste> typesInterdits, int personnelRequis) {
        this.id = id;
        this.duree = duree;
        this.typesInterdits = typesInterdits;
        this.personnelRequis = personnelRequis;
    }
    public int getId() {
        return id;
    }
    public int getDuree() {
        return duree;
    }

    public Set<TypePoste> getTypesInterdits() {
        return typesInterdits;
    }

    public int getPersonnelRequis() {
        return personnelRequis;
    }
}

