package model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class LibrarianManager {
    private DBConnection dbConnection = new DBConnection();
    public Connection cnn;
    public String librarianFileURL;
    public String saverURL;

    public LibrarianManager() {
        this.cnn = this.dbConnection.getConnection();
    }

    public ArrayList<Librarian> selectLibrarian() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.ThuThu";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selLibrarianList = new ArrayList();

        while (selSet.next()) {
            String maTT = selSet.getString("MaTT");
            String tenTT = selSet.getString("TenTT");
            String gioiTinh = selSet.getString("GioiTinh");
            LocalDate ngaySinh = selSet.getDate("NgaySinh").toLocalDate();
            String CMND = selSet.getString("CMND");
            String email = selSet.getString("Email");
            String dienThoai = selSet.getString("DienThoai");
            Librarian l = new Librarian(maTT, tenTT, gioiTinh, ngaySinh, CMND, email, dienThoai);
            selLibrarianList.add(l);
        }

        return selLibrarianList;
    }

    public boolean addLibrarian(Librarian l) {
        String insQuery = "INSERT INTO dbo.ThuThu VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(insQuery);
            pstm.setString(1, l.getMaTT());
            pstm.setString(2, l.getTenTT());
            pstm.setString(3, l.getGioiTinh());
            pstm.setDate(4, Date.valueOf(l.getNgaySinh()));
            pstm.setString(5, l.getCMND());
            pstm.setString(6, l.getEmail());
            pstm.setString(7, l.getDienThoai());
            pstm.execute();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean updateLibrarian(Librarian b) {
        String updQuery = "UPDATE dbo.ThuThu SET MaTT='" + b.getMaTT() + "', TenTT=N'" + b.getTenTT()
                + "', GioiTinh=N'" + b.getGioiTinh() + "', NgaySinh='" + b.getNgaySinh().toString() + "', CMND='" + b.getCMND()
                + "', Email='" + b.getEmail() + "', DienThoai='" + b.getDienThoai() + "' WHERE MaTT='"
                + b.getMaTT() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteLibrarian(Librarian b) {
        String delQuery = "DELETE FROM dbo.ThuThu WHERE maTT='" + b.getMaTT() + "'";
        try {
            PreparedStatement pstm = cnn.prepareStatement(delQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Librarian> insertLibrarianByFile() throws IOException {
        ArrayList<Librarian> insertList = new ArrayList<>();
        FileInputStream file = new FileInputStream(librarianFileURL);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int R = sheet.getLastRowNum();
        for (int i = 1; i <= R; i++) {
            Librarian tmpLib = new Librarian(NumberToTextConverter.toText(sheet.getRow(i).getCell(0).getNumericCellValue()), sheet.getRow(i).getCell(1).toString(),
                    sheet.getRow(i).getCell(2).toString(),
                    (sheet.getRow(i).getCell(3).getDateCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    sheet.getRow(i).getCell(4).toString(), sheet.getRow(i).getCell(5).toString(),
                    sheet.getRow(i).getCell(6).toString());
            insertList.add(tmpLib);

        }
        workbook.close();
        return insertList;
    }
}
