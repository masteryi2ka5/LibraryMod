package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
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

    public boolean addReader(Reader l) {
        String insQuery = "INSERT INTO dbo.ThuThu VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(insQuery);
            pstm.setString(1, l.getMaDG());
            pstm.setString(2, l.getTenDG());
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
        String delQuery = "DELETE FROM dbo.ThuThu WHERE maDG='" + b.getMaDG() + "'";
        try {
            PreparedStatement pstm = cnn.prepareStatement(delQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Reader> insertReaderByFile() {
        BufferedReader br = null;
        ArrayList<Reader> insertList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(readerFileURL));
            String maDG, tenDG, gioiTinh, CMND, email, dienThoai;
            LocalDate ngaySinh;
            int n = Integer.parseInt(br.readLine());
            for (int i = 0; i < n; i++) {
                maDG = br.readLine();
                tenDG = br.readLine();
                gioiTinh = br.readLine();
                ngaySinh = LocalDate.parse(br.readLine());
                CMND = br.readLine();
                email = br.readLine();
                dienThoai = br.readLine();
                Reader tmp = new Reader(maDG, tenDG, gioiTinh, ngaySinh, CMND, email, dienThoai);
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
