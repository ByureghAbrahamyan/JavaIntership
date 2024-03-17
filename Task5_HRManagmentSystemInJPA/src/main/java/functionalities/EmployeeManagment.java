package functionalities;

import entity.Department;
import entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import validation.ValidationManagment;

import javax.management.Query;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagment {

    private static final SessionFactory SESSION_FACTORY = HibernateConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choice = 0;

    public static void start() {

        while (true) {
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
                     insertEmployee();
                     break;
                 case 2:
                     updateEmployee();
                     break;
                 case 3:
                     return;
                 default:
                     System.out.println("Invalid choice. Please try again.");
             }
        }
    }

    public static void printMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nEmployee Managment System " +
                "\n1. Insert Employee " +
                "\n2. Update Employee by information " +
                "\n3. Back to Main Menu " +
                "\n Enter your choice: ");
        System.out.println(sb);
    }


    public static void insertEmployee() {

        Employee employee = new Employee();

        System.out.println("Please enter name:");
        String name = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        employee.setName(name);

        System.out.println("Please enter email.");
        String email = ValidationManagment.getEmailInput("Invalid email format." + "Please enter a valid email.");
        employee.setEmail(email);

        System.out.println("Please enter phoneNumber:");
        String phoneNumber = "" + ValidationManagment.getPhoneInput("Inavalid phone format." +
                "Please enter a valid phone number.(000-00-00-00");
        employee.setPhoneNumber(phoneNumber);

        System.out.println("Please enter hireDate:");
        String hireDate = ValidationManagment.getHireDateInput("Please enter the hire date in the format yyyy-MM-dd:");
        employee.setHireDate(hireDate);

        System.out.println("Please enter jobTitle");
        String jobTotle = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        employee.setJobTitle(jobTotle);

        insertService(employee);
    }


    public static void insertService(Employee employee) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(employee);
                transaction.commit();
                System.out.println("Employee Inserted successfully.");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error Inserting Employee: " + e.getMessage());

            }

        }
    }

    public static void updateEmployee(){

        printAllEmployee();
        System.out.println("Please enter id where update Employee by information");
        int id = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid ID in stock.");
        Employee employee = new Employee();
        employee.setEmployeeId((long)id);

        System.out.println("Please enter name:");
        String newName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        employee.setName(newName);

        System.out.println("Please enter email:");
        String newEmail = ValidationManagment.getEmailInput("Invalid email format. + " +
                "Please enter a valid email.");
        employee.setEmail(newEmail);

        System.out.println("Please enter phone:");
        String newPhone = ValidationManagment.getPhoneInput("Invalid phone format. " +
                "Please enter a valid phone number.(000-00-00-00)");
        employee.setPhoneNumber(newPhone);

        System.out.println("Please enter hireDate:");
        String hireDate = ValidationManagment.getHireDateInput("Invalid hireDate format. " +
                "Please enter valid hireDate format yyyy-MM-dd:");
        employee.setHireDate(hireDate);

        System.out.println("Please enter jobTitle");
        String jobTitle = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        employee.setJobTitle(jobTitle);

        updateService(employee);
    }


//    public static void printAllEmployee() {
//
//        try(Session session = SESSION_FACTORY.openSession()) {
//            Query query = (Query) session.createQuery("FROM Employee");
//            List<Employee> list = query.list();
//            for(Employee employee : list) {
//                System.out.println(employee);
//            }
//        }
//
//    }


    public static void printAllEmployee() {

        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = (Query) session.createQuery("FROM Department", Department.class);
            List<Employee> list = (List<Employee>) ((org.hibernate.query.Query<?>) query).getResultList();
            for (Employee employee : list) {
                System.out.println(employee);
            }
        }
    }

    public static void updateService(Employee employee) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(employee);
                transaction.commit();
                System.out.println("Employee update successfull");
            } catch (Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error updating employee_id does not exist: ");
            }
        }
    }
}
