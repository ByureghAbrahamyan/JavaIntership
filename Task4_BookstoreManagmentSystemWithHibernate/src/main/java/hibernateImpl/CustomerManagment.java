package hibernateImpl;

import entity.Customer;
import entity.Sales;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CustomerManagment {
    private static final SessionFactory SESSION_FACTORY = HibernateImplConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choirs = 0;


    public static void start() {

        while (true) {

            printManu();
            try {
                choirs = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choirs) {
                case 1:
                    insertCustomer();
                    break;
                case 2:
                    updateCustomer();
                    break;
                case 3:
                    viewPurchaseHistory();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }

    }


    public static void printManu() {

        StringBuilder sb = new StringBuilder();
        sb.append("\nCustomers Management System " +
                "\n1. insert customer " +
                "\n2. Update customer by information " +
                "\n3. view purchase history " +
                "\n4. Back to Main Menu " +
                "\n Enter your choice: ");
        System.out.println(sb);
    }


    public static void insertService(Customer customer) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(customer);
                transaction.commit();
                System.out.println("Customer Inserted successfully.");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error Inserting Customer: " + e.getMessage());

            }
        }
    }

    /**
     * Prompts the user to input information for a new customer and inserts it into the database.
     */
    public static void insertCustomer() {

        Customer customer = new Customer();
        System.out.println("Please enter name:");
        String name = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        customer.setName(name);

        System.out.println("Please enter email:");
        String email = ValidationManagment.getEmailInput("Invalid email format." +
                " Please enter a valid email.");
        customer.setEmail(email);

        System.out.println("Please enter phone:");
        String phone = "" + ValidationManagment.getPhoneInput("Invalid phone format. " +
                "Please enter a valid phone number.(000-00-00-00)");
        customer.setPhone(phone);
        insertService(customer);
    }


    public static void updateCustomer() {

        printAllCustomer();
        Customer customer = new Customer();
        System.out.println("pleas enter id where update customer by information");
        int id = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid ID in stock.");
        customer.setCustomerId((long) id);

        System.out.println("Please enter name:");
        String newName = ValidationManagment.getNonEmptyInput("Name cannot be empty.");
        customer.setName(newName);

        System.out.println("Please enter email:");
        String newEmail = ValidationManagment.getEmailInput("Invalid email format. " +
                "Please enter a valid email.");
        customer.setEmail(newEmail);

        System.out.println("Please enter phone:");
        String newPhone = "" + ValidationManagment.getPhoneInput("Invalid phone format." +
                " Please enter a valid phone number.(000-00-00-00)");
        customer.setPhone(newPhone);

        updateService(customer);
    }


    public static void updateService(Customer customer) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(customer);
                transaction.commit();
                System.out.println("Customer update successful");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.err.println("Error updating customer_id does not exist: ");
            }
        }
    }


    public static void printAllCustomer() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = session.createQuery("FROM Customer ");
            List<Customer> list = query.list();
            for (Customer customer : list) {
                System.out.println(customer);
            }
        }
    }


    public static void viewPurchaseHistory() {

        printAllCustomer();
        System.out.println("please enter customer_id: ");
        int id = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid customer_id in stock.");
        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = session.createQuery("FROM Sales WHERE customer.customerId=:id");
            query.setParameter("id", (long) id);
            List<Sales> resultList = query.list();
            if (resultList.isEmpty()) {
                System.out.println("No purchase history found for customer_id: " + id);
            } else {
                for (Sales sales : resultList) {
                    System.out.println(sales);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Customer getById(Long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Customer customer = session.get(Customer.class, id);
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
