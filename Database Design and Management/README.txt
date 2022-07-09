Kieran O'Connor
KJO223
CSE341 FINAL PROJECT

This is the virtual system for the County Gardens Insurance Company.

There are 4 unique interfaces, all accessible from the main interface.
1. Corporate Management
2. Customer
3. Agent
4. Adjuster

I generated data using Mockaroo. 
In hindsight, I should have made the userID's more complex but it also makes it easier to quickly test different users. 

To set up the jar file run the following commands:

    javac *.class

    jar cfmv kjo223.jar Manifest.txt *.class

    java -jar kjo223.jar

Corporate:
To enter corporate you must enter the password, 1. 
Corporate can 
 - view all policies and change their dues
 - view and add customers
 - view all employees
 - view all and edit claims (even open closed claims)

On payment option, total owed to CGI will be calculated.
On policies option, the total value of the policies will be displayed.


Customer:
Customer is by far the most in depth and robust interface, and should be explored the most. 
To access their profiles, customers will have to know their user number.
Customers can
    - pay dues
    - view and add items of all types
    - view, add, and drop their policies of all types
    - file new claims
This may not seem like a lot, but this required lots of code to ensure it met all constraints.


Agent:
To access the agent interface, agents will need to know their id.
Agents can 
- view their customers

Adjuster:
To access their adjuster interface, adjusters will need to know their id.
Adjusters can
 - view their customers
 - view and edit the customers claims (but not ones already closed)


 To test I suggest:
 - Create new customer in Management interface
 - Open that customer's interface
 - Add new items to customer
 - Open policies on those new items
 - file claims on those new policies
 - set adjuster on those claims