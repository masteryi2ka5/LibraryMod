package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookManager {
    public DBConnection connector = new DBConnection();
    public Connection cnn;

    public BookManager() {
        this.cnn = this.connector.getConnection();
    }

    public ArrayList<Book> bookSelect() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.Sach";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selBookList = new ArrayList();

        while(selSet.next()) {
            String bookID = selSet.getString("MaSach");
            String bookName = selSet.getString("TenSach");
            String author = selSet.getString("TacGia");
            String publisher = selSet.getString("NhaXB");
            int publishYear = selSet.getInt("NamXB");
            int price = selSet.getInt("DonGia");
            int quantity = selSet.getInt("SoLuong");
            Book tmp = new Book(bookID, bookName, author, publisher, publishYear, price, quantity);
            selBookList.add(tmp);
        }

        return selBookList;
    }
}
