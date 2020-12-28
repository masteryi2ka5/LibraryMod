package model;

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

public class DetailManager {
    private DBConnection dbConnection = new DBConnection();
    public Connection cnn;
    public String detailFileURL;
    public String saverURL;

    public DetailManager() {
        this.cnn = this.dbConnection.getConnection();
    }

    public ArrayList<Detail> selectDetail() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.ChiTietMuonTra";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selDetailList = new ArrayList();

        while (selSet.next()) {
            String maMT = selSet.getString("MaMT");
            String maSach = selSet.getString("MaSach");
            LocalDate ngayTra = selSet.getDate("NgayTra").toLocalDate();
            String trangThai = selSet.getString("TrangThaiSach");
            int tienPhat = selSet.getInt("TienPhat");
            String ghiChu = selSet.getString("GhiChu");
            Detail d = new Detail(maMT, maSach, ngayTra, trangThai, tienPhat, ghiChu);
            selDetailList.add(d);
        }

        return selDetailList;
    }

    public boolean addDetail(Detail d) {
        String insQuery = "INSERT INTO dbo.ChiTietMuonTra VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(insQuery);
            pstm.setString(1, d.getMaMT());
            pstm.setString(2, d.getMaSach());
            pstm.setDate(3, Date.valueOf(d.getNgayTra()));
            pstm.setString(4, d.getTrangThai());
            pstm.setInt(5, d.getTienPhat());
            pstm.setString(6, d.getGhiChu());
            pstm.execute();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean updateDetail(Detail d) {
        String updQuery = "UPDATE dbo.ChiTietMuonTra SET MaMT='" + d.getMaMT() + "', MaSach='" + d.getMaSach()
                + "', NgayTra='" + d.getNgayTra().toString() + "', TienPhat=" + d.getTienPhat()
                + ", TrangThaiSach=N'" + d.getTrangThai() + "', GhiChu=N'" + d.getGhiChu() + "' WHERE MaMT='"
                + d.getMaMT() + "' AND MaSach='" + d.getMaSach() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean deleteDetail(Detail d) {
        String delQuery = "DELETE FROM dbo.ChiTietMuonTra WHERE maMT='" + d.getMaMT() + "' AND maSach='" + d.getMaSach() + "'";
        try {
            PreparedStatement pstm = cnn.prepareStatement(delQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Detail> insertDetailByFile() throws IOException {
        ArrayList<Detail> insertList = new ArrayList<>();
        FileInputStream file = new FileInputStream(detailFileURL);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int R = sheet.getLastRowNum();
        for (int i = 1; i <= R; i++) {
            Detail tmpDetail = new Detail(sheet.getRow(i).getCell(0).toString(), sheet.getRow(i).getCell(1).toString(),
                    (sheet.getRow(i).getCell(2).getDateCellValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    sheet.getRow(i).getCell(3).toString(), (int)sheet.getRow(i).getCell(4).getNumericCellValue(),
                    sheet.getRow(i).getCell(5).toString());
            insertList.add(tmpDetail);

        }
        workbook.close();
        return insertList;
    }
}
