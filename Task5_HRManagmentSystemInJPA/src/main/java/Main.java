import functionalities.DepartmentManagment;
import functionalities.EmployeeManagment;
import functionalities.ManagerialOversight;
import functionalities.ProjectManagment;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static final int EXIT = 0;
    private static int choice = 0;


    public static void main(String[] args) {

        printMenu();
        try {
            choice = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e){
            sc.nextLine();
            //continue;
        }


        switch (choice) {

            case 1:
                EmployeeManagment.start();
                break;
            case 2:
                DepartmentManagment.start();
                break;
            case 3:
                ProjectManagment.start();
                break;
            case 4:
                ManagerialOversight.start();
                break;
            case 5:
                System.out.println("Exiting the sysytem.");
                System.exit(EXIT);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    public static void printMenu(){

        StringBuilder sb = new StringBuilder();
        sb.append("\n HR Managment System " +
                "\n1. Employee Managment: " +
                "\n2. Department Managment: " +
                "\n3. Project Managment: " +
                "\n4. Managerial Oversight: " +
                "\n5. Exit " +
                "Enter your choice: ");
        System.out.println(sb);
    }

}