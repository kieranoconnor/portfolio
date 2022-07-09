/*
Kieran O'Connor
CSE341 Final

*/
import java.util.Scanner;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;

public class customer {
    public static void customerInterface(Connection con)
            throws SQLException, IOException, java.lang.ClassNotFoundException {
        try {
            //Inital menu
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter Customer ID:");

            String id = getID(input, con);
            try {
                boolean cont = false;
                int choice = 0;

                do {

                    System.out.println("\nWhat would you like to do?");
                    System.out.println("1 to check your policies");
                    System.out.println("2 to check payments due");
                    System.out.println("3 to check on claims");
                    System.out.println("4 to check on insurables");
                    System.out.println("5 to quit");
                    while (!input.hasNextInt()) {
                        System.out.println("Incorrect input, try again");
                        input.next();
                    }
                    choice = input.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.println("Policies");
                            policies(input, con, id);
                            break;
                        case 2:
                            System.out.println("Payment");
                            payments(input, con, id);
                            break;
                        case 3:
                            System.out.println("Claims");
                            claims(input, con, id);
                            break;

                        case 4:
                            System.out.println("Insurables");
                            getItems(input, con, id);
                            break;

                        case 5:
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
                    String query = "select name from customer natural join person where id=" + customerId + "";

                    result = s.executeQuery(query);

                    if (!result.next()) {
                        System.out.println("Customer does not exist. Please Reenter ID.");
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
            System.out.println("Incorrect Input");
        }
        return customerId;
    }

    public static void payments(Scanner input, Connection con, String id) {
        try {
            String query = "select * from customer natural join policy_holder where id=" + id;
            Statement s = con.createStatement();
            ResultSet result = null;
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no open policies with us.");
            } else {
                System.out.println("Here are your payments due");
                System.out.println("Policy Number\tPayment Due");
                do {

                    System.out.println("\t" + result.getString(2) + "\t\t$" + result.getInt(3));

                } while (result.next());
                boolean cont = false;
                try {
                    do

                    {

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1 to make a payment");
                        System.out.println("2 to quit");
                        while (!input.hasNextInt()) {
                            System.out.println("Incorrect input, try again");
                            input.next();
                        }
                        int choice = input.nextInt();
                        switch (choice) {
                            case 1:
                                if (makePayment(input, con, id)) {
                                    System.out.println("Payment Successful.");
                                } else {
                                    System.out.println("Payment Unsuccessful");
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
                    System.out.println("Incorrect Input");

                }
            }
        } catch (Throwable t) {
            System.out.println("Incorrect input.");

        }

    }

    public static void policies(Scanner input, Connection con, String id) {
        ResultSet result;
        try {
            String polType = "";
            String mainAtt = "";
            String insertqs = "";
            System.out.println("1 Car policies\n2 Home Policies\n3 Life Policies\n4 Unique Item policies\n5 quit");
            boolean correct = false;
            int choice = input.nextInt();
            do {
                switch (choice) {

                    case 1:
                        polType = "car";
                        mainAtt = "Serial_number";
                        break;

                    case 2:
                        polType = "home";
                        mainAtt = "Address";

                        break;

                    case 3:
                        polType = "life";
                        mainAtt = "Name";
                        break;

                    case 4:
                        polType = "unique_item";
                        mainAtt = "Description";
                        break;

                    case 5:
                        System.out.println("You chose to quit. Good Choice.");
                        correct = true;
                        break;

                    default:
                        System.out.println("Please only enter 1-5");
                        correct = false;
                        break;
                }
                correct = true;

            } while (!correct);
            String query = "";

            query = "select policy_number, value, item_id, " + mainAtt + " from policy_holder natural join " + polType
                    + "_insurance natural join " + polType + " natural join policy where id =" + id;
            // "select * from policy_holder natural join" + polType+ " where id ="+id;
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no open policies with us.");
            } else {
                System.out.println("Here are your policies on file.");
                System.out.println("Policy Number\tValue\tItem ID\t" + mainAtt);
                do {

                    System.out.println("" + result.getString(1) + "\t\t$" + result.getString(2) + "\t"
                            + result.getInt(3) + "\t" + result.getString(4));

                } while (result.next());
            }

            System.out.println("1 - new policy\n2 - drop policy\n3 - quit");
            int polChoice = input.nextInt();

            if (polChoice == 3) {
                return;
            } else if (polChoice == 2) {
                dropPol(input, con, id);

            } else {
                System.out.println("Pick what kind of policy you would like.");
                System.out.println("1 Car \n2 Home \n3 Life \n4 Unique Item \n5 quit");
                choice = input.nextInt();
                correct = false;
                do {
                    correct = true;
                    switch (choice) {

                        case 1:
                            addPol(input, con, "car", id);
                            break;

                        case 2:
                            addPol(input, con, "home", id);

                            break;

                        case 3:
                            addPol(input, con, "life", id);
                            break;

                        case 4:
                            addPol(input, con, "unique_item", id);
                            break;

                        case 5:
                            System.out.println("You chose to quit. Good Choice.");
                            correct = true;
                            break;

                        default:
                            System.out.println("Please only enter 1-5");
                            correct = false;
                            break;
                    }
                    correct = true;

                } while (!correct);

            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");


        }

    }

    public static void dropPol(Scanner input, Connection con, String id) {
        try {
            ResultSet result;
            Statement s = con.createStatement();
            String query = "";
            System.out.println("Please enter the policy number of the policy you wish to drop.\n0 - quit");
            int dropPol = input.nextInt();
            if (dropPol == 0) {
                return;
            }
            query = "select payment_due from policy_holder where id=" + id + " and policy_number =" + dropPol;

            result = s.executeQuery(query);

            if (!result.next()) {
                System.out.println("You have no policy with that number attached to your account.");

            } else if (result.getInt(1) > 0) {
                System.out.println("You owe money on that policy!\nPlease pay off the policy before dropping");
            }

            else {
                query = "delete from life_insurance where policy_number = " + dropPol;
                s.executeUpdate(query);
                query = "delete from car_insurance where policy_number = " + dropPol;
                s.executeUpdate(query);
                query = "delete from home_insurance where policy_number = " + dropPol;
                s.executeUpdate(query);
                query = "delete from unique_item_insurance where policy_number = " + dropPol;
                s.executeUpdate(query);
                query = "delete from policy_holder where policy_number = " + dropPol;
                s.executeUpdate(query);
                query = "delete from policy where policy_number = " + dropPol;
                s.executeUpdate(query);
                System.out.println("Policy Successfully dropped.");
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");

        }
    }

    public static void claims(Scanner input, Connection con, String id) {
        ResultSet result;
        try {

            String query = "select policy_number,approval_status,percent_paid,status from customer natural join policy_holder natural join claim where id="
                    + id;
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no claims with us.");
            } else {
                System.out.println("Here are your claims on file to date.");
                System.out.println("Policy Number\tApproval Status\tPercent Paid\tOpen/Closed");
                do {

                    System.out.println("\t" + result.getString(1) + "\t\t" + result.getString(2) + "\t"
                            + result.getInt(3) + "%\t\t" + result.getString(4));

                } while (result.next());
                System.out.println("1 - File Claim\n2 - quit");
                if(input.nextInt() == 1)
                {
                    openClaim(input, con, id);
                }
                
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");


        }

    }

    public static boolean makePayment(Scanner input, Connection con, String id) {

        boolean updated = false;
        ResultSet result;
        try {
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Enter which policy you would like to pay. Enter 0 to return to payment menu.");
            int policyChoice = input.nextInt();

            if (policyChoice == 0) {
                return false;
            }
            String query = "select payment_due from policy_holder where id=" + id + " and policy_number ="
                    + policyChoice;

            result = s.executeQuery(query);

            if (!result.next()) {
                System.out.println("You have no policy with that number attached to your account.");

            } else if (result.getInt(1) == 0) {
                System.out.println("You owe no money on that policy");
            } else {
                boolean correct = false;
                int due = 0;
                int payment = 0;
                while (!correct) {
                    due = result.getInt(1);
                    System.out.println("You owe $" + due);
                    System.out.println("Enter how much you would like to pay:");
                    payment = input.nextInt();
                    if (payment > due) {
                        System.out.println("You are trying to pay too much!");
                    } else if (payment < 0) {
                        System.out.println("Focus on paying off you current debts before adding more.");
                    } else if (payment == 0) {
                        System.out.println("Seems like we have a comedian here.");
                    } else {
                        due -= payment;
                        result.updateInt(1, due);
                        result.updateRow();
                        System.out.println("You now owe $" + due + " on policy number " + policyChoice);
                        return true;
                    }
                }
            }

            return updated;
        } catch (Throwable t) {
            System.out.println("invalid input");

        }
        return updated;
    }

    public static void getItems(Scanner input, Connection con, String id) {
        try {
            ResultSet result;

            String polType = "";
            String mainAtt = "";
            String insertqs = "";
            String query = "";
            System.out.println("1 Cars\n2 Homes\n3 Lives\n4 Unique Items\n5 quit");
            boolean correct = false;
            int choice = input.nextInt();
            do {
                switch (choice) {

                    case 1:
                        polType = "car";
                        mainAtt = "Serial_number";
                        query = "select item_id, net_worth, serial_number  from insurable natural join car natural join owns where id ="
                                + id;
                        break;

                    case 2:
                        polType = "home";
                        mainAtt = "Address";
                        query = "select item_id, net_worth, address  from insurable natural join home natural join owns where id ="
                                + id;
                        break;

                    case 3:
                        polType = "life";
                        mainAtt = "Name";
                        query = "select item_id, net_worth, name from insurable natural join life natural join owns where id ="
                                + id;
                        break;

                    case 4:
                        polType = "unique_item";
                        mainAtt = "Description";
                        query = "select item_id, net_worth, description from insurable natural join unique_item natural join owns where id ="
                                + id;
                        break;

                    case 5:
                        System.out.println("You chose to quit. Good Choice.");
                        correct = true;
                        break;

                    default:
                        System.out.println("Please only enter 1-5");
                        correct = false;
                        break;
                }
                correct = true;

            } while (!correct);
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no items in that category recorded with us.");
            } else {
                System.out.println("Here are your " + polType + "s on file.");
                System.out.println("Item ID\tValue\t\t" + mainAtt);
                do {

                    System.out.println("" + result.getString("Item_id") + "\t$" + result.getString("net_worth") + "\t\t"
                            + result.getString(mainAtt));

                } while (result.next());
            }
            System.out.println("You can either choose an item to insert or quit to main menu.");
            System.out.println("1 Car\n2 Home\n3 Life\n4 Unique Item\n5 quit");
            choice = input.nextInt();
            correct = false;
            do {
                switch (choice) {

                    case 1:
                        insertCar(input, con, id);
                        break;

                    case 2:
                        insertHome(input, con, id);
                        break;

                    case 3:
                        insertLife(input, con, id);
                        break;

                    case 4:
                        insertUnique(input, con, id);
                        break;

                    case 5:
                        System.out.println("You chose to quit. Good Choice.");
                        correct = true;
                        break;

                    default:
                        System.out.println("Please only enter 1-5");
                        correct = false;
                        break;
                }
                correct = true;

            } while (!correct);

        }

        catch (Throwable t) {
        }

    }

    public static String checkInput(Scanner input) {

        boolean correct = false;

        String str = input.next();
        while (!correct) {
            if (str.contains(";") || str.contains("\'")) {
                System.out.println("You have entered something that could affect the database.");
                System.out.println("Please reenter without any semicolons or apostrophes.");
                str = input.next();

            } else {
                correct = true;
            }
        }

        return str;
    }

    public static void insertCar(Scanner input, Connection con, String id) {

        try {
            ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the Make of the car");
            String make = checkInput(input);
            System.out.println("Please enter the Model");
            String model = checkInput(input);
            System.out.println("Please enter the VIN");
            String vin = checkInput(input);
            System.out.println("Please enter value as a double");
            double val = input.nextDouble();
            System.out.println("Please describe condition of car (i.e. poor, good, great, etc.)");
            String status = checkInput(input);
            String query = "Select max(item_id) from insurable";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select item_id, net_worth, status from insurable";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateDouble("Net_worth", val);
                result.updateString("Status", status);
                result.insertRow();
            }

            query = "Select item_id,make,model,serial_number from car";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateString("Make", make);
                result.updateString("Model", model);
                result.updateString("Serial_number", vin);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", Integer.parseInt(id));
                result.updateInt("Item_id", newID);
                result.insertRow();
                System.out.println("Car successfully added");
            }
        } catch (Throwable t) {
            System.out.println("Incorrect Input");
        }

    }

    public static void insertHome(Scanner input, Connection con, String id) {
        try {
            ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the Street Name (use underscores for spaces)");
            String address = checkInput(input);
            System.out.println("Please enter the Street number");
            String numb = checkInput(input);
            System.out.println("Please enter value as a double");
            double val = input.nextDouble();
            System.out.println("Please describe condition of home (i.e. poor, good, great, etc.)");
            String status = checkInput(input);
            String query = "Select max(item_id) from insurable";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select item_id, net_worth, status from insurable";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateDouble("Net_worth", val);
                result.updateString("Status", status);
                result.insertRow();
            }

            query = "Select item_id,address from home";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateString("address", numb + " " + address);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", Integer.parseInt(id));
                result.updateInt("Item_id", newID);
                result.insertRow();
                System.out.println("Home successfully added");
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");

        }
    }

    public static void insertLife(Scanner input, Connection con, String id) {
        try {
            ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the First Name");
            String fname = checkInput(input);
            System.out.println("Please enter the Last Name");
            String lname = checkInput(input);
            System.out.println("Please enter their age");
            int age = input.nextInt();
            System.out.println("Please enter value as a double");
            double val = input.nextDouble();
            System.out.println("Please describe condition of person (i.e. poor, good, great, etc.)");
            String status = checkInput(input);
            String query = "Select max(item_id) from insurable";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select item_id, net_worth, status from insurable";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateDouble("Net_worth", val);
                result.updateString("Status", status);
                result.insertRow();
            }

            query = "Select item_id,name, age from life";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateString("name", fname + " " + lname);
                result.updateInt("age", age);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", Integer.parseInt(id));
                result.updateInt("Item_id", newID);
                result.insertRow();
                System.out.println("Life successfully added");
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");
        }
    }

    public static void insertUnique(Scanner input, Connection con, String id) {
        try {
            ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the Description");
            String desc = checkInput(input);
            System.out.println("Please enter value as a double");
            double val = input.nextDouble();
            System.out.println("Please describe condition of item (i.e. poor, good, great, etc.)");
            String status = checkInput(input);
            String query = "Select max(item_id) from insurable";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select item_id, net_worth, status from insurable";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateDouble("Net_worth", val);
                result.updateString("Status", status);
                result.insertRow();
            }

            query = "Select item_id,description from unique_item";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", newID);
                result.updateString("description", desc);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", Integer.parseInt(id));
                result.updateInt("Item_id", newID);
                result.insertRow();
                System.out.println("Unique_items successfully added");
            }
        } catch (Throwable t) {
            System.out.println("Invalid Input");
        }
    }

    public static void addPol(Scanner input, Connection con, String polType, String id) {
        try {
            ResultSet result;
            ResultSet result2;
            boolean correct = false;
            int newPolNum = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "Select max(policy_number) from policy";
            result2 = s.executeQuery(query);
            if (result2.next())
                newPolNum = result2.getInt(1) + 1;

            System.out.println(
                    "Please enter the Item ID of the " + polType + " you would like to insure\nEnter 0 to quit");
            int carID = input.nextInt();
            while (!correct) {
                if (carID == 0) {
                    return;
                }
                query = "select * from " + polType + " natural join owns where id =" + id + " and item_id = " + carID;
                result2 = s.executeQuery(query);
                if (!result2.next()) {
                    System.out.println(
                            "There is no car with that ID attached to your account.\nPlease Reenter Item ID.\n0 to quit");
                    carID = input.nextInt();
                } else
                    correct = true;

            }

            System.out.println("Please enter the value you would like to insure your" + polType + " for");
            double value = input.nextDouble();

            LocalDate currDate = LocalDate.now();
            LocalDate expDate = currDate.plusMonths(6);

            query = "select policy_number, value, start_date, exp_date from policy";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("policy_number", newPolNum);
                result.updateDouble("value", value);
                result.updateDate("start_date", java.sql.Date.valueOf(currDate));
                result.updateDate("exp_date", java.sql.Date.valueOf(expDate));

                result.insertRow();
            }

            query = "select id, policy_number from policy_holder";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", Integer.parseInt(id));
                result.updateInt("policy_number", newPolNum);
                result.insertRow();
            }

            query = "select item_id, policy_number from " + polType + "_insurance";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("ITEM_ID", carID);
                result.updateInt("policy_number", newPolNum);
                result.insertRow();
            }

            System.out.println(polType + " Policy Successfully Added.");
            System.out.println("This Policy will expire in 6 months.");
        } catch (Throwable t) {
            System.out.println("Invalid Input");
        }

    }

    public static void openClaim(Scanner input, Connection con, String id)
    {
        try
        {
            ResultSet result;
            ResultSet result2;
            boolean correct = false;
            int claimID =0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "Select max(claim_id) from claim";
            result2 = s.executeQuery(query);
            if(result2.next())
            {
                claimID = result2.getInt(1)+1;
            }
            System.out.println("What is the Policy number on the policy you would like to open a claim on?");
            int polNum = input.nextInt();
            
            query = "select * from policy_holder where policy_number ="+polNum+" and id = "+id;
            result2 = s.executeQuery(query);
            
            while(!correct)
            {
                if (result2.next())
                correct = true;

                else
                {
                    System.out.println("You have no policy with that number attached to your account.");
                    System.out.println("Please reenter the policy number. \n0 to quit");
                    polNum = input.nextInt();
                    query = "select * from policy_holder where policy_number ="+polNum+" and id = "+id;
                    result2 = s.executeQuery(query);
                }

                if(polNum ==0)
                return;
            }
            
            
            query = "select claim_id, approval_status, percent_paid, policy_number, status from claim";
            result = s.executeQuery(query);

            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("claim_id", claimID);
                result.updateInt("percent_paid", 0);
                result.updateString("approval_status", "pending");
                result.updateString("status", "open");
                result.updateInt("Policy_number", polNum);

                
                result.insertRow();
            }
            System.out.println("Claim Successfully Filed");


        }
        catch(Throwable t)
        {
            System.out.println("Invalid Input");

        }
    }

}