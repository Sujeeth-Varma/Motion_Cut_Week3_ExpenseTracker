import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Expense {
    Date date;
    String category;
    double amount;

    public Expense(Date date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }
}

public class ExpenseTracker {
    private static final List<User> users = new ArrayList<>();
    private static final Map<User, List<Expense>> expensesMap = new HashMap<>();

    public static void main(String[] args) {
        users.add(new User("Ramu", "password1"));
        users.add(new User("Ravi", "password2"));

        User currentUser = login();
        if (currentUser != null) {
            while (true) {
                int choice = showMenu();
                switch (choice) {
                    case 1:
                        enterExpense(currentUser);
                        break;
                    case 2:
                        listExpenses(currentUser);
                        break;
                    case 3:
                        displayCategoryWiseSummation(currentUser);
                        break;
                    case 4:
                        System.out.println("Exiting the application. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }

    private static User login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return user;
            }
        }

        System.out.println("Invalid username or password. Exiting the application.");
        System.exit(0);
        return null;
    }

    private static int showMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n1. Enter Expense");
        System.out.println("2. List Expenses");
        System.out.println("3. Category-wise Summation");
        System.out.println("4. Exit");
        System.out.println("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void enterExpense(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expense details:");
        System.out.println("Date (yyyy-MM-dd): ");
        String dateString = scanner.nextLine();
        Date date = parseDate(dateString);
        System.out.println("Category: ");
        String category = scanner.nextLine();
        System.out.println("Amount: ");
        double amount = scanner.nextDouble();

        Expense expense = new Expense(date, category, amount);
        expensesMap.computeIfAbsent(user, k -> new ArrayList<>()).add(expense);

        System.out.println("Expense added successfully.");
    }

    private static void listExpenses(User user) {
        List<Expense> expenses = expensesMap.getOrDefault(user, new ArrayList<>());
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            System.out.println("List of expenses:");
            for (Expense expense : expenses) {
                System.out.println("Date: " + expense.date + ", Category: " + expense.category + ", Amount: " + expense.amount);
            }
        }
    }

    private static void displayCategoryWiseSummation(User user) {
        Map<String, Double> categorySumMap = new HashMap<>();
        List<Expense> expenses = expensesMap.getOrDefault(user, new ArrayList<>());

        for (Expense expense : expenses) {
            categorySumMap.merge(expense.category, expense.amount, Double::sum);
        }

        System.out.println("Category-wise Summation:");
        for (Map.Entry<String, Double> entry : categorySumMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using current date.");
            return new Date();
        }
    }
}
