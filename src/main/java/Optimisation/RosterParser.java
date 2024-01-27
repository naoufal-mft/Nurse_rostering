package Optimisation;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * La classe RosterParser permet de parser un fichier de planning (roster) au format XML
 * contenant les affectations des employés à différents jours et postes.
 * Elle extrait les données du fichier XML et les stocke dans une liste d'objets EmployeeAssignment.
 *
 * @AUTHOR SEKKOUMI
 */
public class RosterParser {

    public static List<EmployeeAssignment> parseRosterFile(String filePath) {
        List<EmployeeAssignment> employeeAssignments = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            NodeList employeeNodes = document.getElementsByTagName("Employee");

            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Node employeeNode = employeeNodes.item(i);

                if (employeeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element employeeElement = (Element) employeeNode;
                    String employeeID = employeeElement.getAttribute("ID");

                    EmployeeAssignment employeeAssignment = new EmployeeAssignment(employeeID);

                    NodeList assignNodes = employeeElement.getElementsByTagName("Assign");

                    for (int j = 0; j < assignNodes.getLength(); j++) {
                        Node assignNode = assignNodes.item(j);

                        if (assignNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element assignElement = (Element) assignNode;
                            int day = Integer.parseInt(assignElement.getElementsByTagName("Day").item(0).getTextContent());
                            String shift = assignElement.getElementsByTagName("Shift").item(0).getTextContent();

                            Assignment assignment = new Assignment(day, shift);
                            employeeAssignment.addAssignment(assignment);
                        }
                    }

                    employeeAssignments.add(employeeAssignment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeAssignments;
    }

    public int calculate(List<EmployeeAssignment> employeeAssignments,Set<Integer> allDays,Set<String> allShifts,int[][][] penaltyNotPreferred,int[][][] penaltyPreferred,boolean[][][] assignment,int[][] penaltyUnderstaffing,int[][] penaltyOverstaffing,int[][] understaffing,int[][] overstaffing) {
        int calcule=0;
        for(EmployeeAssignment employee : employeeAssignments){
            for(int day : allDays){
                for(String shift : allShifts){
                    calcule= penaltyNotPreferred[Integer.parseInt(employee.getEmployeeID())][day][Integer.parseInt(shift)]*(1-Integer.parseInt(String.valueOf(assignment[Integer.parseInt(employee.getEmployeeID())][day][Integer.parseInt(shift)])));
                    calcule= calcule+ penaltyPreferred[Integer.parseInt(employee.getEmployeeID())][day][Integer.parseInt(shift)] * Integer.parseInt(String.valueOf(assignment[Integer.parseInt(employee.getEmployeeID())][day][Integer.parseInt(shift)]));

                }
            }
        }
        for(int day : allDays){
            for(String shift : allShifts) {
                calcule = calcule + penaltyUnderstaffing[day][Integer.parseInt(shift)]*understaffing[day][Integer.parseInt(shift)] + penaltyOverstaffing[day][Integer.parseInt(shift)]*overstaffing[day][Integer.parseInt(shift)];
            }
            }
        return calcule;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\samir\\Downloads\\a.roster";
        List<EmployeeAssignment> employeeAssignments = parseRosterFile(filePath);
        Set<Integer> allDays = new HashSet<>();
        Set<String> allShifts = new HashSet<>();
        // Now you have the data in the 'employeeAssignments' list, and you can perform calculations.
        // Example: Print the data
        for (EmployeeAssignment employeeAssignment : employeeAssignments) {
            System.out.println("Employee ID: " + employeeAssignment.getEmployeeID());
            for (Assignment assignment : employeeAssignment.getAssignments()) {
                System.out.println("   Day: " + assignment.getDay() + ", Shift: " + assignment.getShift());
                allDays.add(assignment.getDay());
                allShifts.add(assignment.getShift());
            }
            System.out.println();
        }
    }
}
