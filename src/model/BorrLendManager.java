package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrLendManager {
    public DBConnection connector = new DBConnection();
    public Connection cnn;

    public BorrLendManager() {
        this.cnn = this.connector.getConnection();
    }

    public ArrayList<BorrLend> borrLendSelect() throws SQLException {
        Statement stm = this.cnn.createStatement();
        String selQuery = "SELECT * FROM dbo.MuonTra";
        ResultSet selSet = stm.executeQuery(selQuery);
        ArrayList selBorrLendList = new ArrayList();

        while (selSet.next()) {
            String borrLendID = selSet.getString("MaMT");
            String readerID = selSet.getString("MaDG");
            String librarianID = selSet.getString("MaTT");
            LocalDate borrDate = selSet.getDate("NgayMuon").toLocalDate();
            LocalDate returnDate = selSet.getDate("NgayHenTra").toLocalDate();
            int deposit = selSet.getInt("TienCoc");
            BorrLend tmp = new BorrLend(borrLendID, readerID, librarianID, borrDate, returnDate, deposit);
            selBorrLendList.add(tmp);
        }

        return selBorrLendList;
    }

    public boolean addNewBorrLend(BorrLend tmp) {
        try {
            String insQuery = "INSERT INTO dbo.MuonTra VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaMT());
            pstm.setString(2, tmp.getMaDG());
            pstm.setString(3, tmp.getMaTT());
            pstm.setDate(4, Date.valueOf(tmp.getNgayMuon()));
            pstm.setDate(5, Date.valueOf(tmp.getNgayHenTra()));
            pstm.setInt(6, tmp.getTienCoc());
            pstm.execute();
            return true;
        } catch (SQLException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean updateBorrLend(BorrLend tmp) {
        String updQuery = "UPDATE dbo.MuonTra SET MaMT='" + tmp.getMaMT() + "', MaDG='" + tmp.getMaDG()
                + "', MaTT='" + tmp.getMaTT() + "', NgayMuon=N'" + tmp.getNgayMuon() + "', NgayHenTra='" + tmp.getNgayHenTra()
                + "', TienCoc=" + tmp.getTienCoc() + " WHERE MaMT='"
                + tmp.getMaMT() + "'";
        try {
            PreparedStatement pstm = this.cnn.prepareStatement(updQuery);
            pstm.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
