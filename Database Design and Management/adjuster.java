import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class adjuster {
    public static void adjusterInterface(Connection con)
            throws SQLException, IOException, java.lang.ClassNotFoundException {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter adjuster ID");
            String id = getID(input, con);
            try {
                boolean cont = false;
                int choice = 0;

                int count = 0;
                do {
                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1 to check your customers");
                    System.out.println("2 to check on claims");
                    System.out.println("3 to quit");
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
                            System.out.println("Claims");
                            claims(input, con, id);
                            break;
                        case 3:
                            cont = true;
                            System.out.println("You chose to quit. Good Choice.");
                            break;
                        default:
                            System.out.println("Incorrect input. Please choose 1-3");
                            choice = input.nextInt();
                    }

                } while (!cont);
            } catch (Throwable t) {
                System.out.println("");
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
                    String query = "select unique name from adjuster natural join person where adj_id = id and id="
                            + customerId + "";

                    result = s.executeQuery(query);

                    if (!result.next()) {
                        System.out.println("Adjuster does not exist. Please Reenter ID.");
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
            String query = "select name, id, address from adjuster natural join claim natural join policy_holder natural join person where adj_id = "
                    + id;
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no Customers.");
            } else {
                System.out.println("Here are your customers on file to date.");
                System.out.println("Customer\t\tCustomer ID\tAddress");
                do {

                    System.out.println(
                            "" + result.getString(1) + "\t\t\t" + result.getString(2) + "\t\t" + result.getString(3));

                } while (result.next());

            }
        }

        catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

    public static void claims(Scanner input, Connection con, String id) {
        ResultSet result;
        try {

            String query = "select name, policy_number, claim_id, Approval_status, status, percent_paid from adjuster natural join claim natural join policy_holder natural join person where adj_id = "
                    + id;
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no claims to adjust currently.");
            } else {
                System.out.println("Here are your claims on file to date.");
                System.out.println("Customer\tPolicy_Number\tClaim ID\tApproval\tStatus\tPercent Paid\t");
                do {

                    System.out.println("" + result.getString(1) + "\t\t" + result.getString(2) + "\t"
                            + result.getString(3) + "\t\t" + result.getString(4) + "\t\t" + result.getString(5) + "\t\t"
                            + result.getInt(6) + "%");

                } while (result.next());
                boolean cont = false;
                try {
                    do

                    {

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1 to edit claim");
                        System.out.println("2 to quit");
                        while (!input.hasNextInt()) {
                            System.out.println("Incorrect input, try again");
                            input.next();
                        }
                        int choice = input.nextInt();
                        switch (choice) {
                            case 1:
                                if (editClaim(input, con, id)) {
                                    System.out.println("Claim edit Successful.");
                                } else {
                                    System.out.println("Claim edit Unsuccessful");
                                }
                                break;
                            case 2:
                                cont = true;
                                System.out.println("You chose to quit. Good Choice.");
                                break;
                            default:
                                System.out.println("Incorrect input, try again");
                        }

                    } while (!cont);
                } catch (Throwable t) {
                    System.out.println("Incorrect Input1");
                }
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");
            t.printStackTrace();

        }
    }

    public static boolean editClaim(Scanner input, Connection con, String id) {

        boolean updated = false;
        ResultSet result;
        try {
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Enter which claim you would like to update. Enter 0 to return to claim menu.");
            int claimChoice = input.nextInt();

            if (claimChoice == 0) {
                return false;
            }
            String query = "select claim_id,approval_status, status, percent_paid from claim where claim_id in (select claim_id from adjuster natural join claim natural join policy_holder natural join person where adj_id = "
                    + id + " and claim_id =" + claimChoice + ")";

            result = s.executeQuery(query);

            if (!result.next()) {
                System.out.println("You have no claims with that number attached to your account.");

            } else if (result.getString(3).equals("closed")) {
                System.out.println("This claim has already been closed");
            } else {
                boolean correct = false;
                int due = 0;
                int choice = 0;
                int updatedVal = 0;
                while (!correct) {
                    System.out.println("1 to change approval\n2 to close claim\n3 to update percent paid\n4 quit");
                    choice = input.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.println("1 set as Approved\n2 set as Pending\n3 set as Denied");
                            updatedVal = input.nextInt();
                            correct = true;
                            if (updatedVal == 1)
                                result.updateString(2, "approved");
                            else if (updatedVal == 2)
                                result.updateString(2, "pending");
                            else if (updatedVal == 3) {
                                result.updateString(2, "denied");
                                result.updateString(3, "closed");
                            } else {
                                System.out.println("Please enter 1-3");
                                correct = false;
                            }

                            break;
                        case 2:
                            if (result.getString(2).equals("pending"))
                                System.out.println("Please resolve Approval status before closing.");
                            else {
                                result.updateString(3, "closed");
                                correct = true;
                            }
                            break;
                        case 3:
                            System.out.println("Enter new percent paid as an integer");
                            updatedVal = input.nextInt();
                            if ((updatedVal <= 0) || (updatedVal > 100)) {
                                System.out.println("Please only enter a number from 0-100");
                            } else {
                                result.updateInt(4, updatedVal);
                                correct = true;
                            }
                            break;
                        case 4:
                            correct = true;
                            System.out.println("You chose to quit. Good Choice.");
                            return false;
                        default:
                            System.out.println("Incorrect input, try again");
                    }

                }

                result.updateRow();

                return true;
            }
        } catch (Throwable t) {
            System.out.println("Incorrect Input");
            t.printStackTrace();
        }
        return false;
    }
}