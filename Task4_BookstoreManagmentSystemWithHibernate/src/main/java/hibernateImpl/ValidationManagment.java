package hibernateImpl;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidationManagment {
    private static Scanner sc = new Scanner(System.in);

    public static String getNonEmptyInput(String errorMessage){

        String input;

        do {
            input = sc.nextLine().trim();
            if(input.isEmpty()) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }

    public static double getPositiveDoubleInput(String errorMessage) {

        double input;

        do {
            try {
                input = Double.parseDouble(sc.nextLine());
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


    public static int getNonNegativeIntInput(String errorMessage) {

        int input;

        do {
            try {
                input = Integer.parseInt(sc.nextLine());
                if(input < 0) {
                    System.out.println(errorMessage);
                    input = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
                input = -1;
            }
        } while (input < 0);
        return input;
    }


    public static String getEmailInput(String errorMessage) {

        String email;

        do {
            email = sc.nextLine().trim();
            if(!isValidEmail(email)) {
                System.out.println(errorMessage);
            }
        } while (!isValidEmail(email));
        return email;
    }


    public static boolean isValidEmail(String email) {

        String emailRegax = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegax, email);
    }


    public static String getPhoneInput(String errorMessage) {

        String phone;

        do {
            phone = sc.nextLine().trim();
            if (!isValidEmail(phone)) {
                System.out.println(errorMessage);
            }
        } while (!isValidEmail(phone));
        return phone;
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^(\\d{10}|\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}[-\\s]?\\d{2})$");
    }










}
