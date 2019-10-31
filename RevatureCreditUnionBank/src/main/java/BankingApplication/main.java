package BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;

public class main {
	
    String userId = "";
    String password = "";
	private static Scanner sc;

    public static void main(final String[] args) {
    	
        mainclass();
        
    }
    public static void mainclass()
    {
        
        sc = new Scanner(System.in);
        System.out.println("Welcome To Revature Credit Union Banking Application");
        System.out.println("1. User Login ");
        System.out.println("2. Admin Login");
        System.out.println("3. Exit");
        
        System.out.print("Input the value for select: ");
        int uorad = sc.nextInt();
        if(uorad==1)
        {
            user();
        }
        else if(uorad ==2)
        {
            admin();
        }
        else if(uorad ==3)
        {
            exit();
        }
        else
        {
             System.out.println("Wrong input. Plase try again");
             mainclass();
        }
    }
    private static void exit() {
		// TODO Auto-generated method stub
		
	}
	public static void user()
    {
        sc = new Scanner(System.in);
        System.out.println("1. Already Have Account");
        System.out.println("2. Create New Account");
        System.out.print("Select option: ");
            int userOp = sc.nextInt();
            if(userOp ==1)
            {
                System.out.print("Enter account ID:");
                String id = sc.next();
                System.out.print("Enter your Password: ");
                String pass = sc.next();
                try
                {
                    Connection con = SQLConnection.getConnection();
                    String sql = "Select * From user Where sl=? AND password=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, pass);
                    ResultSet rs = pst.executeQuery();
                    
                    if(rs.next())
                    {
                        System.out.println("\n");
                        System.out.println("Welcome "+rs.getString(2));
                        System.out.println("Your Balance "+rs.getString(5));
                        showUserOptions(id,pass);
                    }
                    else
                    {
                        System.out.println("User Not found!");
                        user();
                    }
                
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            else if(userOp ==2)
            {
                System.out.println("Enter Your Name: ");
                String name = sc.next();
                System.out.println("Enter Your Age(only age value): ");
                int age = sc.nextInt();
                System.out.println("Enter Your password: ");
                String pass = sc.next();
                
                try
                {
                    Random ran = new Random();
                    int n = ran.nextInt(1000);
                    String val = String.valueOf(n);
                    
                    double balance = 0.00;
                    
                    Connection con = SQLConnection.getConnection();
                    String sql = "INSERT INTO user (sl,name, age, password, balance) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, val);
                    pst.setString(2, name);
                    pst.setInt(3, age);
                    pst.setString(4, pass);
                    pst.setDouble(5, balance);
                    
                    if(pst.executeUpdate() > 0)
                    {
                        System.out.println("New account created");
                        System.out.println("Your account ID: "+val+" passowrd: "+pass+"\n");
                        mainclass();
                    }
                con.close();
                }
                catch(Exception e)
                {
                   System.out.println(e);
                }
                
            }
            else
            {
                user();
            }
    }
    public static void showUserOptions(String id, String pass) // showing user panel
    {
        String ID = id;
        String password = pass;
        sc = new Scanner(System.in);
        try
        {
            System.out.println("1. Show my account info");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction");
            System.out.println("5. Delete Account");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.print("select option:");
                            
            int usrOp = sc.nextInt();
            if(usrOp == 1)
            {
                showAccoutDetails(ID,password);
            }
            else if(usrOp == 2)
            {
                depositAccount(ID,password);
            }
            else if(usrOp == 3)
            {
                withdrawAccount(ID,password);
            }
            else if(usrOp == 4)
            {
                showUserTransaction(ID, password);
            }
            else if(usrOp == 5)
            {
                deleteUser(ID);
            }
            else if(usrOp == 6)
            {
                mainclass();
            }
            else if(usrOp == 7)
            {
                exit();
            }
            else
            {
                showUserOptions(ID, password);
            }
        }
        catch(Exception e)
        {
            System.out.println("Connection Error: "+e);
        }
    }
    public static void showAccoutDetails(String ID, String password) // show user account profile
    {
        String id = ID;
        String pass = password;
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "Select * From user Where sl=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
                    
            if(rs.next())
            {
                System.out.println("Account ID: "+rs.getString(1));
                System.out.println("Name: "+rs.getString(2));
                System.out.println("Age: "+rs.getString(3));
                System.out.println("Balance: "+rs.getDouble(5));
                showUserOptions(id,pass);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static void depositAccount(String ID, String password) // deposit ammount
    {
        sc = new Scanner(System.in);
        String id = ID;
        String pass = password;
        
        System.out.print("Enter ammount to Deposit: ");
        double balance = sc.nextDouble();
        
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "UPDATE user SET balance=? WHERE sl="+id;
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDouble(1, balance);
            
            if(pst.executeUpdate() > 0)
            {
                System.out.println("Successfully added "+balance + " tk.\n");
                try     // save database the transaction 
                {
                    String transctype = "Deposit";
                    String sql1 = "INSERT INTO transaction (userid, transtype, transbalance) VALUES (?, ?, ?)";
                    PreparedStatement pst1 = con.prepareStatement(sql1);
                    pst1.setString(1, id);
                    pst1.setString(2, transctype);
                    pst1.setDouble(3, balance);
                    
                    if(pst1.executeUpdate() > 0)
                    {
                        System.out.println("Transaction added.\n");
                        showUserOptions(id,pass);
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    public static void withdrawAccount(String ID, String password) // withdraw ammount
    {
        sc = new Scanner(System.in);
        String id = ID;
        String pass = password;
        double accountbal = 0.00;
        System.out.print("Enter ammount to Withdraw: ");
        double balance = sc.nextDouble();
        
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "Select * From user Where sl=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
                    
            if(rs.next())
            {
                accountbal = rs.getDouble(5);
                if(balance < accountbal)
                {
                    accountbal = accountbal - balance;
                    try
                    {
                        String sql1 = "UPDATE user SET balance=? WHERE sl="+id;
                        PreparedStatement pst1 = con.prepareStatement(sql1);
                        pst1.setDouble(1, accountbal);
                        
                        if(pst1.executeUpdate() > 0)
                        {
                            System.out.println("Successfully Withdraw "+balance + " tk.\n");
                            try     // save database the transaction 
                            {
                                String transctype = "Withdraw";
                                String sql2 = "INSERT INTO transaction (userid, transtype, transbalance) VALUES (?, ?, ?)";
                                PreparedStatement pst2 = con.prepareStatement(sql2);
                                pst2.setString(1, id);
                                pst2.setString(2, transctype);
                                pst2.setDouble(3, balance);

                                if(pst2.executeUpdate() > 0)
                                {
                                    System.out.println("Transaction added.\n");
                                    showUserOptions(id,pass);
                                }
                            }
                            catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            
                        }
                        con.close();
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
                else
                {
                    System.out.println("You haven't Enougn Balance!!\n");
                    showUserOptions(id,pass);
                }
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    public static void showUserTransaction(String ID, String password) // view transaction
    {
        String id = ID;
        String pass = password;
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "Select * From transaction where userid="+id;
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int i = 1;
            while(rs.next())
            {
                System.out.println(i+": Transaction Type: "+rs.getString(3)+", Ammount: "+rs.getString(4)+"\n"); 
                i++;
            }
            showUserOptions(id,pass);
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static void deleteUser(String ID) // Delete user account
    {
        String id = ID;
        
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "DELETE FROM user WHERE sl="+id;
            PreparedStatement pst = con.prepareStatement(sql);
            if(pst.executeUpdate() > 0)
            {
                System.out.println("....Account Delete.....\n");
                mainclass();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    //admin panel start
    public static void admin()
    {
        sc = new Scanner(System.in);
        System.out.println("1. Already Have Account");
        System.out.println("2. Create New Admin");
        System.out.println("3. Exit");
        System.out.print("Select option: ");
        
        int adminOp = sc.nextInt();
        if(adminOp == 1)
        {
            System.out.print("Enter account ID:");
                String id = sc.next();
                System.out.print("Enter your Password: ");
                String pass = sc.next();
                try
                {
                    Connection con = SQLConnection.getConnection();
                    String sql = "Select * From admin Where sl=? AND password=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, id);
                    pst.setString(2, pass);
                    ResultSet rs = pst.executeQuery();
                    
                    if(rs.next())
                    {
                        System.out.println("\n");
                        System.out.println("Welcome "+rs.getString(2));
                        showAdminOptions(id,pass);
                    }
                    else
                    {
                        System.out.println("User Not found!");
                        admin();
                    }
                
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
        }
        else if(adminOp == 2)
        {
            System.out.print("Enter Your Name: ");
            String name = sc.next();
            System.out.print("Enter Your password: ");
            String pass = sc.next();
                
            try
                {
                    Random ran = new Random();
                    int n = ran.nextInt(1000);
                    String val = String.valueOf(n);
                    
                    Connection con = SQLConnection.getConnection();
                    String sql = "INSERT INTO admin (sl,name,password) VALUES (?, ?, ?)";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, val);
                    pst.setString(2, name);
                    pst.setString(3, pass);
                    
                    if(pst.executeUpdate() > 0)
                    {
                        System.out.println("New Admin created");
                        System.out.println("Your account ID: "+val+" passowrd: "+pass+"\n");
                        admin();
                    }
                con.close();
                }
                catch(Exception e)
                {
                   System.out.println(e);
                }
        }
        else if(adminOp == 3)
        {
            exit();
        }
        
    }
    public static void showAdminOptions(String id, String pass) // admin panel show
    {
        sc = new Scanner(System.in);
        String ID = id;
        String password = pass;
        
        System.out.println("1. Show all User");
        System.out.println("2. Show all Account");
        System.out.println("3. Logout");
        System.out.println("4. exit");
        System.out.print("Select option: ");
        
        int adminOp = sc.nextInt();
        
        if(adminOp == 1)
        {
            showAllUser(id, pass);
        }
        else if(adminOp == 2)
        {
            showAllAccount(id, pass);
        }
        else if(adminOp == 3)
        {
            mainclass(); //go to mainclass()
        }
        else if(adminOp == 4)
        {
            exit();
        }
        else
        {
            showAdminOptions(ID, password);
        }
        
    }
    public static void showAllUser(String id, String pass) // show all user
    {
        String ID = id;
        String password = pass;
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "Select name From user";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int i =1;  
            while(rs.next())
            {
                System.out.println(i+": Name: "+rs.getString(1)); 
                i++;
            }
            showAllAccount(ID, password);
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static void showAllAccount(String id, String pass) // show all account
    {
        String ID = id;
        String password = pass;
        try
        {
            Connection con = SQLConnection.getConnection();
            String sql = "Select * From user";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int i = 1;
            while(rs.next())
            {
                System.out.println(i+": ID: "+rs.getString(1)+ ", Name: "+rs.getString(2)+", age: "+rs.getString(3)+", pass: "+rs.getString(4)+", Balance: "+rs.getString(5)); 
                i++;
            }
            showAllAccount(ID, password);
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
}
