package model;

import gui.Controller0;
import gui.Controller1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class BookManager {
    private DBConnection dbConnection = new DBConnection();
    public Connection cnn;
    public String bookFileURL;
    public String saverURL;

    public BookManager() {
        this.cnn = this.dbConnection.getConnection();
    }

    public ArrayList<Book> selectBook() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.Sach";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selBookList = new ArrayList();
        while (selSet.next()) {
            String bookID = selSet.getString("MaSach");
            String bookName = selSet.getString("TenSach");
            String author = selSet.getString("TacGia");
            String publisher = selSet.getString("NhaXB");
            int publishYear = selSet.getInt("NamXB");
            int price = selSet.getInt("DonGia");
            String intro = selSet.getString("GioiThieu");
            Book b = new Book(bookID, bookName, author, publisher, publishYear, price, intro);
            selBookList.add(b);
        }
        return selBookList;
    }

    public boolean addBook(Book b) {
        String insQuery = "INSERT INTO dbo.Sach VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(insQuery);
            pstm.setString(1, b.getMaSach());
            pstm.setString(2, b.getTenSach());
            pstm.setString(3, b.getTacGia());
            pstm.setString(4, b.getNhaXB());
            pstm.setInt(5, b.getNamXB());
            pstm.setInt(6, b.getDonGia());
            pstm.setString(7, b.getGioiThieu());
            pstm.execute();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book b) {
        String updQuery = "UPDATE dbo.Sach SET MaSach='" + b.getMaSach() + "', TenSach=N'" + b.getTenSach()
                + "', TacGia=N'" + b.getTacGia() + "', NhaXB=N'" + b.getNhaXB() + "', NamXB=" + b.getNamXB()
                + ", DonGia=" + b.getDonGia() + ", GioiThieu=N'" + b.getGioiThieu() + "' WHERE MaSach='"
                + b.getMaSach() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(Book b) {
        String delQuery = "DELETE FROM dbo.Sach WHERE MaSach='" + b.getMaSach() + "'";
        try {
            PreparedStatement pstm = cnn.prepareStatement(delQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Book> insertBookByFile() {
        BufferedReader br = null;
        ArrayList<Book> insertList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(bookFileURL));
            String maSach, tenSach, tagGia, nhaXB, gioiThieu;
            int namXB, donGia;
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                maSach = br.readLine();
                tenSach = br.readLine();
                tagGia = br.readLine();
                nhaXB = br.readLine();
                namXB = Integer.parseInt(br.readLine());
                donGia = Integer.parseInt(br.readLine());
                gioiThieu = br.readLine();
                Book tmp = new Book(maSach, tenSach, tagGia, nhaXB, namXB, donGia, gioiThieu);
                insertList.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return insertList;
        }
    }
}
