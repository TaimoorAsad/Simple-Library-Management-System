import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.sqlite.SQLiteException;

import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryManagementSystem {

    JTextField userId, userName, userContact, userBook, bookId, bookTitle, bookGenre, bookauthor, bookstatus;

    JButton userAdd, userDelete, userUpdate, userSearch, bookAdd, bookDelete, bookBorrow, bookReturn, bookSearch;
    JTable userTable, bookTable;
    JFrame frame;
    JLabel userIdLabel, userNameLabel, userContactLabel, userBooksLabel, bookIdLabel, bookTitleLabel, bookGenreLabel, bookauthorLabel, bookStatusLabel;
    ArrayList<User> userList;
    ArrayList<Book> bookList;

    String userHeader[] = new String[] {
            "User ID",
            "User Name",
            "User Contact",
            "Book Issued (ID)"
    };
    String bookHeader[] = new String[] {
        "Book ID",
        "Book Title",
        "Book Genre",
        "Book Author",
        "Book Status"
};
    
    DefaultTableModel bookdtm = new DefaultTableModel(0, 0)
    {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel userdtm = new DefaultTableModel(0, 0)
    {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    Connection conn;
    ResultSet rs;
    int row, col;

    public void mainInterface() {
        frame = new JFrame();

        userIdLabel = new JLabel();
        userIdLabel.setText("User ID: ");
        userIdLabel.setBounds(10, 10, 100, 50);
        frame.add(userIdLabel);

        userId = new JTextField();
        userId.setBounds(100, 25, 250, 25);
        frame.add(userId);


        userNameLabel = new JLabel();
        userNameLabel.setText("User Name: ");
        userNameLabel.setBounds(10, 35, 100, 50);
        frame.add(userNameLabel);

        userName = new JTextField();
        userName.setBounds(100, 50, 250, 25);
        frame.add(userName);

        userContactLabel = new JLabel();
        userContactLabel.setText("User Contact: ");
        userContactLabel.setBounds(10, 58, 100, 50);
        frame.add(userContactLabel);

        userContact = new JTextField();
        userContact.setBounds(100, 75, 250, 25);
        frame.add(userContact);

        userAdd = new JButton();
        userAdd.setText("Add");
        userAdd.setBounds(10, 140, 100, 25);
        frame.add(userAdd);
        userAdd.addActionListener(addUserListener);

        userDelete = new JButton();
        userDelete.setText("Delete");
        userDelete.setBounds(120, 140, 100, 25);
        frame.add(userDelete);
        userDelete.addActionListener(deleteUserListener);


        userSearch = new JButton();
        userSearch.setText("Search");
        userSearch.setBounds(230, 140, 100, 25);
        frame.add(userSearch);
        userSearch.addActionListener(searchUserListener);

        userTable = new JTable();
        userTable.setModel(userdtm);
        userdtm.setColumnIdentifiers(userHeader);
        JScrollPane usersp = new JScrollPane(userTable);
        usersp.setBounds(10, 170, 360, 470);
        frame.add(usersp);

        frame.setSize(960, 700);
        frame.setLayout(null); 
        frame.setVisible(true); 


        bookIdLabel = new JLabel();
        bookIdLabel.setText("Book ID");
        bookIdLabel.setBounds(490, 10, 100, 50);
        frame.add(bookIdLabel);

        bookId = new JTextField();
        bookId.setBounds(580, 25, 250, 25);
        frame.add(bookId);


        bookTitleLabel = new JLabel();
        bookTitleLabel.setText("Book Title");
        bookTitleLabel.setBounds(490, 35, 100, 50);
        frame.add(bookTitleLabel);

        bookTitle = new JTextField();
        bookTitle.setBounds(580, 50, 250, 25);
        frame.add(bookTitle);

        bookGenreLabel = new JLabel();
        bookGenreLabel.setText("Book Genre");
        bookGenreLabel.setBounds(490, 58, 100, 50);
        frame.add(bookGenreLabel);

        bookGenre = new JTextField();
        bookGenre.setBounds(580, 75, 250, 25);
        frame.add(bookGenre);

        bookauthorLabel = new JLabel();
        bookauthorLabel.setText("Book Author");
        bookauthorLabel.setBounds(490, 80, 100, 50);
        frame.add(bookauthorLabel);

        bookauthor = new JTextField();
        bookauthor.setBounds(580, 100, 250, 25);
        bookauthor.setBorder(new JTextField().getBorder());
        frame.add(bookauthor);

        bookAdd = new JButton();
        bookAdd.setText("Add");
        bookAdd.setBounds(405, 140, 100, 25);
        frame.add(bookAdd);
        bookAdd.addActionListener(addBookListener);

        bookDelete = new JButton();
        bookDelete.setText("Delete");
        bookDelete.setBounds(510, 140, 100, 25);
        frame.add(bookDelete);
        bookDelete.addActionListener(deleteBookListener);

        bookBorrow = new JButton();
        bookBorrow.setText("Borrow");
        bookBorrow.setBounds(615, 140, 100, 25);
        frame.add(bookBorrow);
        bookBorrow.addActionListener(borrowBookListener);

        bookReturn = new JButton();
        bookReturn.setText("Return");
        bookReturn.setBounds(720, 140, 100, 25);
        frame.add(bookReturn);
        bookReturn.addActionListener(returnBookListener);

        bookSearch = new JButton();
        bookSearch.setText("Search");
        bookSearch.setBounds(825, 140, 100, 25);
        frame.add(bookSearch);
        bookSearch.addActionListener(searchBookListener);

        bookTable = new JTable();
        bookTable.setModel(bookdtm);
        bookdtm.setColumnIdentifiers(bookHeader);
        JScrollPane booksp = new JScrollPane(bookTable);
        booksp.setBounds(405, 170, 530, 470);
        frame.add(booksp);


        frame.setResizable(false);


    }


    ActionListener addUserListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String userIdString = userId.getText().toString();
            String userNameString = userName.getText().toString();
            String userContactString = userContact.getText().toString();

            if (userIdString.isEmpty() || userNameString.isEmpty() || userContactString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter Complete User Info");
                userName.requestFocus();
            } else {
                int result = JOptionPane.showConfirmDialog(frame, "Insert this food data " + userNameString + "?", "Insert",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate("insert into users (`user_id`, `user_name`, `user_contact`, user_book) VALUES ('" +
                                userIdString + "','" + userNameString + "','" + userContactString + "','" + "None"+"')");
                        loadUserData();
                    } catch (Exception err) {
                        if (err instanceof SQLiteException) {
                            JOptionPane.showMessageDialog(frame, "ID of this User Already Exists");
                        } else {
                            System.out.println(err);
                        }
                    }
                }
            }
        }
    };
    
    ActionListener addBookListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String bookIdString = bookId.getText().toString();
            String bookTitleString = bookTitle.getText().toString();
            String bookGenreString = bookGenre.getText().toString();
            String bookauthorString = bookauthor.getText().toString();

            if (bookIdString.isEmpty() || bookTitleString.isEmpty() || bookGenreString.isEmpty() || bookauthorString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter Complete Book Info");
                bookTitle.requestFocus();
            } else {
                int result = JOptionPane.showConfirmDialog(frame, "Insert this food data " + bookTitleString + "?", "Insert",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate("insert into books (`book_id`, `book_title`, `book_genre`, 'book_author', 'book_status') VALUES ('" +
                                bookIdString + "','" + bookTitleString + "','" + bookGenreString + "','" + bookauthorString + "','"+"Available" +"')");
                        loadBookData();
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                }
            }
        }
    };

    public void loadUserData()  throws SQLException{
        System.out.println("Load User data");
        userList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM Users");
        userList.clear();
        while (rs.next()) {
            userList.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        userdtm.setRowCount(0); 
        for (int i = 0; i < userList.size(); i++) {
            Object[] objs = {
                    userList.get(i).id,
                    userList.get(i).name,
                    userList.get(i).contact,
                    userList.get(i).borrowedBooks,
            };
            userdtm.addRow(objs);
        }
    }

    public void loadBookData()  throws SQLException{
        System.out.println("Load Book data");
        bookList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM books");
        bookList.clear();
        while (rs.next()) {
            bookList.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        bookdtm.setRowCount(0); 
        for (int i = 0; i < bookList.size(); i++) {
            Object[] objs = {
                    bookList.get(i).id,
                    bookList.get(i).title,
                    bookList.get(i).genre,
                    bookList.get(i).author,
                    bookList.get(i).status
            };
            bookdtm.addRow(objs);
        }
    }


    ActionListener deleteUserListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String userIdString = JOptionPane.showInputDialog(frame, "Enter User ID to delete:");
            try{
            if (userIdString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter a User Id to Remove");
                
                }
            else{
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete user with ID " + userIdString + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
                    pstmt.setString(1, userIdString);
                    pstmt.executeUpdate();
                    loadUserData();
            }
        }
    }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
        }
        }
    };

    ActionListener deleteBookListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String bookIdString = JOptionPane.showInputDialog(frame, "Enter Book ID to delete:");
            try{
            if (bookIdString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter a Book ID to Remove");
                
                }
            else{
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete book with ID " + bookIdString + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM books WHERE book_id = ?");
                    pstmt.setString(1, bookIdString);
                    pstmt.executeUpdate();
                    loadBookData();
            }
        }
    }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
        }
        }
    };


    ActionListener borrowBookListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String bookIdString = JOptionPane.showInputDialog(frame, "Enter Book ID to Borrow:");
            try{
            if (bookIdString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter a Book ID to Borrow");
                }
            else{
                String userIdString = JOptionPane.showInputDialog(frame, "Enter User ID of Borrower:");
                if(userIdString.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please Enter a User ID");
                }
                else{
                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to Borrow Book with ID: "+bookIdString +" to user with User ID: "+userIdString+ "?", "Confirm Borrow", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET user_book = ? WHERE user_id = ?");
                        pstmt.setString(1,bookIdString);
                        pstmt.setString(2,userIdString);
                        pstmt.executeUpdate();

                        pstmt = conn.prepareStatement("UPDATE books SET book_status = ? WHERE book_id = ?");
                        pstmt.setString(1,"Borrowed");
                        pstmt.setString(2,bookIdString);
                        pstmt.executeUpdate();


                        loadBookData();
                        loadUserData();
                }
            }
                
                
        }
    }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
        }
        }
    };

    ActionListener returnBookListener = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {
            String bookIdString = JOptionPane.showInputDialog(frame, "Enter Book ID to Return:");
            try{
            if (bookIdString.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please Enter a Book ID to Return");
                }
            else{
                String userIdString = JOptionPane.showInputDialog(frame, "Enter User ID of Returner:");
                if(userIdString.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please Enter a User ID");
                }
                else{
                    int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to Return Book with ID: "+bookIdString +" fom user with User ID: "+userIdString+ "?", "Confirm Return", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET user_book = ? WHERE user_id = ?");
                        pstmt.setString(1,"None");
                        pstmt.setString(2,userIdString);
                        pstmt.executeUpdate();

                        pstmt = conn.prepareStatement("UPDATE books SET book_status = ? WHERE book_id = ?");
                        pstmt.setString(1,"Available");
                        pstmt.setString(2,bookIdString);
                        pstmt.executeUpdate();


                        loadBookData();
                        loadUserData();
                }
            }
                
                
        }
    }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
        }
        }
    };



ActionListener searchUserListener = new ActionListener() {
        
    public void actionPerformed(ActionEvent e) {
        String userIdString = JOptionPane.showInputDialog(frame, "Enter User ID to Search:");
        try{
        if (userIdString.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please Enter a User Id to Search");
            }

        else{
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to Search User with ID " + userIdString + "?", "Confirm Search", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = '" + userIdString + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Searched User is:\n\nID: "+rs.getString(1) +"\nName: "+rs.getString(2) +"\nContact: " + rs.getString(3)+ "\nBook Isued: " + rs.getString(4));
                } else {
                    JOptionPane.showMessageDialog(frame, "No User Found.");
                }
        }
    }
}
    catch (SQLException ex){
        JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
    }
    }
};

ActionListener searchBookListener = new ActionListener() {
        
    public void actionPerformed(ActionEvent e) {
        String bookIdString = JOptionPane.showInputDialog(frame, "Enter Book ID to Search:");
        try{
        if (bookIdString.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please Enter a Book Id to Search");
            }

        else{
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to Search Book with ID " + bookIdString + "?", "Confirm Search", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM books WHERE book_id = '" + bookIdString + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Searched Book is:\n\nID: "+rs.getString(1) +"\nTitle: "+rs.getString(2) +"\nGenre: " + rs.getString(3)+ "\nTitle: " + rs.getString(4)+ "\nStatus: " + rs.getString(5));
                } else {
                    JOptionPane.showMessageDialog(frame, "No Book Found.");
                }
        }
    }
}
    catch (SQLException ex){
        JOptionPane.showMessageDialog(frame, "Error" + ex.getMessage());
    }
    }
};
}





