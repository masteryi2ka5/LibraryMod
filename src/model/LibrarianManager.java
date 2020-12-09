package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class LibrarianManager {
    private DBConnection dbConnection = new DBConnection();
    public Connection cnn;

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
        String updQuery = "UPDATE dbo.ThuThu SET maTT='" + b.getMaTT() + "', tenTT=N'" + b.getTenTT()
                + "', gioiTinh=N'" + b.getGioiTinh() + "', ngaySinh='" + b.getNgaySinh().toString() + "', CMND='" + b.getCMND()
                + "', email='" + b.getEmail() + "', dienThoai='" + b.getDienThoai() + "' WHERE maTT='"
                + b.getMaTT() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            System.out.println(b.getNgaySinh().toString());
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
}
