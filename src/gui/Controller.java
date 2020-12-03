package gui;

import gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.BookManager;
import model.BorrLend;
import model.BorrLendManager;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Book> tableViewBook = new TableView();
    @FXML
    private TableColumn<Book, String> maSach = new TableColumn();
    @FXML
    private TableColumn<Book, String> tenSach = new TableColumn();
    @FXML
    private TableColumn<Book, String> tacGia = new TableColumn();
    @FXML
    private TableColumn<Book, String> nhaXB = new TableColumn();
    @FXML
    private TableColumn<Book, Integer> namXB = new TableColumn();
    @FXML
    private TableColumn<Book, Integer> donGia = new TableColumn();
    @FXML
    private TableColumn<Book, Integer> soLuong = new TableColumn();
    @FXML
    private TextField bookID = new TextField();
    @FXML
    private TextField bookName = new TextField();
    @FXML
    private TextField author = new TextField();
    @FXML
    private TextField publisher = new TextField();
    @FXML
    private TextField publishYear = new TextField();
    @FXML
    private TextField price = new TextField();
    @FXML
    private TextField quantity = new TextField();
    private ArrayList<Book> selBookList;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private BookManager bookManager = new BookManager();
    @FXML
    private TableView<BorrLend> tableViewBorrLend = new TableView();
    @FXML
    private TableColumn<BorrLend, String> maMuonTra = new TableColumn();
    @FXML
    private TableColumn<BorrLend, String> maDocGia = new TableColumn();
    @FXML
    private TableColumn<BorrLend, String> maThuThu = new TableColumn();
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
    private ArrayList<BorrLend> selBorrLendList;
    private ObservableList<BorrLend> borrLendList = FXCollections.observableArrayList();
    private BorrLendManager borrLendManager = new BorrLendManager();

    public Controller() {
    }

    public void showWindow0(ActionEvent event) {
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    public void showWindow1(ActionEvent event) {
        GUI.window.setScene(GUI.scene1);
        GUI.window.show();
    }

    public void showWindow2(ActionEvent event) {
        GUI.window.setScene(GUI.scene2);
        GUI.window.show();
    }

    public void showWindow3(ActionEvent event) {
        GUI.window.setScene(GUI.scene3);
        GUI.window.show();
    }

    public void showWindow4(ActionEvent event) {
        GUI.window.setScene(GUI.scene4);
        GUI.window.show();
    }

    void setAlert(String mess) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText((String)null);
        alert.setContentText(mess);
        alert.showAndWait();
    }

    public void addNewBook(ActionEvent event) {
        Book tmp = this.getBookInfo();
        String insQuery = "INSERT INTO dbo.Sach VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstm = this.bookManager.cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaSach());
            pstm.setString(2, tmp.getTenSach());
            pstm.setString(3, tmp.getTacGia());
            pstm.setString(4, tmp.getNhaXB());
            pstm.setInt(5, tmp.getNamXB());
            pstm.setInt(6, tmp.getDonGia());
            pstm.setInt(7, tmp.getSoLuong());
            pstm.execute();
            this.bookList.add(tmp);
            this.tableViewBook.setItems(this.bookList);
            this.setAlert("Thêm thành công!");
        } catch (SQLException var5) {
            var5.printStackTrace();
            this.setAlert("Thêm thất bại!");
        }

        this.clearBookInfo();
    }

    public void cancelBook() {
        this.clearBookInfo();
    }

    void updateBookTable() {
        this.maSach.setCellValueFactory(new PropertyValueFactory("maSach"));
        this.tenSach.setCellValueFactory(new PropertyValueFactory("tenSach"));
        this.tacGia.setCellValueFactory(new PropertyValueFactory("tacGia"));
        this.nhaXB.setCellValueFactory(new PropertyValueFactory("nhaXB"));
        this.namXB.setCellValueFactory(new PropertyValueFactory("namXB"));
        this.donGia.setCellValueFactory(new PropertyValueFactory("donGia"));
        this.soLuong.setCellValueFactory(new PropertyValueFactory("soLuong"));
        this.tableViewBook.setItems(this.bookList);
    }

    Book getBookInfo() {
        String ma_sach = this.bookID.getText();
        String ten_sach = this.bookName.getText();
        String tac_gia = this.author.getText();
        String nha_xuat_ban = this.publisher.getText();
        int nam_xuat_ban = Integer.parseInt(this.publishYear.getText());
        int don_gia = Integer.parseInt(this.price.getText());
        int so_luong = Integer.parseInt(this.quantity.getText());
        Book tmp = new Book(ma_sach, ten_sach, tac_gia, nha_xuat_ban, nam_xuat_ban, don_gia, so_luong);
        return tmp;
    }

    void clearBookInfo() {
        this.bookID.setText("");
        this.bookName.setText("");
        this.author.setText("");
        this.publisher.setText("");
        this.publishYear.setText("");
        this.price.setText("");
        this.quantity.setText("");
    }

    public void addNewBorrLend(ActionEvent event) throws SQLException {
        BorrLend tmp = this.getBorrLendInfo();

        try {
            String insQuery = "INSERT INTO dbo.MuonTra VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = this.bookManager.cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaMT());
            pstm.setString(2, tmp.getMaDG());
            pstm.setString(3, tmp.getMaTT());
            pstm.setDate(4, Date.valueOf(tmp.getNgayMuon()));
            pstm.setDate(5, Date.valueOf(tmp.getNgayHenTra()));
            pstm.setInt(6, tmp.getTienCoc());
            pstm.execute();
            this.borrLendList.add(tmp);
            this.tableViewBorrLend.setItems(this.borrLendList);
            this.setAlert("Thêm thành công!");
        } catch (SQLException var5) {
            var5.printStackTrace();
            this.setAlert("Thêm thất bại!");
        }

        this.clearBorrLendInfo();
    }

    void updateBorrLendTable() {
        this.maMuonTra.setCellValueFactory(new PropertyValueFactory("maMT"));
        this.maDocGia.setCellValueFactory(new PropertyValueFactory("maDG"));
        this.maThuThu.setCellValueFactory(new PropertyValueFactory("maTT"));
        this.ngayMuon.setCellValueFactory(new PropertyValueFactory("ngayMuon"));
        this.ngayHenTra.setCellValueFactory(new PropertyValueFactory("ngayHenTra"));
        this.tienCoc.setCellValueFactory(new PropertyValueFactory("tienCoc"));
        this.tableViewBorrLend.setItems(this.borrLendList);
    }

    BorrLend getBorrLendInfo() {
        String ma_muon_tra = this.borrLendID.getText();
        String ma_doc_gia = this.readerID.getText();
        String ma_thu_thu = this.librarianID.getText();
        LocalDate ngay_muon = (LocalDate)this.borrDate.getValue();
        LocalDate ngay_hen_tra = (LocalDate)this.returnDate.getValue();
        int tien_coc = Integer.parseInt(this.deposit.getText());
        BorrLend tmp = new BorrLend(ma_muon_tra, ma_doc_gia, ma_thu_thu, ngay_muon, ngay_hen_tra, tien_coc);
        return tmp;
    }

    void clearBorrLendInfo() {
        this.borrDate.setValue(LocalDate.of(2000, 1, 1));
        this.returnDate.setValue(LocalDate.of(2000, 1, 1));
        this.borrLendID.setText("");
        this.readerID.setText("");
        this.librarianID.setText("");
        this.deposit.setText("");
    }

    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            this.selBookList = this.bookManager.bookSelect();
            var3 = this.selBookList.iterator();

            while(var3.hasNext()) {
                Book b = (Book)var3.next();
                this.bookList.add(b);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        this.updateBookTable();

        try {
            this.selBorrLendList = this.borrLendManager.borrLendSelect();
            var3 = this.selBorrLendList.iterator();

            while(var3.hasNext()) {
                BorrLend bl = (BorrLend)var3.next();
                this.borrLendList.add(bl);
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        this.updateBorrLendTable();
    }
}
