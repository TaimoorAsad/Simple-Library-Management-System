// Main class that impliments all the methods in Library class

import java.sql.DriverManager;

public class LibraryManagementSystem {

    public static void main(String[] args) throws Exception {
        // establishing connection to sqlite database
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:data.db";
        Library app = new Library();
        app.conn = DriverManager.getConnection(url);
        app.mainInterface();
        app.loadBookData();
        app.loadUserData();
    }
    
}
