package org.example;


import java.time.LocalDate;
import java.util.Map;

public interface ControlDuty {
    void listActiveDyte();
    void assignDuties(User user, Duty duty);
    void removeDutyFromUser(String fullName, LocalDate date);
    void autoDistribution(int year, int month, Map<String, Integer> dutiesMonth);
}

