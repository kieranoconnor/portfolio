import java.sql.*;
import java.util.Scanner;
import java.io.*;
import java.rmi.ConnectException;

public class kjo223 {
    public static void main(String[] args) throws SQLException, IOException, java.lang.ClassNotFoundException {
        Connection con = null;
        try {

            Scanner keyboard = new Scanner(System.in);
            boolean correctLogin = false;

            while (!correctLogin) {
                System.out.println("Please Enter Oracle User Id:");
                String userID = keyboard.next();
                System.out.println("\nPlease Enter Oracle Password:");
                String userPswd = keyboard.next();
                correctLogin = true;
                try {
                    con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", userID,
                            userPswd);
                } catch (Exception e) {
                    System.out.println("\nIncorrect username or password\n\n");
                    correctLogin = false;
                }
            }
            System.out.println("\n**Connection made successfully**");

            do {

                System.out.println("\n\nWelcome to Country Gardens Insurance Virtual Interface");
                System.out.println("-------------------------------------------------------");
                System.out.println("\nPlease select which interface you would like to enter:\n");

                System.out.println("1 - Corporate Management\n2 - Customer\n3 - Agent\n4 - Adjuster\n5 - quit");
                int choiceMode = keyboard.nextInt();

                switch (choiceMode) {
                    case 1:
                        System.out.println("You chose Corporate");
                        management.mgmtInterface(con);
                        break;

                    case 2:
                        System.out.println("You chose Customer");
                        customer.customerInterface(con);
                        break;

                    case 3:
                        System.out.println("You chose Agent");
                        agent.agentInterface(con);
                        break;

                    case 4:
                        System.out.println("You chose Adjuster");
                        adjuster.adjusterInterface(con);
                        break;

                    case 5:
                        System.out.println("You chose to quit. Good Choice.");
                        correctLogin = true;
                        break;
                    default:
                        System.out.println("Please only enter a number 1-5");
                        correctLogin = false;
                        break;

                }

            } while (!correctLogin);
            con.close();
        } catch (Throwable e) {

        } finally {
            con.close();

        }

    }
}
