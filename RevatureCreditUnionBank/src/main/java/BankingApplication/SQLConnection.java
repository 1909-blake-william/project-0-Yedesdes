package BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
	 
	  
		 public static Connection getConnection() throws SQLException {
				String url = System.getenv("REVATURE_CREDIT_UNION_DB_USERNAM");
				String username = System.getenv("RevatureCreditUnion");
				String password = System.getenv("RCUPass");
				return DriverManager.getConnection(url, username, password);
			}

	        
	        
	    }

