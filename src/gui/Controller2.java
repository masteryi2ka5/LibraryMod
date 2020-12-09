package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BorrLend;
import model.BorrLendManager;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Controller2 implements Initializable {

    @FXML
    private TableView<BorrLend> tableViewBorrLend = new TableView();
    @FXML
    private TableColumn<BorrLend, String> maMT = new TableColumn();
    @FXML
    private TableColumn<BorrLend, String> maDG = new TableColumn();
    @FXML
    private TableColumn<BorrLend, String> maTT = new TableColumn();
    @FXML
    private TableColumn<BorrLend, LocalDate> ngayMuon = new TableColumn();
    @FXML
    private TableColumn<BorrLend, LocalDate> ngayHenTra = new TableColumn();
    @FXML
    private TableColumn<BorrLend, Integer> tienCoc = new TableColumn();
    @FXML
    private TextField borrLendID = new TextField();
    @FXML
    private TextField readerID = new TextField();
    @FXML
    private TextField librarianID = new TextField();
    @FXML
    private DatePicker borrDate = new DatePicker();
    @FXML
    private DatePicker returnDate = new DatePicker();
    @FXML
    private TextField deposit = new TextField();
    @FXML
    private TextField borrLendKeyword = new TextField();

    private ArrayList<BorrLend> selBorrLendList;
    private ObservableList<BorrLend> borrLendList = FXCollections.observableArrayList();
    private ObservableList<BorrLend> borrLendSearchList = FXCollections.observableArrayList();
    private BorrLendManager borrLendManager = new BorrLendManager();
    private BorrLend mainBorrLend = new BorrLend();

    public Controller2() {
    }

    public void showWindow0(ActionEvent event) {
        clearBorrLendInfo();
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    public void search() {
        borrLendList.clear();
        String keyword = borrLendKeyword.getText();
        for (int i = 0; i < borrLendList.size(); i++) {
            BorrLend tmp = borrLendList.get(i);
            if (tmp.getMaTT().contains(keyword) || tmp.getMaMT().contains(keyword)
                    || tmp.getMaDG().contains(keyword) || tmp.getMaTT().contains(keyword)
                    || String.valueOf(tmp.getTienCoc()).contains(keyword))
                borrLendSearchList.add(tmp);
        }
        if (borrLendSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateBorrLendTable();
        }
        else {
            Collections.sort(borrLendSearchList);
            maMT.setCellValueFactory(new PropertyValueFactory("maMT"));
            maDG.setCellValueFactory(new PropertyValueFactory("maDG"));
            maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
            ngayMuon.setCellValueFactory(new PropertyValueFactory("ngayMuon"));
            ngayHenTra.setCellValueFactory(new PropertyValueFactory("ngayHenTra"));
            tienCoc.setCellValueFactory(new PropertyValueFactory("tienCoc"));
        }
        borrLendKeyword.setText("");
    }

    public void addNewBorrLend(ActionEvent event) throws SQLException {
        BorrLend tmp = this.getBorrLendInfo();

        try {
            String insQuery = "INSERT INTO dbo.MuonTra VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = this.borrLendManager.cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaMT());
            pstm.setString(2, tmp.getMaDG());
            pstm.setString(3, tmp.getMaTT());
            pstm.setDate(4, Date.valueOf(tmp.getNgayMuon()));
            pstm.setDate(5, Date.valueOf(tmp.getNgayHenTra()));
            pstm.setInt(6, tmp.getTienCoc());
            pstm.execute();
            this.borrLendList.add(tmp);
            this.tableViewBorrLend.setItems(this.borrLendList);
            (new Controller0()).setAlert("Thêm thành công!");
        } catch (SQLException var5) {
            var5.printStackTrace();
            (new Controller0()).setAlert("Thêm thất bại!");
        }

        this.clearBorrLendInfo();
    }

    BorrLend getBorrLendInfo() {
        String ma_muon_tra = this.borrLendID.getText();
        String ma_doc_gia = this.readerID.getText();
        String ma_thu_thu = this.librarianID.getText();
        LocalDate ngay_muon = this.borrDate.getValue();
        LocalDate ngay_hen_tra = this.returnDate.getValue();
        int tien_coc = Integer.parseInt(this.deposit.getText());
        BorrLend tmp = new BorrLend(ma_muon_tra, ma_doc_gia, ma_thu_thu, ngay_muon, ngay_hen_tra, tien_coc);
        return tmp;
    }

    void clearBorrLendInfo() {
        this.borrDate.setValue(null);
        this.returnDate.setValue(null);
        this.borrLendID.setText("");
        this.readerID.setText("");
        this.librarianID.setText("");
        this.deposit.setText("");
    }

    void updateBorrLendTable() {
        Collections.sort(borrLendList);
        maMT.setCellValueFactory(new PropertyValueFactory("maMT"));
        maDG.setCellValueFactory(new PropertyValueFactory("maDG"));
        maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
        ngayMuon.setCellValueFactory(new PropertyValueFactory("ngayMuon"));
        ngayHenTra.setCellValueFactory(new PropertyValueFactory("ngayHenTra"));
        tienCoc.setCellValueFactory(new PropertyValueFactory("tienCoc"));
        tableViewBorrLend.setItems(this.borrLendList);
    }

    public void getSelectedBorrLend() {
        tableViewBorrLend.setOnMouseClicked(e -> {
            mainBorrLend = tableViewBorrLend.getSelectionModel().getSelectedItem();
            borrLendID.setText(mainBorrLend.getMaMT());
            readerID.setText(mainBorrLend.getMaDG());
            librarianID.setText(mainBorrLend.getMaTT());
            borrDate.setValue(mainBorrLend.getNgayMuon());
            returnDate.setValue(mainBorrLend.getNgayHenTra());
            deposit.setText(String.valueOf(mainBorrLend.getTienCoc()));
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selBorrLendList = borrLendManager.borrLendSelect();
            var3 = selBorrLendList.iterator();
            while (var3.hasNext()) {
                BorrLend b = (BorrLend) var3.next();
                borrLendList.add(b);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateBorrLendTable();
        getSelectedBorrLend();
    }
}