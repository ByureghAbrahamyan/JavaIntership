import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SalesServices {

    private static final DatabaseConnection DBC = DatabaseConnection.getInstance();
    private static final Connection connection = DBC.getConnection();
    private static Scanner sc = new Scanner(System.in);


    public static void salesStart() {
        int choirs = 0;
        while (true) {

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
                    if (BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    createTable();
                    break;
                case 2:
                    if (checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    insertSales(exemplary());
                    break;
                case 3:
                    if (checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    calculateTotalRevenueByGenre();
                    break;
                case 4:
                    if (checkSales()) {
                        break;
                    }
                    deleteSalesAll();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\nSales Management System ");
        System.out.println("1. creat sales table");
        System.out.println("2. insert sales");
        System.out.println("3. See calculate total revenue by genre");
        System.out.println("4. delete all table sales");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    public static void createTable() {
        String sql = """
                 CREATE TABLE IF NOT EXISTS sales(
                    saleId SERIAL PRIMARY KEY ,
                    bookId INTEGER,
                    customersId INTEGER,
                    dataOfSale  DATE,
                    quantitySold INTEGER NOT NULL CHECK ( quantitySold >= 0 ),
                    totalPrice REAL NOT NULL  CHECK ( totalPrice >= 0 ),
                    CONSTRAINT fkBook FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE SET NULL ,
                    CONSTRAINT fkCustomer FOREIGN KEY (customersId) REFERENCES customers(customerId) ON DELETE SET NULL 
                    );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void insertSales(Sales sales) {
        if (sales.getBookId() <= 0 || sales.getCustomersId() <= 0) {
            System.out.println("Invalid bookId or customerId. Please provide valid values.");
            return;
        }

        if (!BookServices.isBookIdValid(sales.getBookId())) {
            System.out.println("Invalid bookId. Please make sure the bookId exists in the books table.");
            return;
        } else if (!CustomerServices.isCustomerIdValid(sales.getCustomersId())) {
            System.out.println("Invalid customerId. Please make sure the customerId exists in the customers table.");
            return;
        }
    }

    public static Sales exemplary() {
        BookServices.printAllBooks();
        System.out.println("Please enter BookId:");
        int bookId = getPositiveIntInput("Invalid input. Please enter a valid BookId.");
        CustomerServices.printAllCustomers();
        System.out.println("Please enter customersId:");
        int customersId = getPositiveIntInput("Invalid input. Please enter a valid customersId.");
        System.out.println("Please enter quantitySold:");
        int quantitySold = getPositiveIntInput("Invalid input. Please enter a valid quantitySold.");
        double totalPrice = quantitySold * BookServices.searchBookIdByPrice(bookId);

        Sales sales = new Sales(bookId,customersId,quantitySold,totalPrice);


        return sales;
    }

    private static int getPositiveIntInput(String errorMessage) {
        int input;
        do {
            try {
                input = Integer.parseInt(sc.nextLine());
                if (input <= 0) {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
                input = -1;
            }
        } while (input <= 0);
        return input;
    }

    public static boolean checkSales() {
        String sql = "SELECT * FROM sales";
        try (Statement statement = connection.createStatement()){
            statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("sales no exist pleas creat sales");
            return true;
        }
        return false;
    }

    public static void calculateTotalRevenueByGenre() {
        try {
            String sql = "SELECT b.genre, SUM(b.price * s.totalPrice) AS total_revenue " +
                    "FROM sales s " +
                    "JOIN books b ON s.bookId = b.bookId " +
                    "GROUP BY b.genre";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    System.out.println("Genre: " + resultSet.getString("genre") +
                            ", Total Revenue: $" + resultSet.getDouble("total_revenue"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSalesAll() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE sales");
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}