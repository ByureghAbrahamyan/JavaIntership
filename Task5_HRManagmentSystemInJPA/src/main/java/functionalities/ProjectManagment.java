package functionalities;

import entity.Department;
import entity.Employee;
import entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import validation.ValidationManagment;

import javax.management.Query;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ProjectManagment {

    private static final SessionFactory SESSION_FACTORY = HibernateConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choice = 0;


    private static void printManu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nProject Management System " +
                "\n1. Create Project: " +
                "\n2. Update Project: " +
                "\n3. Delete Project: " +
                "\n4. Assign Employees to Project: " +
                "\n5. Reassign Employees to Project: " +
                "\n6. Back to Main Menu: " +
                "Enter your choice: ");
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
                    createProject();
                    break;
                case 2:
                    updateProject();
                    break;
                case 3:
                    deleteProject();
                    break;
                case 4:
                    assignEmployeesToDepartmentFromCLI();
                    break;
                case 5:
                    reassignEmployeesToDepartmentFromCLI();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void createProject() {

        Project project = new Project();
        System.out.println("Please enter projectName");
        String projectName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        project.setProjectName(projectName);
        System.out.println("Please enter startDate");
        String startDate = ValidationManagment.getHireDateInput("Please enter the hire date in the format yyyy-MM-dd:");
        project.setStartDate(startDate);
        System.out.println("Please enter endDate");
        String endDate = ValidationManagment.getHireDateInput("Please enter the hire date in the format yyyy-MM-dd:");
        project.setEndDate(endDate);
        System.out.println("Please enter budget");
        Double budget = ValidationManagment.getPositiveDoubleInput("Please enter positive budget ");
        project.setBudget(budget);

        serviceCreate(project);
    }

    public static void serviceCreate(Project project) {

        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(project);
                transaction.commit();
                System.out.println("Project Creating successfull");
            } catch (Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                System.err.println("Error Creating Project: ");
                e.printStackTrace();
            }
        }
    }


//    public static void printAllProject() {
//        try(Session session = SESSION_FACTORY.openSession()) {
//            Query query = (Query) session.createQuery("FROM Project ");
//            List<Project> list = query.list();
//            for(Project project: list) {
//                System.out.println(project);
//            }
//        }
//    }



    public static void printAllProject() {

        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = (Query) session.createQuery("FROM Department", Department.class);
            List<Project> list = (List<Project>) ((org.hibernate.query.Query<?>) query).getResultList();
            for (Project project : list) {
                System.out.println(project);
            }
        }
    }



    public static void updateProject() {

        printAllProject();

        System.out.println("Enter project ID to update:");
        int id = ValidationManagment.getNonNegativeIntInput("No valid input");
        Project project = new Project();

        project.setProjectId((long)id);
        System.out.println("Enter new project name:");
        String newProjectName = ValidationManagment.getNonEmptyInput("No valid input.");
        project.setProjectName(newProjectName);

        System.out.println("Enter new start date (YYYY-MM-DD):");
        String newStartDate = ValidationManagment.getHireDateInput("Please enter the hire date in the format yyyy-MM-dd");
        project.setStartDate(newStartDate);

        System.out.println("Enter new end date (YYYY-MM-DD):");
        String newEndDate = ValidationManagment.getHireDateInput("Please enter the hire date in the format yyyy-MM-dd");
        project.setEndDate(newEndDate);

        System.out.println("Please enter budget");
        Double budget = ValidationManagment.getPositiveDoubleInput("Please enter positive budget ");
        project.setBudget(budget);

        serviceUpdate(project);
    }


    public static void serviceUpdate(Project project) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(project);
                transaction.commit();
                System.out.println("Project updated successfully.");
            } catch (Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                System.err.println("Error updating project: " + e.getMessage());
            }
        }
    }


    public static void deleteProject() {

        printAllProject();
        System.out.println("please enter the department ID to delete:");
        int projectId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");

        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Project project = session.get(Project.class, (long)projectId);
                if(project != null) {
                    session.delete(project);
                    transaction.commit();
                    System.out.println("Project deleted successfully.");
                } else {
                    System.out.println("Project with ID " + projectId + " does not exist.");
                }
            } catch(Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                System.err.println("Error deleting project: " + e.getMessage());
            }
        }
    }

    private static void assignEmployeesToDepartmentFromCLI() {

        System.out.println("\nAssign Employees to Department");
        System.out.println("Enter Department ID:");
        int departmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter valid department ID:");
        System.out.println("Enter Employee IDs (comma-separated): ");
        String employeeIdsString = ValidationManagment.getNonEmptyInput("No valid input");
        String[] employeeIdsArray = employeeIdsString.split(",");
        List<Long> employeeIds = new ArrayList<>();
        for(String employeeId : employeeIdsArray) {
            employeeIds.add(Long.parseLong(employeeId.trim()));
        }

        assignEmployeesToDepartment((long)departmentId, employeeIds);
    }

    public static void assignEmployeesToDepartment(Long departmentId, List<Long> employeeIds){

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department department = session.get(Department.class, departmentId);
            if(department != null) {
                for (Long employeeId : employeeIds) {
                    Employee employee = session.get(Employee.class, employeeId);
                    if(employee != null) {
                        department.getEmployees().add(employee);
                    }
                }
                session.update(department);
                transaction.commit();
                System.out.println("Employees assigned to department successfully.");
            } else {
                System.out.println("Department with ID " + departmentId + " does not exist.");
            }
        } catch (Exception e){
            System.err.println("Error assigning employees to department: " + e.getMessage());
        }
    }



    public static void reassignEmployeesToDepartmentFromCLI() {
        System.out.println("\nReassign Employees to Department");
        System.out.print("Enter old Department ID: ");
        int oldDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        System.out.print("Enter new Department ID: ");
        int newDepartmentId = ValidationManagment.getNonNegativeIntInput("Invalid input. Please enter a valid department ID:");
        System.out.print("Enter Employee IDs (comma-separated): ");
        String employeeIdsString = ValidationManagment.getNonEmptyInput("no valid input");
        String[] employeeIdsArray = employeeIdsString.split(",");
        List<Long> employeeIds = new ArrayList<>();
        for (String employeeId : employeeIdsArray) {
            employeeIds.add(Long.parseLong(employeeId.trim()));
        }

        reassignEmployeesToDepartment(employeeIds,(long) oldDepartmentId,(long) newDepartmentId);
    }


    public static void reassignEmployeesToDepartment(List<Long> employeeIds, Long oldDepartmentId, Long newDepartmentId) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            Department oldDepartment = session.get(Department.class, oldDepartmentId);
            Department newDepartment = session.get(Department.class, newDepartmentId);
            if (oldDepartment != null && newDepartment != null) {
                for (Long employeeId : employeeIds) {
                    Employee employee = session.get(Employee.class, employeeId);
                    if (employee != null) {
                        oldDepartment.getEmployees().remove(employee);
                        newDepartment.getEmployees().add(employee);
                    }
                }
                session.update(oldDepartment);
                session.update(newDepartment);
                transaction.commit();
                System.out.println("Employees reassigned to department successfully.");
            } else {
                System.out.println("One or both of the departments do not exist.");
            }
        } catch (Exception e) {
            System.err.println("Error reassigning employees to department: " + e.getMessage());
        }
    }

}
















