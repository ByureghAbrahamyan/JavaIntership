import com.sun.source.tree.BreakTree;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BookServices {
    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private static final Connection connection = databaseConnection.getConnection();
    private static Scanner sc = new Scanner(System.in);

    /**
     Այս մեթոդը ծառայում է որպես մուտքի կետ գրքերի հետ կապված գործառնությունների կառավարման համար:
     Այն օգտատիրոջը մենյու է ներկայացնում՝ թույլ տալով նրանց ընտրել գրքերի հետ կապված տարբեր
     առաջադրանքներ:
     Օգտագործողը կարող է ստեղծել գրքեր, տեղադրել օրինակելի գրքեր, թարմացնել առկա գրքերը, ցուցակագրել
     գրքերը ըստ հեղինակի, ցուցակագրել գրքերն ըստ ժանրի, ջնջել գրքերի ամբողջ աղյուսակը կամ դուրս գալ
     գրքերի կառավարման ընտրացանկից:
     */

    public static void bookStart() {

        int choirs = 0;

        while(true) {

            printMenu();

            try{
                choirs = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e){
                sc.nextLine();
                continue;
            }

            switch (choirs) {
                case 1:
                    createBooks();
                    break;
                case 2:
                    if(checkTableBooks()){
                        break;
                    }
                    insertBooks(exemplary());
                    break;
                case 3:
                    if(checkTableBooks()) {
                        break;
                    }
                    updateExemplary();
                    break;
                case 4:
                    if(checkTableBooks()) {
                        break;
                    }
                    listBooksByAuthor();
                    break;
                case 5:
                    if(checkTableBooks()) {
                        break;
                    }
                    listBooksByGenre();
                    break;
                case 6:
                    if(checkTableBooks()) {
                        break;
                    }
                    deleteTableBooks();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prints a menu for book management options to the console.
     */

    public static void printMenu() {
        System.out.println("\n Books Managment System ");
        System.out.println("1. Create books");
        System.out.println("2. Insert books");
        System.out.println("3. Update books");
        System.out.println("4. Search all books by author");
        System.out.println("5. Search all books by genre");
        System.out.println("6. Delete All Table Books");
        System.out.println("7. Back to Main Menu");
    }

    /**
     * Creates the "books" table in the database if it does not exist.
     */

    public static void createBooks() {
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    """
                             CREATE TABLE IF NOT EXISTS books(
                             bookId SERIAL PRIMARY KEY ,
                             title TEXT NOT NULL ,
                             author VARCHAR(128) NOT NULL ,
                             genre VARCHAR(128) NOT NULL ,
                             price REAL NOT NULL CHECK ( price > 0 ),
                             quantityInStock INTEGER NOT NULL CHECK ( quantityInStock >= 0 )
                             );
"""
            );
            System.out.println("Succesfull");
        }
        catch (SQLException e){
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }

    public static boolean checkTableBooks(){

        String sql = "SELECT * FROM books";

        try(Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
        } catch (SQLException e){
            System.out.println("Books doesn't exist please create table.");
            return true;
        }
        return false;
    }


    /**
     * Այս մեթոդը օգտվողին հուշում է մուտքագրել տեղեկատվություն օրինակելի Գրքի օբյեկտ ստեղծելու համար:
     * Օգտագործողից պահանջվում է մուտքագրել պահեստի անվանումը, հեղինակը, ժանրը, գինը և քանակը:
     * Մեթոդը կատարում է մուտքագրման վավերացում՝ ապահովելու համար տվյալների ճիշտ տեսակները մուտքագրված գնի
     * համար և քանակությունը պահեստում և վերադարձնում է նոր Գրքի օբյեկտ, որը սկզբնավորվել է տրամադրված արժեքներով:
     * @return Գրքի օբյեկտ՝ օգտագործողի կողմից տրամադրված տեղեկություններով:
     */
    public static Book exemplary() {
        System.out.println("Please enter title: ");
        String title = getNonEmptyInput("Title cann't be empty.");

        System.out.println("Please enter author:");
        String author = getNonEmptyInput("Author cann't be empty.");

        System.out.println("Please enter genre:");
        String genre = getNonEmptyInput("Genre cann't be empty.");

        System.out.println("Please enter price:");
        double price = getPositiveDoubleInput("Invalid input. Please enter a valid pricex.");

        System.out.println("Please enter quantity in stock:");
        double quantityInStock = getNonNegativeInput("Invalid input. Please enter a valid quantity in stock.");

        return new Book(title, author, genre, price, (int) quantityInStock);
    }


    /**
     * Կարդում և վավերացնում է օգտագործողի ոչ դատարկ տողերի մուտքագրումը վահանակի միջոցով:
     * Այն ցուցադրում է սխալի հաղորդագրություն և հուշում է նորից մուտքագրել, մինչև չտրամադրվի վավեր ոչ դատարկ տող:
     *  @param errorMessage - Սխալի հաղորդագրություն, որը պետք է ցուցադրվի անվավեր մուտքագրման համար:
     *  @return - Օգտագործողի կողմից տրամադրված վավեր ոչ դատարկ տողային մուտքագրում:
     */

    public static String getNonEmptyInput(String errorMessage) {

        String input;

        do {
            input = sc.nextLine().trim();
            if(input.isEmpty()) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }

   /**
    Կարդում և վավերացնում է օգտագործողի դրական կրկնակի մուտքը վահանակի միջոցով:
    Այն ցուցադրում է սխալի հաղորդագրություն և հուշում է նորից մուտք գործելու համար,
    մինչև վավերական դրական կրկնակի չտրամադրվի:
    @param errorMessage Սխալի հաղորդագրություն, որը ցուցադրվում է անվավեր մուտքագրման համար:
    @return Օգտատիրոջ կողմից տրամադրված վավեր դրական կրկնակի մուտքագրում:
    */

   private static double getPositiveDoubleInput(String errorMessage) {
       double input;
       do {
           try {
               input = Double.parseDouble(sc.nextLine());
               if(input <= 0) {
                   System.out.println(errorMessage);
               }
           } catch (NumberFormatException e){
               System.out.println(errorMessage);
               input = -1;
           }
       } while (input <= 0);
       return input;
   }

   /**
    Կարդում և վավերացնում է ոչ բացասական ամբողջ թվի մուտքագրումը օգտատիրոջից վահանակի միջոցով:
    Այն ցուցադրում է սխալի հաղորդագրություն և հուշում է նորից մուտքագրել, քանի դեռ վավեր
    ոչ բացասական ամբողջ թիվ չի տրամադրվել:
    @param errorMessage Սխալի հաղորդագրություն, որը ցուցադրվում է անվավեր մուտքագրման համար:
    @return Օգտագործողի կողմից տրամադրված վավեր ոչ բացասական ամբողջ թվի մուտքագրում:
    */

   private static int getNonNegativeInput(String errorMessage) {

       int input;
       do {
           try {
               input = Integer.parseInt(sc.nextLine());
               if(input < 0) {
                   System.out.println(errorMessage);
               }
           } catch (NumberFormatException e) {
               System.out.println(errorMessage);
               input = -1;
           }
       } while (input < 0);
       return input;
   }

    /**
     * Inserts a book into the "books" table.
     *
     * @param book The Book object to be inserted into the database.
     */

   public static void insertBooks(Book book) {

       try (
               PreparedStatement preparedStatement = connection.prepareStatement(
                       "INSERT INTO books(title, author, genre, price, quantityInStock) VALUES (?,?,?,?,?)"
               )
       ) {
           preparedStatement.setString(1, book.getTitle());
           preparedStatement.setString(2, book.getAuthor());
           preparedStatement.setString(3, book.getGenre());
           preparedStatement.setDouble(4, book.getPrice());
           preparedStatement.setInt(5, book.getQuantityInStock());
           preparedStatement.execute();
           System.out.println("Successful");
       }
       catch (SQLException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
   }

    /**
     * Updates book details interactively based on user input.
     */

   public static void updateExemplary() {

       String sql = "SELECT * FROM books";

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           ResultSet resultSet = preparedStatement.executeQuery();

           while (resultSet.next()) {
               System.out.println("BookID = " + resultSet.getInt("bookId") + ", " +
                       "Author = " + resultSet.getString("author") + ":");
           }
           resultSet.close();
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

       System.out.println("Please enter BookId:");
       int id = getNonNegativeInput("Invalid input. Please enter a valid BookId.");

       System.out.println("Please enter New title:");
       String title = getNonEmptyInput("Title cann't be empty.");

       System.out.println("Please enter new author:");
       String author = getNonEmptyInput("Author cann't be empty.");

       System.out.println("Please enter new genre:");
       String genre = getNonEmptyInput("Genre cann't be empty.");

       System.out.println("Please enter new price:");
       Double price = getPositiveDoubleInput("Invalid input. Please enter a valid price.");

       System.out.println("Please enter new quantityinStock:");
       int quantityInStock = getNonNegativeInput("Invalid input. Please enter a valid quantity in stock.");


       updateBooks(id, title, author, genre, price, quantityInStock);
   }

    /**
     * Updates book details in the "books" table based on the provided information.
     *
     * @param id             The bookId of the book to be updated.
     * @param title          The new title for the book.
     * @param author         The new author for the book.
     * @param genre          The new genre for the book.
     * @param price          The new price for the book.
     * @param quantityInStock The new quantity in stock for the book.
     */

   public static void updateBooks(int id, String title, String author, String genre, double price, int quantityInStock) {

       String sql = """
               UPDATE books
               SET title=?, author=?, genre=?, price=?, quantityInStock=?
               WHERE bookId=?;
               """;

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
           preparedStatement.setString(1, title);
           preparedStatement.setString(2, author);
           preparedStatement.setString(3, genre);
           preparedStatement.setDouble(4, price);
           preparedStatement.setInt(5, quantityInStock);
           preparedStatement.setInt(6, id);

           int count = preparedStatement.executeUpdate();
           if(count > 0) {
               System.out.println("Update successfully");
           } else {
               System.out.println("No such id found");
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }


    /**
     * search all  books by author.
     */

   public static void listBooksByAuthor() {

       System.out.println("Please enter author");
       String author = sc.nextLine();
       String sql = """
               SELECT * FROM books
               WHERE books.author=?;
               """;

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           preparedStatement.setString(1, author);
           ResultSet resultSet = preparedStatement.executeQuery();
           while(resultSet.next()) {
               System.out.println("id=" + resultSet.getInt("bookId") + ", " +
               "title=" + resultSet.getString("title") + ", " +
               "author=" + resultSet.getString("author") + ", " +
               "genre=" + resultSet.getString("genre") + ", " +
               "price=" + resultSet.getDouble("price") + ", "+
               "quantityInStock" + resultSet.getInt("quantityInStock"));
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }

   public static void listBooksByGenre() {

       System.out.println("Please enter genre");
       String genre = sc.nextLine();
       String sql = " SELECT * FROM books WHERE books.genre=? ";

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           preparedStatement.setString(1, genre);
           ResultSet resultSet = preparedStatement.executeQuery();
           while(resultSet.next()) {
               System.out.println("id=" + resultSet.getInt("bookId") + ", " +
                       "title=" + resultSet.getString("title") + ", " +
                       "author=" + resultSet.getString("author") + ", " +
                       "genre=" + resultSet.getString("genre") + ", " +
                       "price=" + resultSet.getDouble("price") + ", "+
                       "quantityInStock" + resultSet.getInt("quantityInStock"));
           }
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }


    /**
     * Delete all table
     */

    public static void deleteTableBooks() {

        try(Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE books");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * Checks if a given bookId is valid by querying the "books" table.
     * @param bookId The bookId to check for validity.
     * @return True if the bookId is valid, false otherwise.
     */

    static boolean isBookIdValid(int bookId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM books WHERE bookId = ?")) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches for the price of a book with the given bookId in the "books" table.
     *
     * @param id The bookId to search for.
     * @return The price of the book with the given bookId.
     */
    public static double searchBookIdByPrice(int id) {
        String sql = "SELECT * FROM books WHERE bookId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            double price = 0;
            while (resultSet.next()) {
                price = resultSet.getDouble("price");
            }
            return price;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints all books in the "books" table to the console.
     */

    public static void printAllBooks() {
        String sql = "SELECT * FROM books";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("bookId= " + resultSet.getInt("bookId") +
                        ", " + "author= " + resultSet.getString("author"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}