package org.example;


import java.time.LocalDate;

public interface ControlDuty {
    void listActiveDyte();
    void assignDuties(User user, Duty duty);
    void autoDistribution();
    void removeDutyFromUser(String fullName, LocalDate date);

}
