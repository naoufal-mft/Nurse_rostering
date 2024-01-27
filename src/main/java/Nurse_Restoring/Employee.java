package Nurse_Restoring;

import java.util.Set;
/**
 * La classe Nurse_Restoring.Employee représente un employé dans le problème de planification des infirmières.
 * Chaque instance de cette classe contient les caractéristiques spécifiques de l'employé telles que
 * les jours de repos, le temps de travail minimum et maximum, le nombre de postes consécutifs,
 * le nombre de jours de repos consécutifs, le nombre maximal de week-ends travaillés, etc.
 *
 * @author: MEFTAHI
 */
public class Employee {
    private char employeeID;
    private int[] joursDeReposIndexes; //l’ensemble des jours où l’employé e ne travaille pas
    private int MinTotalMinutes;//le temps total minimum de travail à faire par e
    private int MaxTotalMinutes;//le temps total maximum de travail à faire par e
    private int MinConsecutiveShifts;//le nombre minimum de postes de travail consécutifs que l’employé e doit travailler
    private int MaxConsecutiveShifts;// le nombre maximum de postes de travail consécutifs que l’employé e doit travailler
    private int MinConsecutiveDaysOff;//le nombre minimum de jour de repos consécutifs à affecter à l’employé e
    private int MaxWeekends; //le nombre maximum de week-ends que peut travailler l’employé e
    private int MaxShifts;// le nombre maximum de jours où e travaille sur le poste de type p (p ∈ P).
    private Set<Shift> souhaitsAffectation;//souhait d’être affecté à un type de poste particulier un jour de l’horizon de planification
    private Set<Shift> souhaitsEvitement;//souhait de ne pas être affecté à un type de poste particulier un jour de l’horizon de planification

    public Employee(char employeeID,
                    int[] joursDeReposIndexes,
                    int tempsTotalMinimum,
                    int MaxTotalMinutes,
                    int MinConsecutiveShifts,
                    int MaxConsecutiveShifts,
                    int MinConsecutiveDaysOff,
                    int MaxWeekends,
                    int MaxShifts,
                    Set<Shift> souhaitsAffectation,
                    Set<Shift> souhaitsEvitement) {
        this.employeeID = employeeID;
        this.joursDeReposIndexes = joursDeReposIndexes;
        this.MinTotalMinutes = tempsTotalMinimum;
        this.MaxTotalMinutes = MaxTotalMinutes;
        this.MinConsecutiveShifts = MinConsecutiveShifts;
        this.MaxConsecutiveShifts = MaxConsecutiveShifts;
        this.MinConsecutiveDaysOff = MinConsecutiveDaysOff;
        this.MaxWeekends = MaxWeekends;
        this.MaxShifts = MaxShifts;
        this.souhaitsAffectation = souhaitsAffectation;
        this.souhaitsEvitement = souhaitsEvitement;
    }
    public int getEmployeeID() {
        return employeeID;
    }

    public int[] getJoursDeReposIndexes() {
        return joursDeReposIndexes;
    }

    public int getMinTotalMinutes() {
        return MinTotalMinutes;
    }

    public int getMaxTotalMinutes() {
        return MaxTotalMinutes;
    }

    public int getMinConsecutiveShifts() {
        return MinConsecutiveShifts;
    }

    public int getMaxConsecutiveShifts() {
        return MaxConsecutiveShifts;
    }

    public int getMinConsecutiveDaysOff() {
        return MinConsecutiveDaysOff;
    }

    public int getMaxWeekends() {
        return MaxWeekends;
    }

    public int getMaxShifts() {
        return MaxShifts;
    }

    public Set<Shift> getSouhaitsAffectation() {
        return souhaitsAffectation;
    }

    public Set<Shift> getSouhaitsEvitement() {
        return souhaitsEvitement;
    }
}
