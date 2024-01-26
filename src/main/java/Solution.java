import java.util.List;

public class Solution {
    private List<EmployeeAssignment> employeeAssignments;
    private boolean isFeasible;
    private int cost;

    public Solution(List<EmployeeAssignment> employeeAssignments, boolean isFeasible, int cost) {
        this.employeeAssignments = employeeAssignments;
        this.isFeasible = isFeasible;
        this.cost = cost;
    }
    public Solution(List<EmployeeAssignment> employeeAssignments) {
        this.employeeAssignments = employeeAssignments;
        this.isFeasible = false; // Valeur par défaut pour isFeasible
        this.cost = 0; // Valeur par défaut pour cost
    }

    public List<EmployeeAssignment> getEmployeeAssignments() {
        return employeeAssignments;
    }
    public boolean isFeasible() {
        return isFeasible;
    }

    public int getCost() {
        return cost;
    }

    public static class EmployeeAssignment {
        private char employeeId;
        private List<Assign> assignments;

        public EmployeeAssignment(char employeeId, List<Assign> assignments) {
            this.employeeId = employeeId;
            this.assignments = assignments;
        }

        public char getEmployeeId() {
            return employeeId;
        }

        public List<Assign> getAssignments() {
            return assignments;
        }
    }


    public static class Assign {
        private int day;
        private char shiftId;


        public Assign(int day, char shiftId ) {
            this.day = day;
            this.shiftId = shiftId;

        }

        public int getDay() {
            return day;
        }

        public char getShiftId() {
            return shiftId;
        }

        public void setShiftId(char shiftId) {
            this.shiftId = shiftId;
        }

    }
}
