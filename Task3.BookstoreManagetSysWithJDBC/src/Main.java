import java.beans.Customizer;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main{

    static Scanner sc;

    public static void main(String[] args) {

        sc = new Scanner(System.in);
        while (true) {
            int choice = 0;
            printMenu();
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    BookServices.bookStart();
                    break;
                case 2:
                    CustomerServices.customerStart();
                    break;
                case 3:
                    SalesServices.salesStart();
                    break;
                case 4:
                    ReportsImplServices.ServicesReportsStart();
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\n Bookstore Managment System ");
        System.out.println("1. Book ServicesReports");
        System.out.println("2. Customer ServicesReports");
        System.out.println("3. Sales ServicesReports");
        System.out.println("4. Sales Reports");
        System.out.println("5. Exit");
        System.out.println("Enter your choice: ");
    }
}