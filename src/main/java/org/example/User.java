package org.example;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String fullname;
    private String password;
    private Role role;
    private List<Duty> duties;
    private int limitDuties;

    public User(String fullname, String password, Role role) {
        this.fullname = fullname;
        this.password = password;
        this.role = role;
        this.duties = new ArrayList<>();
        this.limitDuties = -1;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void addDuty(Duty duty) {
        duties.add(duty);
    }

    public int getDutyCount() {
        return duties.size();
    }

    public boolean receivDuty() {
        return limitDuties == -1 || getDutyCount() < limitDuties;
    }

    @Override
    public String toString() {
        StringBuilder write = new StringBuilder(fullname + " (" + role +  ") - Наряди: ");
        if (duties.isEmpty()) {
            write.append("немає");
        } else {
            for (Duty d : duties) {
                write.append("\n * ").append(d.getDescription())
                        .append(" - ").append(d.getDate());
            }
        }
        return write.toString();
    }

    public LocalDate lastDuty() {
        return duties.stream()
                .map(Duty::getDate)
                .filter(date -> date != null)
                .max(LocalDate::compareTo)
                .orElse(null);
    }
}