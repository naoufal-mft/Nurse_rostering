package Optimisation;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe EmployeeAssignment représente les affectations d'un employé dans le cadre du problème de planification des infirmières.
 * Elle contient l'identifiant de l'employé et une liste d'attributions d'équipe.
 *
 * @author SEKKOUMI
 */
public class EmployeeAssignment {
    private String employeeID;
    private List<Assignment> assignments;

    public EmployeeAssignment(String employeeID) {
        this.employeeID = employeeID;
        this.assignments = new ArrayList<>();
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    // Getters and other methods as needed

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
