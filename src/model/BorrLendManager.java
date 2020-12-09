package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
