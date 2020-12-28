package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller5 implements Initializable {
    @FXML
    private TableView<Detail> tableViewDetail = new TableView();
    @FXML
    private TableColumn<Detail, String> maMT = new TableColumn();
    @FXML
    private TableColumn<Detail, String> maSach = new TableColumn();
    @FXML
    private TableColumn<Detail, String> ngayTra = new TableColumn();
    @FXML
    private TableColumn<Detail, String> trangThaiSach = new TableColumn();
    @FXML
    private TableColumn<Detail, Integer> tienPhat = new TableColumn();
    @FXML
    private TableColumn<Detail, String> ghiChu = new TableColumn();
    @FXML
    private ComboBox borrLendID = new ComboBox();
    @FXML
    private ComboBox bookID = new ComboBox();
    @FXML
    private DatePicker realReturnDate = new DatePicker();
    @FXML
    private TextField state = new TextField();
    @FXML
    private TextField fine = new TextField();
    @FXML
    private TextField note = new TextField();
    @FXML
    private TextField detailKeyword = new TextField();
    @FXML
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public Controller5() {
    }

    public void showWindow0(ActionEvent event) {
        clearDetailInfo();
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    private ArrayList<Detail> selDetailList;
    private ObservableList<Detail> detailList = FXCollections.observableArrayList();
    private ObservableList<Detail> detailSearchList = FXCollections.observableArrayList();
    private DetailManager detailManager = new DetailManager();
    private Detail mainDetail = new Detail();

    public void searchDetail() {
        detailSearchList.clear();
        String keyword = detailKeyword.getText();
        for (int i = 0; i < detailList.size(); i++) {
            Detail tmp = detailList.get(i);
            if (tmp.getMaMT().contains(keyword) || tmp.getMaSach().contains(keyword)
                    || tmp.getTrangThai().contains(keyword))
                detailSearchList.add(tmp);
        }
        if (detailSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateDetailTable();
        }
        else {
            FXCollections.sort(detailSearchList);
            maMT.setCellValueFactory(new PropertyValueFactory("maMT"));
            maSach.setCellValueFactory(new PropertyValueFactory("maSach"));
            ngayTra.setCellValueFactory(new PropertyValueFactory("ngayTra"));
            trangThaiSach.setCellValueFactory(new PropertyValueFactory("trangThai"));
            tienPhat.setCellValueFactory(new PropertyValueFactory("tienPhat"));
            ghiChu.setCellValueFactory(new PropertyValueFactory("ghiChu"));
            tableViewDetail.setItems(this.detailSearchList);
        }
        detailKeyword.setText("");
    }

    public void addNewDetail() {
        Detail tmp = getDetailInfo();
        try {
            String insQuery = "INSERT INTO dbo.ChiTietMuonTra VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = this.detailManager.cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaMT());
            pstm.setString(2, tmp.getMaSach());
            pstm.setDate(3, Date.valueOf(tmp.getNgayTra()));
            pstm.setString(4, tmp.getTrangThai());
            pstm.setInt(5, tmp.getTienPhat());
            pstm.setString(6, tmp.getGhiChu());
            pstm.execute();
            this.detailList.add(tmp);
            this.tableViewDetail.setItems(this.detailList);
            (new Controller0()).setAlert("Thêm thành công!");
        } catch (SQLException var5) {
            var5.printStackTrace();
            (new Controller0()).setAlert("Thêm thất bại!");
        }

        this.clearDetailInfo();
    }

    public void updateDetail() {
        Detail tmp = getDetailInfo();
        if (detailManager.updateDetail(tmp)) {
            int i = 0;
            while (!detailList.get(i).getMaMT().equals(tmp.getMaMT()) || !detailList.get(i).getMaSach().equals(tmp.getMaSach()))
                i++;
            detailList.set(i, tmp);
            updateDetailTable();
            clearDetailInfo();
            (new Controller0()).setAlert("Sửa thành công!");
        } else
            (new Controller0()).setAlert("Sửa thất bại");
    }

    public void deleteDetail() {
        boolean choice = (new Controller0()).setConfirm("Bạn có chắc chắn muốn xóa không?");
        if (choice) {
            if (detailManager.deleteDetail(mainDetail)) {
                for (int i = 0; i < detailList.size(); i++) {
                    if (detailList.get(i).getMaMT().equals(mainDetail.getMaMT()) && detailList.get(i).getMaSach().equals(mainDetail.getMaSach()))
                        detailList.remove(i);
                }
                updateDetailTable();
                clearDetailInfo();
                (new Controller0()).setAlert("Xóa thành công!");
            } else
                (new Controller0()).setAlert("Xóa thất bại! Mời kiểm tra lại!");
        }
    }

    public void cancelDetail() {
        clearDetailInfo();
    }

    public void getSelectedDetail() {
        tableViewDetail.setOnMouseClicked(e -> {
            mainDetail = tableViewDetail.getSelectionModel().getSelectedItem();
            borrLendID.setValue(mainDetail.getMaMT());
            bookID.setValue(mainDetail.getMaSach());
            realReturnDate.setValue(mainDetail.getNgayTra());
            state.setText(mainDetail.getTrangThai());
            fine.setText(String.valueOf(mainDetail.getTienPhat()));
            note.setText(mainDetail.getGhiChu());
        });
    }

    void updateDetailTable() {
        FXCollections.sort(detailList);
        maMT.setCellValueFactory(new PropertyValueFactory("maMT"));
        maSach.setCellValueFactory(new PropertyValueFactory("maSach"));
        ngayTra.setCellValueFactory(new PropertyValueFactory("ngayTra"));
        trangThaiSach.setCellValueFactory(new PropertyValueFactory("trangThai"));
        tienPhat.setCellValueFactory(new PropertyValueFactory("tienPhat"));
        ghiChu.setCellValueFactory(new PropertyValueFactory("ghiChu"));
        tableViewDetail.setItems(this.detailList);
    }

    Detail getDetailInfo() {
        String ma_MT = this.borrLendID.getSelectionModel().selectedItemProperty().getValue().toString();
        String ma_sach = this.bookID.getSelectionModel().selectedItemProperty().getValue().toString();
        LocalDate ngay_tra = this.realReturnDate.getValue();
        String trang_thai = this.state.getText();
        int tien_phat = Integer.parseInt(this.fine.getText());
        String ghi_chu = this.note.getText();
        Detail tmp = new Detail(ma_MT, ma_sach, ngay_tra, trang_thai, tien_phat, ghi_chu);
        return tmp;
    }

    void clearDetailInfo() {
        this.borrLendID.getSelectionModel().clearSelection();
        this.bookID.getSelectionModel().clearSelection();
        this.realReturnDate.setValue(null);
        this.state.setText("");
        this.fine.setText("");
        this.note.setText("");
        updateDetailTable();
    }

    public void getDetailFileURL() throws IOException {
        File selectedFile = fileChooser.showOpenDialog(GUI.window);
        detailManager.detailFileURL = selectedFile.getAbsolutePath();
        insertDetailByFile();
    }

    public void insertDetailByFile() throws IOException {
        ArrayList<Detail> insertList = detailManager.insertDetailByFile();
        boolean res = true;
        int n = insertList.size();
        for (int i = 0; i < n; i++) {
            Detail b = insertList.get(i);
            res = res && detailManager.addDetail(b);
            if (res == false) {
                (new Controller0()).setAlert("Nhập file thất bại!");
                return;
            }
            detailList.add(b);
            tableViewDetail.setItems(detailList);
            updateDetailTable();
        }
        if (res == true)
            (new Controller0()).setAlert("Nhập file thành công!");
        else
            (new Controller0()).setAlert("Nhập file thất bại!");
    }

    public void exportListingFile() {
        try {
            File bookDataFile = new File(detailManager.saverURL + "//detailData.txt");
            FileWriter detailDataWriter = new FileWriter(bookDataFile.getAbsolutePath());
            int n = detailList.size();
            detailDataWriter.write("-- Thống kê " + n + " chi tiết mượn trả --\n");
            for (int i = 0; i < n; i++) {
                detailDataWriter.write("No " + i + 1 + ":\n");
                detailDataWriter.write(detailList.get(i).getMaMT() + "\n" + detailList.get(i).getMaSach() + "\n"
                        + detailList.get(i).getNgayTra() + "\n" + detailList.get(i).getTrangThai() + "\n"
                        + detailList.get(i).getTienPhat() + "\n" + detailList.get(i).getGhiChu() + "\n -----------------\n");
            }
            detailDataWriter.close();
            (new Controller0()).setAlert("Xuất dữ liệu thành công!");
        } catch (IOException e) {
            (new Controller0()).setAlert("Xuất dữ liệu thất bại!");
            e.printStackTrace();
        }
    }

    public void printDetailPatern() throws IOException, SQLException {
        ArrayList<String> borrLendIDList = new ArrayList<>();
        for (Detail d : detailList)
            borrLendIDList.add(d.getMaMT());
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
            File detailPatern = new File(URL + "//ChiTietMT" + borrLendID.get() + ".txt");
            FileWriter fileWriter = new FileWriter(detailPatern.getAbsolutePath());
            ArrayList<Detail> details = new ArrayList<>();
            for (Detail d : detailList)
                if (d.getMaMT().equals(borrLendID.get()))
                    details.add(d);
            int n = details.size();
            fileWriter.write("\n\t\t -- CHI TIẾT MƯỢN TRẢ SÁCH --" + "\n" + "\t\t      MÃ MƯỢN TRẢ: " + details.get(0).getMaMT() + "\n\n");
            for (int i = 0; i < n; i++) {
                fileWriter.write("---------------------------------------------------------------\n");
                fileWriter.write("Mã sách: " + details.get(i).getMaSach() + "\t\t\tNgày trả: " + details.get(i).getNgayTra() + "\n");
                fileWriter.write("Tiền phạt: " + details.get(i).getTienPhat() + "\n");
                fileWriter.write("Trạng thái sách: " + details.get(i).getTrangThai() + "\n");
                fileWriter.write("Ghi chú: " + details.get(i).getGhiChu() + "\n");
            }
            fileWriter.write("---------------------------------------------------------------\n");
            fileWriter.write("\n\n     Chữ ký của độc giả\t\tChữ ký của thủ thư\n");
            fileWriter.close();
            (new Controller0()).setAlert("In biểu mẫu thành công!");
        }
        else {
            (new Controller0()).setAlert("In biểu mẫu thất bại!");
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selDetailList = detailManager.selectDetail();
            var3 = selDetailList.iterator();
            while (var3.hasNext()) {
                Detail b = (Detail) var3.next();
                detailList.add(b);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        BorrLendManager borrLendManager = new BorrLendManager();
        BookManager bookManager = new BookManager();
        try {
            ArrayList<BorrLend> borrLendList = borrLendManager.borrLendSelect();
            ObservableList<String> borrLendIDList = FXCollections.observableList(new ArrayList());
            for (BorrLend bl : borrLendList)
                borrLendIDList.add(bl.getMaMT());
            borrLendID.setItems(borrLendIDList);
            ArrayList<Book> bookList = bookManager.selectBook();
            ObservableList<String> bookIDList = FXCollections.observableList(new ArrayList());
            for (Book b : bookList)
                bookIDList.add(b.getMaSach());
            bookID.setItems(bookIDList);
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateDetailTable();
        getSelectedDetail();
    }
}
