package Optimisation;

/**
 * La classe Assignment représente une affectation d'un employé à un quart de travail
 * pour un jour spécifique.
 *
 * @AUTHOR SEKKOUMI
 */
public class Assignment {
    private int day;
    private String shift;

    public Assignment(int day, String shift) {
        this.day = day;
        this.shift = shift;
    }

    public int getDay() {
        return day;
    }

    public String getShift() {
        return shift;
    }

    // Other methods as needed
}
