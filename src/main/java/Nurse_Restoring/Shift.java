package Nurse_Restoring;

import java.util.Set;
/**
 * La classe Post représente un type de poste dans le problème de planification des infirmières.
 * Chaque type de poste a une durée, des types de postes incompatibles immédiatement après,
 * et le nombre de personnels requis pour ce type de poste le jour j(dayIndexe).
 *  @Auteur: MEFTAHI
 */

public class Shift {
    private char id;
    private int Length; //La durée (en minutes)
    private Set<Shift> typesInterdits;
    private int personnelRequis;

    public Shift(char id, int Length, Set<Shift> typesInterdits, int personnelRequis) {
        this.id = id;
        this.Length = Length;
        this.typesInterdits = typesInterdits;
        this.personnelRequis = personnelRequis;
    }
    public int getId() {
        return id;
    }
    public int getLength() {
        return Length;
    }

    public Set<Shift> getTypesInterdits() {
        return typesInterdits;
    }

    public int getPersonnelRequis() {
        return personnelRequis;
    }
}

