package hibernateImpl;

import entity.Book;
import entity.Customer;
import entity.Sales;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SalesManagment {
    private static final SessionFactory SESSION_FACTORY = HibernateImplConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);
    private static int choirs = 0;


    public static void salesStart() {

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
                    insertSales();
                    break;
                case 2:
                    calculateTotalRevenueByGenre();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public static void printManu() {

        StringBuilder sb = new StringBuilder();
        sb.append("\nSales Management System " +
                "\n1. insert sales " +
                "\n2. See calculate total revenue by genre " +
                "\n3. Back to Main Menu " +
                "\n Enter your choice: ");
        System.out.println(sb);

    }


    public static void insertSales() {

        Sales sales = new Sales();
        CustomerManagment.printAllCustomer();
        System.out.println("please enter customer_id");

        int customerId = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid ID in stock.");
        Customer customer = CustomerManagment.getById((long) customerId);
        sales.setCustomer(customer);
        BookManagment.printAllBook();

        System.out.println("please enter book_id");
        int bookId = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid ID in stock.");
        Book book = BookManagment.getById((long) bookId);
        sales.setBook(book);
        sales.setDateOfSale(LocalDate.now());

        System.out.println("please enter quantitySolid");
        int quantitySolid = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid ID in stock.");
        sales.setQuantitySolid(quantitySolid);
        updateStore((long) bookId, quantitySolid);

        Double bookPrice = book.getPrice();
        Double totalPrice = bookPrice * quantitySolid;
        sales.setTotalPrice(totalPrice);
        insertService(sales);

    }


    public static void insertService(Sales sales) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(sales);
                transaction.commit();
                System.out.println("Sales inserting successful");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error inserting: ");
                e.printStackTrace();
            }
        }
    }


    public static void updateStore(Long bookId, int soldQuantity) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Book book = session.get(Book.class, bookId);
                book.setQuantityInStock(book.getQuantityInStock() - soldQuantity);
                session.update(book);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("error updateStock ");
                e.printStackTrace();
            }
        }
    }


    public static Double calculateTotalRevenueByGenre() {

        BookManagment.printAllBook();
        System.out.println("pleas enter genre");
        String genre = ValidationManagment.getNonEmptyInput("Genre cannot be empty.");
        String hql = "SELECT SUM(s.totalPrice) FROM Sales s" +
                " INNER JOIN s.book b " +
                "WHERE b.genre = :genre";

        try (Session session = SESSION_FACTORY.openSession()) {
            Query<?> query = session.createQuery(hql, Double.class);
            query.setParameter("genre", genre);

            Double totalRevenue = query.uniqueResult() != null ? (Double) query.uniqueResult() : 0.0;

            System.out.println("Total revenue for genre " + genre + ": " + totalRevenue);
            return totalRevenue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calculating total revenue by genre: " + e.getMessage());
        }
    }
}