package Optimisation;

import java.util.ArrayList;
import java.util.List;
/**
 * La classe SchedulerData représente les données nécessaires pour la planification des horaires.
 * Elle contient des listes de différents éléments tels que les postes de travail, le personnel, les jours de repos,
 * les exigences de couverture, les demandes de présence et les demandes d'absence.
 *
 * @author ELMOUDEN
 */
public class SchedulerData {
    private List<Shift> shifts;
    private List<Staff> staffList;
    private List<DayOff> dayOffs;
    private List<CoverRequirement> coverRequirements;
    private List<ShiftOnRequest> shiftOnRequestsList;
    private List<ShiftOffRequest> shiftOffRequestsList;

    // Constructeur
    public SchedulerData() {
        this.shifts = new ArrayList<>();
        this.staffList = new ArrayList<>();
        this.dayOffs = new ArrayList<>();
        this.coverRequirements = new ArrayList<>();
        this.shiftOnRequestsList = new ArrayList<>();
        this.shiftOffRequestsList = new ArrayList<>();
    }

    // Getters et Setters pour Shifts
    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    // Getters et Setters pour StaffList
    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    // Getters et Setters pour DayOffs
    public List<DayOff> getDayOffs() {
        return dayOffs;
    }

    public void setDayOffs(List<DayOff> dayOffs) {
        this.dayOffs = dayOffs;
    }

    // Getters et Setters pour CoverRequirements
    public List<CoverRequirement> getCoverRequirements() {
        return coverRequirements;
    }

    public void setCoverRequirements(List<CoverRequirement> coverRequirements) {
        this.coverRequirements = coverRequirements;
    }

    // Getters et Setters pour ShiftOnRequestsList
    public List<ShiftOnRequest> getShiftOnRequestsList() {
        return shiftOnRequestsList;
    }

    public void setShiftOnRequestsList(List<ShiftOnRequest> shiftOnRequestsList) {
        this.shiftOnRequestsList = shiftOnRequestsList;
    }

    // Getters et Setters pour ShiftOffRequestsList
    public List<ShiftOffRequest> getShiftOffRequestsList() {
        return shiftOffRequestsList;
    }

    public void setShiftOffRequestsList(List<ShiftOffRequest> shiftOffRequestsList) {
        this.shiftOffRequestsList = shiftOffRequestsList;
    }
}
