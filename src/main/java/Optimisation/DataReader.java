package Optimisation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**  Fonction de lecture des données à partir d’un fichier texte associé à chaque instance
 *
 * @author ELMOUDEN
 *
 * */
class Post{
    public int day;
    public String shiftId;
    public int weight;
    public Post(int day,String shiftId,int weight){
        this.day=day;
        this.shiftId=shiftId;
        this.weight=weight;
    }
}

public class Shift {

    private String shiftID;
    private int lengthInMinutes;
    private List<String> nonFollowableShifts;

    public Shift(String shiftID, int lengthInMinutes, List<String> nonFollowableShifts) {
        this.shiftID = shiftID;
        this.lengthInMinutes = lengthInMinutes;
        this.nonFollowableShifts = nonFollowableShifts;
    }

    // Getters
    public String getShiftID() {
        return shiftID;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List<String> getNonFollowableShifts() {
        return nonFollowableShifts;
    }

    // Setters
    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    public void setNonFollowableShifts(List<String> nonFollowableShifts) {
        this.nonFollowableShifts = nonFollowableShifts;
    }
}


public class Staff {
    private String id;
    private Map<String, Integer> maxShifts; // map pour stocker les maxShifts par type de quart
    private int maxTotalMinutes;
    private int minTotalMinutes;
    private int maxConsecutiveShifts;
    private int minConsecutiveShifts;
    private int minConsecutiveDaysOff;
    private int maxWeekends;
    private ArrayList<Post> souhaitsAffectation;
    private ArrayList<Post> souhaitsEvitment;
    private List<Integer> dayOff;

    // Constructeur
    public Staff(String id, Map<String,Integer> maxShifts, int maxTotalMinutes, int minTotalMinutes,
                 int maxConsecutiveShifts, int minConsecutiveShifts, int minConsecutiveDaysOff, int maxWeekends) {
        this.id = id;
        this.maxShifts = maxShifts;
        this.maxTotalMinutes = maxTotalMinutes;
        this.minTotalMinutes = minTotalMinutes;
        this.maxConsecutiveShifts = maxConsecutiveShifts;
        this.minConsecutiveShifts = minConsecutiveShifts;
        this.minConsecutiveDaysOff = minConsecutiveDaysOff;
        this.maxWeekends = maxWeekends;
    }

    // Getters et Setters

    public List<Integer> getDayOff(){
        return dayOff;
    }
    public ArrayList<Post> getSouhaitsAffectation(){
        return souhaitsAffectation;
    }
    public ArrayList<Post> getSouhaitsEvitment(){
        return souhaitsEvitment;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String,Integer> getMaxShifts() {
        return maxShifts;
    }

    public void setMaxShifts(Map<String,Integer> maxShifts) {
        this.maxShifts = maxShifts;
    }

    public int getMaxTotalMinutes() {
        return maxTotalMinutes;
    }

    public void setMaxTotalMinutes(int maxTotalMinutes) {
        this.maxTotalMinutes = maxTotalMinutes;
    }

    public int getMinTotalMinutes() {
        return minTotalMinutes;
    }

    public void setMinTotalMinutes(int minTotalMinutes) {
        this.minTotalMinutes = minTotalMinutes;
    }
    public void setMaxConsecutiveShifts(int maxConsecutiveShifts) {
        this.maxConsecutiveShifts = maxConsecutiveShifts;
    }

    public void setMinConsecutiveShifts(int minConsecutiveShifts) {
        this.minConsecutiveShifts = minConsecutiveShifts;
    }

    public void setMinConsecutiveDaysOff(int minConsecutiveDaysOff) {
        this.minConsecutiveDaysOff = minConsecutiveDaysOff;
    }

    public void setMaxWeekends(int maxWeekends) {
        this.maxWeekends = maxWeekends;
    }
    public int getMaxConsecutiveShifts() {
        return maxConsecutiveShifts;
    }

    public int getMinConsecutiveShifts() {
        return minConsecutiveShifts;
    }

    public int getMinConsecutiveDaysOff() {
        return minConsecutiveDaysOff;
    }

    public int getMaxWeekends() {
        return maxWeekends;
    }
}


public class DayOff {
    private String employeeId;
    private List<Integer> daysOff;

    public DayOff(String employeeId) {
        this.employeeId = employeeId;
        this.daysOff = new ArrayList<>();;
    }

    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Integer> getDaysOff() {
        return daysOff;
    }
    public void addDayOff(int dayOff) {
        this.daysOff.add(dayOff);
    }
}

public class CoverRequirement {
    private int date;
    private String shiftId;
    private int requiredStaff;
    private int penaltyForUnderstaffing;
    private int penaltyForOverstaffing;

    public CoverRequirement(int date, String shiftId, int requiredStaff, int penaltyForUnderstaffing, int penaltyForOverstaffing) {
        this.date = date;
        this.shiftId = shiftId;
        this.requiredStaff = requiredStaff;
        this.penaltyForUnderstaffing = penaltyForUnderstaffing;
        this.penaltyForOverstaffing = penaltyForOverstaffing;
    }

    // Getters and setters
    // Getters
    public int getDate() {
        return date;
    }

    public String getShiftId() {
        return shiftId;
    }

    public int getRequiredStaff() {
        return requiredStaff;
    }

    public int getPenaltyForUnderstaffing() {
        return penaltyForUnderstaffing;
    }

    public int getPenaltyForOverstaffing() {
        return penaltyForOverstaffing;
    }

    // Setters
    public void setDate(int date) {
        this.date = date;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public void setRequiredStaff(int requiredStaff) {
        this.requiredStaff = requiredStaff;
    }

    public void setPenaltyForUnderstaffing(int penaltyForUnderstaffing) {
        this.penaltyForUnderstaffing = penaltyForUnderstaffing;
    }

    public void setPenaltyForOverstaffing(int penaltyForOverstaffing) {
        this.penaltyForOverstaffing = penaltyForOverstaffing;
    }
}

public class ShiftOnRequest {
    private String employeeId;
    private int day;
    private String shiftId;
    private int weight;

    // Constructeurs
    public ShiftOnRequest(String employeeId, int day, String shiftId, int weight) {
        this.employeeId = employeeId;
        this.day = day;
        this.shiftId = shiftId;
        this.weight = weight;
    }
    //getters, setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

public class ShiftOffRequest {
    private String employeeId;
    private int day;
    private String shiftId;
    private int weight;

    // Constructeurs
    public ShiftOffRequest(String employeeId, int day, String shiftId, int weight) {
        this.employeeId = employeeId;
        this.day = day;
        this.shiftId = shiftId;
        this.weight = weight;

    }



    //getters, setters
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}





}

