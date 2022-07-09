import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class agent {
    public static void agentInterface(Connection con)
            throws SQLException, IOException, java.lang.ClassNotFoundException {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter agent ID");
            String id = getID(input, con);
            try {
                boolean cont = false;
                int choice = 0;

                int count = 0;
                do {
                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1 to check your customers");
                    System.out.println("2 to quit");
                    while (!input.hasNextInt()) {
                        System.out.println("Incorrect input, try again.");
                        choice = input.nextInt();
                        count++;
                        if (count > 1000)
                            con.close();
                    }
                    choice = input.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.println("Customers");
                            customers(input, con, id);
                            break;
                        case 2:
                            cont = true;
                            System.out.println("You chose to quit. Good Choice.");
                            break;
                        default:
                            System.out.println("Incorrect input. Please choose 1-3");
                            choice = input.nextInt();
                    }

                } while (!cont);

            } catch (Throwable t) {
                System.out.println("Incorrect Input");
            }
        } catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

    public static String getID(Scanner input, Connection con)
            throws SQLException, IOException, java.lang.ClassNotFoundException {

        String customerId = "";
        String name = "";
        ResultSet result = null;
        boolean correct = false;
        boolean results = false;
        boolean match = false;
        try {
            do {
                try {
                    customerId = "" + input.nextInt();

                    Statement s = con.createStatement();
                    String query = "select name from agent natural join person where agent_id=" + customerId + "";

                    result = s.executeQuery(query);

                    if (!result.next()) {
                        System.out.println("Agent does not exist. Please Reenter ID.");
                    } else {
                        name = result.getString("name");
                        System.out.println("Hello " + name);
                        correct = true;

                    }

                } catch (Throwable t) {
                    System.out.println("Incorrect Input. Please reenter your ID.");

                }
            } while (!correct);
            return customerId;
        } catch (Throwable t) {
            System.out.println("Incorrect Input. 3");
        }
        return "";
    }

    public static void customers(Scanner input, Connection con, String id) {
        ResultSet result;
        try {
            String query = "select name, id, address from agent natural join person where agent_id = "
                    + id;
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no Customers.");
            } else {
                System.out.println("Here are your customers on file.");
                System.out.println("Customer\tCustomer ID\tAddress");
                do {

                    System.out.println(
                            "" + result.getString(1) + "\t\t" + result.getString(2) + "\t" + result.getString(3));

                } while (result.next());

            }
        }

        catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

}