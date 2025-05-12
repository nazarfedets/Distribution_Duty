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
                {"Олег Петришин", "oleg2025", "0"},
                {"Петро Сидоренко", "petro2022", "-1"},
                {"Василь Потопа", "vasyl2024", "4"},
                {"Андрій Карпо", "carpo2022", "-1"},
                {"Тарас Лащук", "lashchuk2021", "3"}
        };

        for (String[] data : cadets) {
            User cadet = new User(data[0], data[1], Role.USER);
            cadet.setLimitduty(Integer.parseInt(data[2]));
            users.add(cadet);
        }

        
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
    public void autoDistribution(int year, int month, Map<String, Integer> dutiesMonth) {
        LocalDate startDate= LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<User> userOnly = users.stream()
                .filter(user -> user.getRole() == Role.USER)
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : dutiesMonth.entrySet()) {
            String description = entry.getKey();
            int totalDuties = entry.getValue();
            int assigned = 0;

            int maxPerDay = description.toLowerCase().contains("столова") ? 2 : 1;

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (assigned >= totalDuties) break;

                Collections.shuffle(userOnly);
                int dailyAssigned = 0;

                for (User user : userOnly) {
                    if (dailyAssigned >= maxPerDay || assigned >= totalDuties) break;

                    if (user.getDuty()) {
                        LocalDate last = user.lastDuty();
                        if (last == null || last.plusDays(3).isBefore(date)) {
                            user.addDuty(new Duty(description, 1, date));
                            dailyAssigned++;
                            assigned++;
                        }
                    }

                }
            }

        }
    }


    public void printQuantilyperMonth(int year, int month) {
        System.out.println("\n--- Статистика за місяць ---" + month + "/" + year + " ---");
        for (User user : users) {
            if (user.getRole() == Role.USER) {
                Map<String, Long> dutyCount = user.getDuties().stream()
                        .filter(duty -> duty.getDate() != null && duty.getDate().getYear() == year && duty.getDate().getMonthValue() == month)
                        .collect(Collectors.groupingBy(Duty::getDescription, Collectors.counting()));

                System.out.println(user.getFullname() + ":");
                if (dutyCount.isEmpty()) {
                    System.out.println("   (Немає нарядів)");
                } else {
                    dutyCount.forEach((desc, count) -> System.out.println("  *" + desc + ": " + count));
                }
            }
        }
    }




    public void manualDistribution(Scanner scanner) {
        System.out.println("Введіть рік: ");
        int year = scanner.nextInt();
        System.out.println("Введіть місяць: ");
        int month = scanner.nextInt();
        System.out.println("Введіть кількість нарядів на столову: ");
        int tableDutyCount = scanner.nextInt();
        System.out.println("Введіть кількість нарядів на курс: ");
        int courseDutyCount = scanner.nextInt();

        Map<String, Integer> dutiesMonth = Map.of(
                "Наряд на столову", tableDutyCount,
                "Наряд на курс", courseDutyCount
        );

        autoDistribution(year, month, dutiesMonth);

    }

}
