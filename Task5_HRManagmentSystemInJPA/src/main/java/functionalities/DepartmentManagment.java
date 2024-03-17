package functionalities;

import entity.Department;
import entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import validation.ValidationManagment;

import javax.management.Query;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class DepartmentManagment {

    private static final SessionFactory SESSION_FACTORY = HibernateConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choice = 0;

    public static void start() {
        while (true) {
            printMenu();
            try{
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    createDepartment();
                    break;
                case 2:
                    updateDepartment();
                    break;
                case 3:
                    deleteDepartment();
                    break;
                case 4:
                    assignEmployeesToDepartmentFromCLI();
                    break;
                case 5:
                    ressignEmployeesToDepartmentFromCLI();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void printMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nDepartment ManagmentSystem " +
                  "\n1. Create Department: " +
                  "\n2. Upadte Department: " +
                  "\n3. Delete Department: " +
                  "\n4. Assign Employees to Department: " +
                  "\n5. Ressign Employees to Department: " +
                  "\n6. Back to Main Menu: " +
                  "\n Enter your choice:");
        System.out.println(sb);
    }


    public static void createDepartment() {

        Department department = new Department();
        System.out.println("Please enter departmentName");
        String departmentName = ValidationManagment.getNonEmptyInput("Name cann't be empty.");
        department.setDepartmentName(departmentName);
        System.out.println("Please enter location");
        String location = ValidationManagment.getNonEmptyInput("Location cannot be empty.");
        department.setLocation(location);

        insertingDep(department);
    }

    public static void insertingDep(Department department) {

        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(department);
                transaction.commit();
                System.out.println("Department create successfully.");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error department creating: ");
            }
        }
    }

//    public static void printAllDepartment() {
//
//        try(Session session = SESSION_FACTORY.openSession()){
//            Query query = (Query)session.createQuery("FROM Department ");
//            List<Department> list = query.list();
//            for (Department department : list){
//                System.out.println(department);
//            }
//        }
//    }


    public static void printAllDepartment() {

        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = (Query) session.createQuery("FROM Department", Department.class);
            List<Department> list = (List<Department>) ((org.hibernate.query.Query<?>) query).getResultList();
            for (Department department : list) {
                System.out.println(department);
            }
        }
    }

    public static void updateDepartment() {

        printAllDepartment();

        System.out.println("Please enter id update");
        int departmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                                                           "Please enter a valid quantity in stock.");
        Department department = new Department();
        department.setDepartmentId((long)departmentId);

        System.out.println("Please enter departmentName");
        String departmentName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        department.setDepartmentName(departmentName);

        System.out.println("Please enter location");
        String location = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        department.setLocation(location);

        updateService(department);
    }

    public static void updateService(Department department) {

        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                session.update(department);
                transaction.commit();
                System.out.println("Department update successful");
            } catch (Exception e){
                if(transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error updating Department_id doesn't exist: ");
            }
        }
    }

    public static void deleteDepartment() {

        printAllDepartment();

        System.out.println("Please enter the department ID to delete:");
        int departmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID: ");

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Department department = session.get(Department.class, (long)departmentId);
                if(department != null) {
                    session.delete(department);
                    transaction.commit();
                    System.out.println("Department deleted successfully.");
                } else {
                    System.out.println("Department with ID " + departmentId + "does not exist.");
                }
            } catch (Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                System.err.println("Error deleting department: " + e.getMessage());
            }
        }
    }

    public static void assignEmployeesToDepartmentFromCLI() {

        System.out.println("\nAssign Employees to Department");
        System.out.println("Enter Department ID");
        int departmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        System.out.println("Enter Employee IDs (comma-sepaarated): ");
        String employeeIdsString = ValidationManagment.getNonEmptyInput("No valid input");
        String[] employeeIdsArray = employeeIdsString.split(",");
        List<Long> employeeIds = new ArrayList<>();
        for (String employeeId : employeeIdsArray) {
            employeeIds.add(Long.parseLong(employeeId.trim()));
        }

        assignEmployeesToDepartment((long)departmentId, employeeIds);
    }


    public static void assignEmployeesToDepartment(Long departmentId, List<Long> employeeIds) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department department = session.get(Department.class, departmentId);

            if (department != null) {
                for (Long employeeId : employeeIds) {
                    Employee employee = session.get(Employee.class, employeeId);
                    if(employee != null) {
                        department.getEmployees().add(employee);
                    }
                }
                session.update(department);
                transaction.commit();
                System.out.println("Employees assigned to departmnet successfully.");
            } else {
                System.out.println("Department with ID " + departmentId + " does not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error assigning employees to department: " + e.getMessage());
        }
    }


    public static void ressignEmployeesToDepartmentFromCLI() {

        System.out.println("\nRessign Employees to Department");
        System.out.println("Enter old Department ID: ");
        int oldDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        System.out.println("Enter new Department ID: ");
        int newDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        System.out.println("Enter Employee IDs (comma-sepaarated): ");

        String employeeIdsString = ValidationManagment.getNonEmptyInput("No valid input");
        String[] employeeIdsArray = employeeIdsString.split(",");
        List<Long> employeeIds = new ArrayList<>();
        for (String employeeId : employeeIdsArray) {
            employeeIds.add(Long.parseLong(employeeId.trim()));
        }

        ressignEmployeesToDepartment(employeeIds, (long) oldDepartmentId, (long) newDepartmentId);
    }


    public static void ressignEmployeesToDepartment(List<Long> employeeIds, Long oldDepartmentId, Long newDepartmentId) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department oldDepartment = session.get(Department.class, oldDepartmentId);
            Department newDepartment = session.get(Department.class, newDepartmentId);

            if (oldDepartment != null && newDepartment != null) {
                for (Long employeeId : employeeIds) {
                    Employee employee = session.get(Employee.class, employeeId);
                    if(employee != null) {
                        oldDepartment.getEmployees().remove(employee);
                        newDepartment.getEmployees().add(employee);
                    }
                }

                session.update(oldDepartment);
                session.update(newDepartment);
                transaction.commit();
                System.out.println("Employees ressigned to departmnet successfully.");
            } else {
                System.out.println("One or both of the departments do not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error ressigning employees to department: " + e.getMessage());
        }
    }

}