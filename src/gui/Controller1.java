package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.BookManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.sql.PreparedStatement;

public class Controller1 implements Initializable {
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
    private TableColumn<Book, String> gioiThieu = new TableColumn();
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
    private TextField intro = new TextField();
    @FXML
    private TextField bookKeyword = new TextField();

    public Controller1() {
    }

    public void showWindow0(ActionEvent event) {
        clearBookInfo();
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    private ArrayList<Book> selBookList;
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private ObservableList<Book> bookSearchList = FXCollections.observableArrayList();
    private BookManager bookManager = new BookManager();
    private Book mainBook = new Book();

    public void search() {
        bookSearchList.clear();
        String keyword = bookKeyword.getText();
        for (int i = 0; i < bookList.size(); i++) {
            Book tmp = bookList.get(i);
            if (tmp.getMaSach().contains(keyword) || tmp.getTenSach().contains(keyword)
                    || tmp.getTacGia().contains(keyword) || tmp.getNhaXB().contains(keyword)
                    || String.valueOf(tmp.getNamXB()).contains(keyword)
                    || String.valueOf(tmp.getDonGia()).contains(keyword)
                    || tmp.getGioiThieu().contains(keyword))
                bookSearchList.add(tmp);
        }
        if (bookSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateBookTable();
        }
        else {
            Collections.sort(bookSearchList);
            maSach.setCellValueFactory(new PropertyValueFactory("maSach"));
            tenSach.setCellValueFactory(new PropertyValueFactory("tenSach"));
            tacGia.setCellValueFactory(new PropertyValueFactory("tacGia"));
            nhaXB.setCellValueFactory(new PropertyValueFactory("nhaXB"));
            namXB.setCellValueFactory(new PropertyValueFactory("namXB"));
            donGia.setCellValueFactory(new PropertyValueFactory("donGia"));
            gioiThieu.setCellValueFactory(new PropertyValueFactory("gioiThieu"));
            tableViewBook.setItems(this.bookSearchList);
        }
        bookKeyword.setText("");
    }

    public void addNewBook() {
        Book tmp = getBookInfo();
        if (bookManager.addBook(tmp)) {
            bookList.add(tmp);
            tableViewBook.setItems(bookList);
            updateBookTable();
            clearBookInfo();
            (new Controller0()).setAlert("Thêm thành công!");
        } else
            (new Controller0()).setAlert("Thêm thất bại! Mời kiểm tra lại dữ liệu!");
    }

    public void updateBook() {
        Book tmp = getBookInfo();
        if (bookManager.updateBook(tmp)) {
            int i = 0;
            while (!bookList.get(i).getMaSach().equals(tmp.getMaSach()))
                i++;
            bookList.set(i, tmp);
            updateBookTable();
            clearBookInfo();
            (new Controller0()).setAlert("Sửa thành công!");
        } else
            (new Controller0()).setAlert("Sửa thất bại");
    }

    public void deleteBook() {
        boolean choice = (new Controller0()).setConfirm("Bạn có chắc chắn muốn xóa không?");
        if (choice) {
            if (bookManager.deleteBook(mainBook)) {
                for (int i = 0; i < bookList.size(); i++) {
                    if (bookList.get(i).getMaSach().equals(mainBook.getMaSach()))
                        bookList.remove(i);
                }
                updateBookTable();
                clearBookInfo();
                (new Controller0()).setAlert("Xóa thành công!");
            }
            else
                (new Controller0()).setAlert("Xóa thất bại! Mời kiểm tra lại!");
        }
    }

    public void cancelBook() {
        clearBookInfo();
    }

    public void getSelectedBook() {
        tableViewBook.setOnMouseClicked(e -> {
            mainBook = tableViewBook.getSelectionModel().getSelectedItem();
            bookID.setText(mainBook.getMaSach());
            bookName.setText(mainBook.getTenSach());
            author.setText(mainBook.getTacGia());
            publisher.setText(mainBook.getNhaXB());
            publishYear.setText(String.valueOf(mainBook.getNamXB()));
            price.setText(String.valueOf(mainBook.getDonGia()));
            intro.setText(mainBook.getGioiThieu());
        });
    }

    void updateBookTable() {
        Collections.sort(bookList);
        maSach.setCellValueFactory(new PropertyValueFactory("maSach"));
        tenSach.setCellValueFactory(new PropertyValueFactory("tenSach"));
        tacGia.setCellValueFactory(new PropertyValueFactory("tacGia"));
        nhaXB.setCellValueFactory(new PropertyValueFactory("nhaXB"));
        namXB.setCellValueFactory(new PropertyValueFactory("namXB"));
        donGia.setCellValueFactory(new PropertyValueFactory("donGia"));
        gioiThieu.setCellValueFactory(new PropertyValueFactory("gioiThieu"));
        tableViewBook.setItems(this.bookList);
    }

    Book getBookInfo() {
        String ma_sach = bookID.getText();
        String ten_sach = bookName.getText();
        String tac_gia = author.getText();
        String nha_xuat_ban = publisher.getText();
        int nam_xuat_ban = Integer.parseInt(publishYear.getText());
        int don_gia = Integer.parseInt(price.getText());
        String gioi_thieu = intro.getText();
        Book tmp = new Book(ma_sach, ten_sach, tac_gia, nha_xuat_ban, nam_xuat_ban, don_gia, gioi_thieu);
        updateBookTable();
        return tmp;
    }

    void clearBookInfo() {
        bookID.setText("");
        bookName.setText("");
        author.setText("");
        publisher.setText("");
        publishYear.setText("");
        price.setText("");
        intro.setText("");
        updateBookTable();
    }

    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selBookList = bookManager.selectBook();
            var3 = selBookList.iterator();
            while (var3.hasNext()) {
                Book b = (Book) var3.next();
                bookList.add(b);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateBookTable();
        getSelectedBook();
    }
}
