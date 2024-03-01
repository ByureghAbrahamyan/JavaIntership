package hibernateImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ReportsManagment {

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

                    informationAboutBooksSoldInTheSurvey();
                    break;
                case 2:

                    queryForFindingTheTotalRevenueGeneratedFromEachGenreOfBooks();
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
        sb.append("\nServicesReports Management System " +
                "\n1. information about books sold in the survey " +
                "\n2. query for finding the total revenue generated from each genre of books " +
                "\n3. Back to Main Menu " +
                "\nEnter your choice: ");
        System.out.println(sb);
    }


    private static void queryForFindingTheTotalRevenueGeneratedFromEachGenreOfBooks() {
        try (Session session = SESSION_FACTORY.openSession()) {
            String hql = "SELECT b.genre, SUM(s.totalPrice) AS revenue " +
                    "FROM Sales s " +
                    "INNER JOIN s.book b " +
                    "GROUP BY b.genre";

            Query query = session.createQuery(hql);
            List<Object[]> resultList = query.getResultList();

            for (Object[] result : resultList) {
                String genre = (String) result[0];
                double revenue = ((Number) result[1]).doubleValue(); // Assuming totalRevenue is a numeric type

                System.out.println("Genre: " + genre + ", Total Revenue: " + revenue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void informationAboutBooksSoldInTheSurvey() {

        try (Session session = SESSION_FACTORY.openSession()) {
            String hql = "SELECT b.title AS bookTitle, c.name AS customerName, s.dateOfSale " +
                    "FROM Sales s " +
                    "INNER JOIN s.book b " +
                    "INNER JOIN s.customer c";

            Query query = session.createQuery(hql);
            List<Object[]> resultList = query.getResultList();

            for (Object[] result : resultList) {
                System.out.println("Book Title: " + result[0] +
                        ", Customer Name: " + result[1] +
                        ", Sale Date: " + result[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}