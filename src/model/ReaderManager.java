package model;

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

public class ReaderManager {
    private DBConnection dbConnection = new DBConnection();
    public Connection cnn;
    public String readerFileURL;
    public String saverURL;

    public ReaderManager() {
        this.cnn = this.dbConnection.getConnection();
    }

    public ArrayList<Reader> selectReader() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.DocGia";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selReaderList = new ArrayList();

        while (selSet.next()) {
            String maDG = selSet.getString("MaDG");
            String tenDG = selSet.getString("TenDG");
            String gioiTinh = selSet.getString("GioiTinh");
            LocalDate ngaySinh = selSet.getDate("NgaySinh").toLocalDate();
            String CMND = selSet.getString("CMND");
            String email = selSet.getString("Email");
            String dienThoai = selSet.getString("DienThoai");
            Reader l = new Reader(maDG, tenDG, gioiTinh, ngaySinh, CMND, email, dienThoai);
            selReaderList.add(l);
        }

        return selReaderList;
    }

    public boolean addReader(Reader r) {
        String insQuery = "INSERT INTO dbo.DocGia VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(insQuery);
            pstm.setString(1, r.getMaDG());
            pstm.setString(2, r.getTenDG());
            pstm.setString(3, r.getGioiTinh());
            pstm.setDate(4, Date.valueOf(r.getNgaySinh()));
            pstm.setString(5, r.getCMND());
            pstm.setString(6, r.getEmail());
            pstm.setString(7, r.getDienThoai());
            pstm.execute();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean updateReader(Reader b) {
        String updQuery = "UPDATE dbo.DocGia SET MaDG='" + b.getMaDG() + "', TenDG=N'" + b.getTenDG()
                + "', GioiTinh=N'" + b.getGioiTinh() + "', NgaySinh='" + b.getNgaySinh().toString() + "', CMND='" + b.getCMND()
                + "', Email='" + b.getEmail() + "', DienThoai='" + b.getDienThoai() + "' WHERE MaDG='"
                + b.getMaDG() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteReader(Reader b) {
        String delQuery = "DELETE FROM dbo.DocGia WHERE maDG='" + b.getMaDG() + "'";
        try {
            PreparedStatement pstm = cnn.prepareStatement(delQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Reader> insertReaderByFile() throws IOException {
        ArrayList<Reader> insertList = new ArrayList<>();
        FileInputStream file = new FileInputStream(readerFileURL);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int R = sheet.getLastRowNum();
        for (int i = 1; i <= R; i++) {
            Reader tmpReader = new Reader(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString(),
                    sheet.getRow(i).getCell(2).toString(), (sheet.getRow(i).getCell(3).getDateCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    sheet.getRow(i).getCell(4).toString(), sheet.getRow(i).getCell(5).toString(),
                    sheet.getRow(i).getCell(6).toString());
            insertList.add(tmpReader);

        }
        workbook.close();
        return insertList;
    }
}
