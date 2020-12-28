package model;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
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

    public ArrayList<Book> insertBookByFile() throws IOException {
        ArrayList<Book> insertList = new ArrayList<>();
        FileInputStream file = new FileInputStream(bookFileURL);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int R = sheet.getLastRowNum();
        for (int i = 1; i <= R; i++) {
            Book tmpBook = new Book(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString(),
                    sheet.getRow(i).getCell(2).toString(), sheet.getRow(i).getCell(3).toString(),
                    (int)sheet.getRow(i).getCell(4).getNumericCellValue(), (int)sheet.getRow(i).getCell(5).getNumericCellValue(),
                    sheet.getRow(i).getCell(6).toString());
            insertList.add(tmpBook);
        }
        workbook.close();
        return insertList;
    }
}
