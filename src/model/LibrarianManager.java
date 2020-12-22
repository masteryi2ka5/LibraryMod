package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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

    public ArrayList<Librarian> insertLibrarianByFile() {
        BufferedReader br = null;
        ArrayList<Librarian> insertList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(librarianFileURL));
            String maTT, tenTT, gioiTinh, CMND, email, dienThoai;
            LocalDate ngaySinh;
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                maTT = br.readLine();
                tenTT = br.readLine();
                gioiTinh = br.readLine();
                ngaySinh = LocalDate.parse(br.readLine());
                CMND = br.readLine();
                email = br.readLine();
                dienThoai = br.readLine();
                Librarian tmp = new Librarian(maTT, tenTT, gioiTinh, ngaySinh, CMND, email, dienThoai);
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
