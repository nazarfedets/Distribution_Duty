package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Register_Users implements UserManagerInterface, ControlDuty {
    private List<User> users = new ArrayList<>();
    private List<Duty> availableDuties = new ArrayList<>();

    Register_Users() {
        users.add(new User("Admin", "12345", Role.ADMIN));

        String[][] cadets = {
                {"Іван Іванов", "ivan2023", "2"},
                {"Олег Петришин", "oleg2025", "1"},
                {"Петро Сидоренко", "petro2022", "-1"},
                {"Василь Потопа", "vasyl2024", "4"},
                {"Андрій Карпо", "carpo2022", "-1"},
                {"Тарас Лащук", "lashchuk2021", "3"}
        };

        for (String[] data : cadets) {
            User cadet = new User(data[0], data[1], Role.USER);
            cadet.setLimitDuties(Integer.parseInt(data[2]));
            users.add(cadet);
        }

        availableDuties.add(new Duty("Наряд на столову", 5));
        availableDuties.add(new Duty("Наряд на курс", 7));
    }

    @Override
    public User loginUser(String fullName, String password) {
        Optional<User> user = users.stream()
                .filter(u -> u.getFullname().equalsIgnoreCase(fullName) && u.checkPassword(password))
                .findFirst();

        if (user.isPresent()) {
            System.out.println("Вхід успішний! Вітаємо, " + user.get().getFullname() + " (" + user.get().getRole() + ")");
            return user.get();
        } else {
            return null;
        }
    }

    @Override
    public void listUsers() {
        System.out.println("\n--- Список курсантів ---");
        users.stream()
                .filter(u -> u.getRole() == Role.USER)
                .forEach(System.out::println);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void listActiveDyte() {
        System.out.println("\n--- Наряди курсантів ---");
        users.stream()
                .filter(u -> u.getRole() == Role.USER)
                .forEach(user -> {
                    System.out.println(user.getFullname() + ":");
                    user.getDuties().forEach(d -> System.out.println("   * " + d.getDescription() + " — " + d.getDate()));
                });
    }

    @Override
    public void removeDutyFromUser(String fullName, LocalDate date) {
        users.stream()
                .filter(u -> u.getFullname().equalsIgnoreCase(fullName))
                .findFirst()
                .ifPresent(user -> {
                    boolean removed = user.getDuties().removeIf(duty -> date.equals(duty.getDate()));
                    if (removed) {
                        System.out.println("Наряд на " + date + " було видалено для " + fullName);
                    } else {
                        System.out.println("Наряд не знайдено на вказану дату.");
                    }
                });
    }

    @Override
    public void assignDuties(User user, Duty duty) {
        user.addDuty(duty);
        System.out.println("Наряд \"" + duty.getDescription() + "\" призначено на " + duty.getDate() + " для " + user.getFullname());
    }

    @Override
    public void autoDistribution() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<User> userOnly = users.stream()
                .filter(user -> user.getRole() == Role.USER)
                .collect(Collectors.toList());

        for (Duty duty : availableDuties) {
            int totalDuties = duty.getQuantity();
            int assigned = 0;

            int maxPerDay;
            if (duty.getDescription().toLowerCase().contains("столов")) {
                maxPerDay = 2;
            } else {
                maxPerDay = 1;
            }

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (assigned >= totalDuties) break;

                Collections.shuffle(userOnly);
                int dailyAssigned = 0;

                for (User user : userOnly) {
                    if (dailyAssigned >= maxPerDay || assigned >= totalDuties) break;

                    if (user.receivDuty()) {
                        LocalDate last = user.lastDuty();
                        if (last == null || last.plusDays(3).isBefore(date)) {
                            user.addDuty(new Duty(duty.getDescription(), 1, date));
                            dailyAssigned++;
                            assigned++;
                        }
                    }

                }
            }

            duty.setQuantity(duty.getQuantity() - assigned);
        }
    }



}
