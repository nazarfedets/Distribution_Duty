package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Register_Users registerUsers = new Register_Users();

        User user = null;


        while (user == null) {
            System.out.println("Введіть ПІБ: ");
            String name = scanner.nextLine();
            System.out.println("Введіть пароль: ");
            String password = scanner.nextLine();
            user = registerUsers.loginUser(name, password);

            if (user == null) {
                System.out.println("Неправильне ім'я або пароль. Спробуйте ще раз.");
            }
        }


        if (user.getRole() == Role.ADMIN) {
            int choice;
            do {
                System.out.println("\n--- Адмін меню ---");
                System.out.println("1.Переглянути користувачів");
                System.out.println("2.Переглядати активні наряди");
                System.out.println("3.Розподілити наряди");
                System.out.println("4.Змінити наряд вручну");
                System.out.println("0.Вихід");

                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> registerUsers.listUsers();
                    case 2 -> registerUsers.listActiveDyte();
                    case 3 -> registerUsers.autoDistribution();
                    case 4 -> changeDutyManually(scanner, registerUsers);
                }
            } while (choice != 0);
        } else {
            int choice;
            do {
                System.out.println("\n--- Меню користувача ---");
                System.out.println("1. Переглянути свої наряди");
                System.out.println("0. Вихід");

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.println(user);
                    }
                }
            } while (choice != 0);
        }

        System.out.println("Ви вийшли з програми.");
    }


    private static void changeDutyManually(Scanner scanner, Register_Users registerUsers) {
        System.out.println("Виберіть користувача для зміни наряду:");
        registerUsers.listUsers();
        System.out.println("Введіть ім'я користувача: ");
        String username = scanner.nextLine();

        User user = registerUsers.getUsers().stream()
                .filter(u -> u.getFullname().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        if (user != null) {
            System.out.println("Що ви хочете зробити?");
            System.out.println("1. Призначити наряд");
            System.out.println("2. Забрати наряд");

            int actionChoice = scanner.nextInt();
            scanner.nextLine();
            if (actionChoice == 1) {

                System.out.println("Виберіть наряд для призначення:");
                System.out.println("1. Наряд на столову");
                System.out.println("2. Наряд на курс");

                int dutyChoice = scanner.nextInt();
                scanner.nextLine();

                Duty duty = null;
                switch (dutyChoice) {
                    case 1 -> duty = new Duty("Наряд на столову", 1);
                    case 2 -> duty = new Duty("Наряд на курс", 1);
                    default -> System.out.println("Невірний вибір");
                }

                if (duty != null) {
                    user.addDuty(duty);
                    System.out.println("Наряд успішно призначено користувачу " + user.getFullname());
                }
            } else if (actionChoice == 2) {
                // Забираємо наряд
                System.out.println("Виберіть наряд для видалення:");
                for (int i = 0; i < user.getDuties().size(); i++) {
                    Duty d = user.getDuties().get(i);
                    System.out.println((i + 1) + ". " + d.getDescription() + " на " + d.getDate());
                }

                int dutyIndex = scanner.nextInt() - 1;
                scanner.nextLine();

                if (dutyIndex >= 0 && dutyIndex < user.getDuties().size()) {
                    Duty dutyToRemove = user.getDuties().get(dutyIndex);
                    user.getDuties().remove(dutyToRemove);  // Видаляємо наряд
                    System.out.println("Наряд " + dutyToRemove.getDescription() + " успішно забрано у користувача " + user.getFullname());
                } else {
                    System.out.println("Невірний вибір наряду.");
                }
            } else {
                System.out.println("Невірний вибір дії.");
            }
        } else {
            System.out.println("Користувач не знайдений.");
        }
    }
}

