package hibernateImpl;

import entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BookManagment {

    private static final SessionFactory SESSION_FACTORY = HibernateImplConfig.getSessionFactory();
    private static Scanner sc = new Scanner(System.in);

    public static void start() {

        int choirs = 0;
        while(true) {
            printMenu();
            try {
                choirs = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choirs) {
                case 1:
                    insertBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    listBooksByAuthor();
                    break;
                case 4:
                    listBooksByGenre();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void printMenu() {

        StringBuilder sb = new StringBuilder();
        sb.append("\nBooks Managment System " +
                "\n1. insert books " +
                "\n2. update books " +
                "\n3. search all books by author " +
                "\n4. search all books by genre " +
                "\n5. Back to Main Menu " +
                "\n Enter your choice: ");
        System.out.println(sb);
    }

    public static void insertBook() {

        Book book = new Book();
        System.out.println("Please enter title:");
        String title = ValidationManagment.getNonEmptyInput("Title cannot be empty.");
        book.setTitle(title);

        System.out.println("Please enter author:");
        String author = ValidationManagment.getNonEmptyInput("Author cannot be empty.");
        book.setAuthor(author);

        System.out.println("Please enter genre:");
        String genre = ValidationManagment.getNonEmptyInput("Genre cannot be empty.");
        book.setGenre(genre);

        System.out.println("Please enter price:");
        Double price = ValidationManagment.getPositiveDoubleInput("Price cannot be empty.");
        book.setPrice(price);

        System.out.println("Please enter quantity in stock:");
        int quantityInStock = ValidationManagment.getNonNegativeIntInput("Invalid input. " + "Please enter a valid quantity in stock.");
        book.setQuantityInStock(quantityInStock);

        insertService(book);

    }

    public static void insertService(Book book) {

        try(Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(book);
                transaction.commit();
                System.out.println("Book Inserted successfully.");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error Inserting Book: " + e.getMessage());
            }
        }
    }


    public static void updateServes(Book book) {

        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(book);
                transaction.commit();
                System.out.println("Book updated successfully.");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error updating BookId does not exist: ");
            }
        }
    }


    public static void printAllBook() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Query query = session.createQuery("FROM Book ");
            List<Book> books = query.list();
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }


    public static void updateBook() {

        printAllBook();
        Book book = new Book();
        System.out.println("Please enter id update");
        int bookId = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid quantity in stock.");
        book.setBookId((long) bookId);

        System.out.println("Please enter title:");
        String title = ValidationManagment.getNonEmptyInput("Title cannot be empty.");
        book.setTitle(title);

        System.out.println("Please enter author:");
        String author = ValidationManagment.getNonEmptyInput("Author cannot be empty.");
        book.setAuthor(author);

        System.out.println("Please enter genre:");
        String genre = ValidationManagment.getNonEmptyInput("Genre cannot be empty.");
        book.setGenre(genre);

        System.out.println("Please enter price:");
        Double price = ValidationManagment.getPositiveDoubleInput("Invalid input. Please enter a valid price.");
        book.setPrice(price);

        System.out.println("Please enter quantity in stock:");
        int quantityInStock = ValidationManagment.getNonNegativeIntInput("Invalid input. " +
                "Please enter a valid quantity in stock.");
        book.setQuantityInStock(quantityInStock);
        updateServes(book);
    }

    /**
     * search all  books by author.
     */
    public static void listBooksByAuthor() {
        System.out.println("Please enter author:");
        String author = ValidationManagment.getNonEmptyInput("Author cannot be empty.");

        try (Session session = SESSION_FACTORY.openSession()) {
            String hql = "FROM Book WHERE author = :author";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("author", author);

            List<Book> books = query.getResultList();

            if (books.isEmpty()) {
                System.out.println("No books found for the author: " + author);
            } else {
                System.out.println("Books by author " + author + ":");
                for (Book book : books) {
                    System.out.println(book);
                }
            }
        }
    }


    public static void listBooksByGenre() {
        System.out.println("please enter genre");
        String genre = ValidationManagment.getNonEmptyInput("Genre cannot by empty");
        try (Session session = SESSION_FACTORY.openSession()) {
            String hql = "FROM Book WHERE genre = :genre";
            Query<Book> query = session.createQuery(hql, Book.class);
            query.setParameter("genre", genre);
            List<Book> books = query.getResultList();
            if (books.isEmpty()) {
                System.out.println("No books found for the genre: " + genre);
            } else {
                System.out.println("Books by genre " + genre + ":");
                for (Book book : books) {
                    System.out.println(book);
                }
            }

        }
    }


    public static Book getById(Long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Book book = session.get(Book.class, id);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
