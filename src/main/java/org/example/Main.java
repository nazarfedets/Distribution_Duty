package org.example;

import java.time.LocalDate;
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
                System.out.println("5. Показати статистику за місяць");
                System.out.println("0.Вихід");

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> registerUsers.listUsers();
                    case 2 -> registerUsers.listActiveDyte();
                    case 3 -> registerUsers.manualDistribution(scanner);
                    case 4 -> changeduty(scanner, registerUsers);
                    case 5 -> {
                        System.out.println("Введіть рік: ");
                        int year = scanner.nextInt();
                        System.out.println("Введіть місяць: ");
                        int month = scanner.nextInt();
                        scanner.nextLine();
                        registerUsers.printQuantilyperMonth(year, month);
                    }
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


    private static void changeduty(Scanner scanner, Register_Users registerUsers) {
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

            int actionselection = scanner.nextInt();
            scanner.nextLine();

            if (actionselection == 1) {
                System.out.println("Виберіть наряд для призначення:");
                System.out.println("1. Наряд на столову");
                System.out.println("2. Наряд на курс");

                int dutyselection = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Введіть дату призначення наряду (у форматі рррр-мм-дд): ");
                String datesr = scanner.nextLine();
                LocalDate date = LocalDate.parse(datesr);

                Duty duty = null;
                switch (dutyselection) {
                    case 1 -> duty = new Duty("Наряд на столову", 1, date);
                    case 2 -> duty = new Duty("Наряд на курс", 1, date);
                    default -> System.out.println("Невірний вибір");
                }

                if (duty != null) {
                    user.addDuty(duty);
                    System.out.println("Наряд успішно призначено " + user.getFullname());
                }
            } else if (actionselection == 2) {
                System.out.println("Виберіть наряд для видалення:");
                for (int i = 0; i < user.getDuties().size(); i++) {
                    Duty d = user.getDuties().get(i);
                    System.out.println((i + 1) + ". " + d.getDescription() + " на " + d.getDate());
                }

                int indexduty = scanner.nextInt() - 1;
                scanner.nextLine();

                if (indexduty >= 0 && indexduty < user.getDuties().size()) {
                    Duty dutyremove = user.getDuties().get(indexduty);
                    user.getDuties().remove(dutyremove);
                    System.out.println("Наряд " + dutyremove.getDescription() + " забрано у " + user.getFullname());
                } else {
                    System.out.println("Невірний вибір наряду.");
                }
            } else {
                System.out.println("Невірний вибір.");
            }
        } else {
            System.out.println("Користувача не знайдений.");
        }
    }

}

