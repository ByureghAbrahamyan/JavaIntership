import hibernateImpl.BookManagment;
import hibernateImpl.CustomerManagment;
import hibernateImpl.ReportsManagment;
import hibernateImpl.SalesManagment;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static int choice = 0;


    public static void main(String[] args) {

        while (true) {
            printManu();
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    BookManagment.start();
                    break;
                case 2:
                    CustomerManagment.start();
                    break;
                case 3:
                    SalesManagment.salesStart();
                    break;
                case 4:
                    ReportsManagment.start();
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void printManu() {

        StringBuilder sb = new StringBuilder();
        sb.append("\nBookstore Management System " +
                "\n1. Book ServicesReports " +
                "\n2. Customer ServicesReports " +
                "\n3. Sales Processing " +
                "\n4. Sales Reports " +
                "\n5. Exit " +
                "Enter your choice: ");
        System.out.println(sb);
    }

}
