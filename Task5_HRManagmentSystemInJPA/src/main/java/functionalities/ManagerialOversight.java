package functionalities;

import entity.Department;
import entity.Manager;
import entity.ManagmentLevel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import validation.ValidationManagment;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ManagerialOversight {

    private static final SessionFactory SESSION_FACTORY = HibernateConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choice = 0;


    private static void printManu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nManagerial Oversight System " +
                "\n1. Assign Manager to Department: " +
                "\n2. Reassign Manager to Department: " +
                "\n3. Manage Managerial Details: " +
                "\n4. Back to Main Menu: " +
                "\n Enter your choice: ");
        System.out.println(sb);
    }


    public static void start() {
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
                    assignManagerToDepartment();
                    break;
                case 2:
                    reassignManagerToDepartment();
                    break;
                case 3:
                    manageManagerialDetails();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void printAllManager() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = session.createQuery("FROM Manager ");
            List<Manager> list = query.list();
            for (Manager manager : list) {
                System.out.println(manager);
            }
        }
    }


    public static void assignManagerToDepartment() {
        printAllManager();
        System.out.println("Enter manager name:");
        String managerName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        DepartmentManagment.printAllDepartment();
        System.out.println("Enter department ID:");
        int departmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        servesAMD(managerName, (long) departmentId);
    }


    public static void servesAMD(String managerName, Long departmentId) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();

            Department department = session.get(Department.class, departmentId);
            Manager manager = session.get(Manager.class, managerName);

            if (department != null && manager != null) {
                department.setDepartmentHead(manager.toString());
                session.update(department);
                transaction.commit();
                System.out.println("Manager assigned to department successfully.");
            } else {
                System.out.println("Manager or department does not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error assigning manager to department: " + e.getMessage());
        }
    }



    public static void reassignManagerToDepartment() {
        printAllManager();
        System.out.println("Enter manager name:");
        String managerName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        DepartmentManagment.printAllDepartment();
        System.out.println("Enter old department ID:");
        int oldDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid department ID:");
        DepartmentManagment.printAllDepartment();
        System.out.println("Enter new department ID:");
        int newDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input." +
                " Please enter a valid department ID:");
        serviceRED(managerName, (long) oldDepartmentId, (long) newDepartmentId);
    }


    public static void serviceRED(String managerName, Long oldDepartmentId, Long newDepartmentId) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();

            Department oldDepartment = session.get(Department.class, oldDepartmentId);
            Department newDepartment = session.get(Department.class, newDepartmentId);
            Manager manager = session.get(Manager.class, managerName);

            if (oldDepartment != null && newDepartment != null && manager != null) {
                oldDepartment.setDepartmentHead(null);
                newDepartment.setDepartmentHead(manager.toString());
                session.update(oldDepartment);
                session.update(newDepartment);
                transaction.commit();
                System.out.println("Manager reassigned to department successfully.");
            } else {
                System.out.println("One or more entities do not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error reassigning manager to department: " + e.getMessage());
        }
    }

    public static void manageManagerialDetails() {

        printAllManager();

        System.out.println("Enter manager name:");
        String managerName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");

        ManagmentLevel managmentLevel = null;
        do {
            System.out.println("Enter management level (TOP_LEVEL, MID_LEVEL, FIRST_LINE):");
            String levelStr = sc.nextLine().trim().toUpperCase();
            try {
                managmentLevel = ManagmentLevel.valueOf(levelStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid management level. Please try again.");
            }
        } while (true);

        System.out.println("Enter number of subordinate IDs:");
        int numSubordinates = sc.nextInt();
        List<Long> subordinateIds = new ArrayList<>();
        for (int i = 0; i < numSubordinates; i++) {
            System.out.println("Enter subordinate ID " + (i + 1) + ":");
            subordinateIds.add(sc.nextLong());
        }

        servesMED(managerName, subordinateIds, managmentLevel);
        sc.nextLine();
    }


    public static void servesMED(String managerName, List<Long> subordinateIds, ManagmentLevel managementLevel) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();

            Manager manager = session.get(Manager.class, managerName);

            if (manager != null) {
                manager.setManagedDepartment(null);

                for (Long subordinateId : subordinateIds) {
                    Department subordinate = session.get(Department.class, subordinateId);
                    if (subordinate != null) {
                        manager.setManagedDepartment(subordinate.toString());
                    }
                }
                manager.setManagmentLevel(managementLevel);

                session.update(manager);
                transaction.commit();
                System.out.println("Managerial details managed successfully.");
            } else {
                System.out.println("Manager does not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error managing managerial details: " + e.getMessage());
        }
    }

}
