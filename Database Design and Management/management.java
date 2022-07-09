import java.util.Scanner;
import java.sql.*;
import java.time.chrono.ThaiBuddhistChronology;
import java.io.*;

public class management {
    public static void mgmtInterface(Connection con)
            throws SQLException, IOException, java.lang.ClassNotFoundException {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter management Password");
            int password = input.nextInt();
            if (password != 1) {
                System.out.println("Incorrect Password.");
                input.close();
                return;

            } else {
                try {
                    boolean cont = false;
                    int choice = 0;
                    System.out.println("Welcome to the Corporate View.");

                    do {

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1 to check your policies");
                        System.out.println("2 to check payments due");
                        System.out.println("3 to check on claims");
                        System.out.println("4 to check customers");
                        System.out.println("5 to check employees");
                        System.out.println("6 to quit");
                        while (!input.hasNextInt()) {
                            System.out.println("Incorrect input, try again");
                            input.next();
                        }
                        choice = input.nextInt();
                        switch (choice) {
                            case 1:
                                System.out.println("Policies");
                                policies(input, con);
                                break;
                            case 2:
                                System.out.println("Payment");
                                money(input, con);
                                break;
                            case 3:
                                System.out.println("Claims");
                                claims(input, con);
                                break;
                            case 4:
                                customers(input, con);
                            break;

                            case 5:
                            employees(input, con);
                            break;

                            case 6:
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
            System.out.println("Incorrect Input");
        }
    }

    public static void policies(Scanner input, Connection con) {
        ResultSet result;
        try {
            String polType = "";
            String mainAtt = "";
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

            String query = "select policy_number, value, item_id, " + mainAtt + " from policy_holder natural join "
                    + polType + "_insurance natural join " + polType + " natural join policy";
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no open policies with us.");
            } else {
                System.out.println("Here are our policies on file.");
                System.out.println("Policy Number\tValue\tItem ID\t" + mainAtt);
                do {

                    System.out.println("" + result.getString(1) + "\t\t$" + result.getString(2) + "\t"
                            + result.getInt(3) + "\t" + result.getString(4));

                } while (result.next());

                query = "select Sum(value) from policy_holder natural join " + polType + "_insurance natural join "
                        + polType + " natural join policy";
                result = s.executeQuery(query);
                if (result.next())
                    System.out.println(
                            "The combined values of all our " + polType + " policies is $" + result.getString(1));
            }
            System.out.println("Would you like to drop a policy?\n1 - yes\n2 - no");
            if(input.nextInt() == 2)
            return;
            dropPol(input, con);

        } catch (Throwable t) {
            System.out.println("Invalid Input");

        }

    }

    public static void customers(Scanner input, Connection con) {
        ResultSet result;
        try {
            String query = "select name, id, address from customer natural join person";
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no Customers.");
            } else {
                System.out.println("Here are all our customers on file.");
                System.out.println("Customer\tCustomer ID\tAddress");
                do {

                    System.out.println(
                            "" + result.getString(1) + "\t\t" + result.getString(2) + "\t" + result.getString(3));

                } while (result.next());

                

            }
            
            System.out.println("1 - add Customer\n2 - exit");
                if(input.nextInt()==1)
                addCust(input, con);
        } catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

    public static void employees(Scanner input, Connection con) {
        ResultSet result;
        try {
            String query = "select name, id, address from employee natural join person";
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no Employees. Yikes.");
            } else {
                System.out.println("Here are all our employees on file.");
                System.out.println("Customer\tCustomer ID\tAddress");
                do {

                    System.out.println(
                            "" + result.getString(1) + "\t\t" + result.getString(2) + "\t" + result.getString(3));

                } while (result.next());

            }

            System.out.println("1 - add Employee\n2 - add Agent\n3 - add Adjuster\n4 - exit");
            int choice = input.nextInt();
            switch(choice)
            {
            case 1:
            addEmp(input, con);
            break;
            
            case 2 :
            addAgent(input, con);
            break;

            case 3 :
            addAdjuster(input,con);
            
            default:
            return;
            

            }
        } catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

    public static void money(Scanner input, Connection con) {
        ResultSet result;
        try {
            String query = "select Sum(payment_due) from policy_holder";
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            System.out.println("The total owed to CGI is $" + result.getString(1));

            System.out.println("Would you like to see dues per policy?\n1 - yes \n2 - no");
            if(input.nextInt() == 1)
            payments(input, con);
            return;

        } catch (Throwable t) {
            System.out.println("Incorrect Input");
        }
    }

    public static void payments(Scanner input, Connection con) 
    {
        try {
            String query = "select * from customer natural join policy_holder";
            Statement s = con.createStatement();
            ResultSet result = null;
            result = s.executeQuery(query);
            if (!result.next()) {
                System.out.println("You have no open policies with us.");
            } else {
                System.out.println("Here are policies and the payments due");
                System.out.println("Policy Number\tPayment Due");
                do {

                    System.out.println("\t" + result.getString(2) + "\t\t$" + result.getInt(3));

                } while (result.next());
                boolean cont = false;
                try {
                    do

                    {

                        System.out.println("\nWhat would you like to do?");
                        System.out.println("1 to edit amount owed");
                        System.out.println("2 to quit");
                        while (!input.hasNextInt()) {
                            System.out.println("Incorrect input, try again");
                            input.next();
                        }
                        int choice = input.nextInt();
                        switch (choice) {
                            case 1:
                                if (makePayment(input, con)) {
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
                    System.out.println("Incorrect Input1");

                }
            }
        } catch (Throwable t) {
            System.out.println("Incorrect input.");
            t.printStackTrace();
        }
    }

    public static void claims(Scanner input, Connection con) {
        ResultSet result;
        try {

            String query = "select name, policy_number, claim_id, Approval_status, status, percent_paid from  claim natural join policy_holder natural join person";
            Statement s = con.createStatement();
            result = s.executeQuery(query);
            String tabs = "";
            String tabs2 = "";
            if (!result.next()) {
                System.out.println("You have no claims to adjust currently.");
            } else {
                System.out.println("Here are your claims on file to date.");
                System.out.println("Customer\t\tPolicy_Number\tClaim ID\tApproval\tStatus\tPercent Paid\t");
                do {

                    if (result.getString(1).length() >= 16)
                        tabs = "\t\t";

                    else
                        tabs = "\t\t\t";

                    if (result.getString(4).equals("approved"))
                        tabs2 = "\t";
                    else
                        tabs2 = "\t\t";

                    System.out.println("" + result.getString(1) + tabs + result.getString(2) + "\t"
                            + result.getString(3) + "\t\t" + result.getString(4) + tabs2 + result.getString(5) + "\t\t"
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
                                if (editClaim(input, con)) {
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

    public static boolean editClaim(Scanner input, Connection con) {

        boolean updated = false;
        ResultSet result;
        try {
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Enter which claim you would like to update. Enter 0 to return to claim menu.");
            int claimChoice = input.nextInt();

            if (claimChoice == 0) {
                return false;
            }
            String query = "select claim_id,approval_status, status, percent_paid from claim where claim_id ="
                    + claimChoice;

            result = s.executeQuery(query);

            if (!result.next()) {
                System.out.println("You have no claims with that number attached to your account.");

            }  else {
                boolean correct = false;
                int due = 0;
                int choice = 0;
                int updatedVal = 0;
                while (!correct) {
                    System.out.println("1 to change approval\n2 to change open/close status\n3 to update percent paid\n4 quit");
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
                        System.out.println("1 set as open\n2 set as close");
                        updatedVal = input.nextInt();
                        if (updatedVal == 1)
                        result.updateString("status","open");
                        else{
                            if (result.getString(2).equals("pending"))
                                System.out.println("Please resolve Approval status before closing.");
                            else {
                                result.updateString(3, "closed");
                                correct = true;
                            }
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

    public static void dropPol(Scanner input, Connection con) {
        try {
            ResultSet result;
            Statement s = con.createStatement();
            String query = "";
            System.out.println("Please enter the policy number of the policy you wish to drop.\n0 - quit");
            int dropPol = input.nextInt();
            if (dropPol == 0) {
                return;
            }
            query = "select payment_due from policy_holder where policy_number =" + dropPol;

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
            t.printStackTrace();
        }
    }

    public static boolean makePayment(Scanner input, Connection con) {

        boolean updated = false;
        ResultSet result;
        try {
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Enter which policy you would like to pay. Enter 0 to return to payment menu.");
            int policyChoice = input.nextInt();

            if (policyChoice == 0) {
                return false;
            }
            String query = "select payment_due from policy_holder where policy_number ="
                    + policyChoice;

            result = s.executeQuery(query);

            if (!result.next()) {
                System.out.println("You have no policy with that number.");

            
            } else {
                boolean correct = false;
                int due = 0;
                int payment = 0;
                while (!correct) {
                    due = result.getInt(1);
                    System.out.println("This policy owes $" + due);
                    System.out.println("Enter how much the policy should be updated to owe:");
                    payment = input.nextInt();
                    if (payment < 0) {
                        System.out.println("The police have been called and they are one their way.");
                    } else {
                        due = payment;
                        result.updateInt(1, due);
                        result.updateRow();
                        System.out.println("This policy now owes $" + due + " on policy number " + policyChoice);
                        return true;
                    }
                }
            }
           
        } catch (Throwable t) {
            System.out.println("Invalid Input");
            t.printStackTrace();
        }
        return updated;
    }


    public static void addCust(Scanner input, Connection con)
    {
        try{
        ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the First Name");
            String fname = checkInput(input);
            System.out.println("Please enter the Last Name");
            String lname = checkInput(input);
            System.out.println("Please enter the Street Name (use underscores for spaces)");
            String address = checkInput(input);
            System.out.println("Please enter the Street number");
            String numb = checkInput(input);
            String query = "Select max(id) from person";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select id, name, address from person";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", newID);
                result.updateString("name", fname +" "+ lname);
                result.updateString("address", numb+" "+address);
                result.insertRow();
            }

            query = "Select id from customer";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", newID);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);

                System.out.println("Life successfully added");
            
        } catch (Throwable t) {
            System.out.println("Invalid Input");
        }
    }

    public static void addEmp(Scanner input, Connection con)
    {
        try{
        ResultSet result;
            ResultSet result2;
            int newID = 0;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Please enter the First Name");
            String fname = checkInput(input);
            System.out.println("Please enter the Last Name");
            String lname = checkInput(input);
            System.out.println("Please enter the Street Name (use underscores for spaces)");
            String address = checkInput(input);
            System.out.println("Please enter the Street number");
            String numb = checkInput(input);
            String query = "Select max(id) from person";
            result2 = s.executeQuery(query);
            if (result2.next())
                newID = result2.getInt(1) + 1;
            query = "Select id, name, address from person";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", newID);
                result.updateString("name", fname +" "+ lname);
                result.updateString("address", numb+" "+address);
                result.insertRow();
            }

            query = "Select id from employee";
            result = s.executeQuery(query);
            if (result.next()) {
                result.moveToInsertRow();
                result.updateInt("id", newID);
                result.insertRow();
            }
            query = "Select id,item_id from owns";
            result = s.executeQuery(query);

                System.out.println("Life successfully added");
            
        } catch (Throwable t) {
            System.out.println("Invalid Input");
        }
    }

    public static void addAgent(Scanner input, Connection con)
    {
        try{
            ResultSet result;
            ResultSet result2;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Please enter the employee ID you want to set to Agent.");
        int adjID = input.nextInt();
        System.out.println("Please enter customer ID you want to link with the Agent");
        int custID = input.nextInt();
        boolean correct = false;
        String query = "select * from employee where id ="+adjID;
        result2 = s.executeQuery(query);
        while(!correct)
        {
        if (!result2.next())
        {
            System.out.println("There is no employee with that ID. Please reenter employee ID");
            adjID= input.nextInt();
            query = "select * from employee where id ="+adjID;
            result2 = s.executeQuery(query);

        }
        else
        correct = true;
        }
        correct = false;
        query = "select * from customer where id ="+custID;
        while(!correct)
        {
        if (!result2.next())
        {
            System.out.println("There is no customer with that ID. Please reenter customer ID");
            custID= input.nextInt();
            query = "select * from customer where id ="+custID;
            result2 = s.executeQuery(query);

        }
        else
        correct = true;
        }
        query = "Select id, agent_id from agent";
        result = s.executeQuery(query);
        if (result.next()) {
            result.moveToInsertRow();
            result.updateInt("id", custID);
            result.updateInt("agent_id",adjID);
            result.insertRow();
        }
        result2.close();
        result.close();
        System.out.println("Agent successfully added.");
    }
        catch(Throwable t)
        {
            System.out.println("Invalid Input");
        }
    }


    public static void addAdjuster(Scanner input, Connection con)
    {
        try
        {
            ResultSet result;
            ResultSet result2;
            Statement s = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Please enter the employee ID you want to set to Adjust.");
        int adjID = input.nextInt();
        System.out.println("Please enter claim ID you want to link with the Adjuster");
        int claimID = input.nextInt();
        boolean correct = false;
        String query = "select * from employee where id ="+adjID;
        result2 = s.executeQuery(query);
        while(!correct)
        {
        if (!result2.next())
        {
            System.out.println("There is no claim with that ID. Please reenter employee ID");
            adjID= input.nextInt();
            query = "select * from employee where id ="+adjID;
            result2 = s.executeQuery(query);

        }
        else
        correct = true;
        }
        correct = false;
        query = "select * from claim where claim_id ="+claimID;
        while(!correct)
        {
        if (!result2.next())
        {
            System.out.println("There is no claim with that ID. Please reenter claim ID");
            claimID= input.nextInt();
            query = "select * from customer where claim_id ="+claimID;
            result2 = s.executeQuery(query);

        }
        else
        correct = true;
        }
        query = "Select id, claim_id from adjuster";
        result = s.executeQuery(query);
        if (result.next()) {
            result.moveToInsertRow();
            result.updateInt("claim_id", claimID);
            result.updateInt("adj_id",adjID);
            result.insertRow();
        }
        result2.close();
        result.close();
        System.out.println("Adjuster successfully added.");
        }
        catch(Throwable t)
        {
            System.out.println("Invalid Input");
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
}
