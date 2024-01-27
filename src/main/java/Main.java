import Optimisation.*;

import java.io.IOException;
public class Main {

    public static void main(String[] args) {
        try {
            SchedulerData data = DataReader.readSchedulerData("Instances/Instance3.txt");

            // Affichage des Shifts
            System.out.println("Shifts:");
            for (Shift shift : data.getShifts()) {
                System.out.println("Shift ID: " + shift.getShiftID() + ", Length: " + shift.getLengthInMinutes());
            }

            // Affichage du Staff
            System.out.println("\nStaff:");
            for (Staff staff : data.getStaffList()) {
                System.out.println("Staff ID: " + staff.getId() + ", Max Shifts: " + staff.getMaxShifts());
            }

            // Affichage des Day Offs
            System.out.println("\nDay Offs:");
            for (DayOff dayOff : data.getDayOffs()) {
                System.out.println("Employee ID: " + dayOff.getEmployeeId() + ", Days Off: " + dayOff.getDaysOff());
            }

            System.out.println("\nCover Requirements:");
            for (CoverRequirement cover : data.getCoverRequirements()) {
                System.out.println("Date: " + cover.getDate() + ", Shift ID: " + cover.getShiftId() + ", Required Staff: " + cover.getRequiredStaff());
            }

            System.out.println("\nShift On Requests:");
            for (ShiftOnRequest shiftOn : data.getShiftOnRequestsList()) {
                System.out.println("Employee ID: " + shiftOn.getEmployeeId() + ", Day: " + shiftOn.getDay() + ", Shift ID: " + shiftOn.getShiftId() + ", Weight: " + shiftOn.getWeight());
            }

            System.out.println("\nShift Off Requests:");
            for (ShiftOffRequest shiftOff : data.getShiftOffRequestsList()) {
                System.out.println("Employee ID: " + shiftOff.getEmployeeId() + ", Day: " + shiftOff.getDay() + ", Shift ID: " + shiftOff.getShiftId() + ", Weight: " + shiftOff.getWeight());
            }

            // Ici, vous pouvez utiliser les données comme vous le souhaitez
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }
}
