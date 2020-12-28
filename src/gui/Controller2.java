package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
    private ComboBox readerID = new ComboBox();
    @FXML
    private ComboBox librarianID = new ComboBox();
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

    public void addNewBorrLend() throws SQLException {
        BorrLend tmp = this.getBorrLendInfo();
        if (borrLendManager.addNewBorrLend(tmp)) {
            this.borrLendList.add(tmp);
            this.tableViewBorrLend.setItems(this.borrLendList);
            (new Controller0()).setAlert("Thêm thành công!");
        }
        else {
            (new Controller0()).setAlert("Thêm thất bại!");
        }
        this.clearBorrLendInfo();
    }

    BorrLend getBorrLendInfo() {
        String ma_muon_tra = this.borrLendID.getText();
        String ma_doc_gia = this.readerID.getSelectionModel().selectedItemProperty().getValue().toString();
        String ma_thu_thu = this.librarianID.getSelectionModel().selectedItemProperty().getValue().toString();
        LocalDate ngay_muon = this.borrDate.getValue();
        LocalDate ngay_hen_tra = this.returnDate.getValue();
        int tien_coc = Integer.parseInt(this.deposit.getText());
        BorrLend tmp = new BorrLend(ma_muon_tra, ma_doc_gia, ma_thu_thu, ngay_muon, ngay_hen_tra, tien_coc);
        return tmp;
    }

    public void updateBorrLend() {
        BorrLend tmp = getBorrLendInfo();
        if (borrLendManager.updateBorrLend(tmp)) {
            int i = 0;
            while (!borrLendList.get(i).getMaMT().equals(tmp.getMaMT()))
                i++;
            borrLendList.set(i, tmp);
            updateBorrLendTable();
            clearBorrLendInfo();
            (new Controller0()).setAlert("Sửa thành công!");
        } else
            (new Controller0()).setAlert("Sửa thất bại");
    }

    void clearBorrLendInfo() {
        this.borrDate.setValue(null);
        this.returnDate.setValue(null);
        this.borrLendID.setText("");
        this.readerID.getSelectionModel().clearSelection();
        this.librarianID.getSelectionModel().clearSelection();
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
            readerID.setValue(mainBorrLend.getMaDG());
            librarianID.setValue(mainBorrLend.getMaTT());
            borrDate.setValue(mainBorrLend.getNgayMuon());
            returnDate.setValue(mainBorrLend.getNgayHenTra());
            deposit.setText(String.valueOf(mainBorrLend.getTienCoc()));
        });
    }

    public void printBorrLendPatern() throws IOException {
        ArrayList<String> borrLendIDList = new ArrayList<>();
        for (BorrLend bl : borrLendList)
            borrLendIDList.add(bl.getMaMT());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(borrLendIDList.get(0), borrLendIDList);
        dialog.setTitle("Xuất biểu mẫu phiếu mượn");
        dialog.setContentText("Mời chọn mã mượn trả!");
        dialog.setContentText("Mã mượn trả: ");
        Optional<String> borrLendID = dialog.showAndWait();
        boolean sel = (new Controller0()).setConfirm("Chọn nơi lưu!");
        if (sel) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(GUI.window);
            String URL = selectedDirectory.getAbsolutePath();
            File borrLendPatern = new File(URL + "//MT" + borrLendID.get() + ".txt");
            FileWriter fileWriter = new FileWriter(borrLendPatern.getAbsolutePath());
            BorrLend tmp = new BorrLend();
            for (BorrLend bl : borrLendList)
                if (bl.getMaMT().equals(borrLendID.get()))
                    tmp = bl;
            fileWriter.write("\n\t\t --- PHIẾU MƯỢN SÁCH ---" + "\n" + "\t\t   MÃ PHIẾU: " + tmp.getMaMT() + "\n\n");
            fileWriter.write("Mã thủ thư: " + tmp.getMaTT() + "\tNgày mượn: " + tmp.getNgayMuon() + "\n");
            fileWriter.write("Mã độc giả: " + tmp.getMaDG() + "\tNgày hẹn trả: " + tmp.getNgayHenTra() + "\n");
            fileWriter.write("Tiền cọc: " + tmp.getTienCoc() + "\n\n   Chữ ký của độc giả\t\tChữ ký của thủ thư");
            fileWriter.close();
            (new Controller0()).setAlert("In biểu mẫu thành công!");
        }
        else {
            (new Controller0()).setAlert("In biểu mẫu thất bại!");
        }
    }

    public void cancelBorrLend() {
        clearBorrLendInfo();
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
        LibrarianManager libManager = new LibrarianManager();
        ReaderManager readerManager = new ReaderManager();
        try {
            ArrayList<Librarian> libList = libManager.selectLibrarian();
            ObservableList<String> libIDList = FXCollections.observableList(new ArrayList());
            for (Librarian lb : libList)
                libIDList.add(lb.getMaTT());
            librarianID.setItems(libIDList);
            ArrayList<Reader> readerList = readerManager.selectReader();
            ObservableList<String> readerIDList = FXCollections.observableList(new ArrayList());
            for (Reader rd : readerList)
                readerIDList.add(rd.getMaDG());
            readerID.setItems(readerIDList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        updateBorrLendTable();
        getSelectedBorrLend();
    }
}
