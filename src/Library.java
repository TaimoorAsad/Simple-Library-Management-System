import java.sql.DriverManager;

public class Library {

    public static void main(String[] args) throws Exception {
        // static Connection conn;
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:data.db";
        LibraryManagementSystem app = new LibraryManagementSystem();
        app.conn = DriverManager.getConnection(url);
        app.mainInterface();
        app.loadBookData();
        app.loadUserData();
    }
    
}
