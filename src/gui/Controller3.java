package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Controller3 implements Initializable {
    @FXML
    private TableView<Librarian> tableViewLibrarian = new TableView();
    @FXML
    private TableColumn<Librarian, String> maTT = new TableColumn();
    @FXML
    private TableColumn<Librarian, String> tenTT = new TableColumn();
    @FXML
    private TableColumn<Librarian, String> gioiTinh = new TableColumn();
    @FXML
    private TableColumn<Librarian, String> ngaySinh = new TableColumn();
    @FXML
    private TableColumn<Librarian, Integer> CMND = new TableColumn();
    @FXML
    private TableColumn<Librarian, Integer> email = new TableColumn();
    @FXML
    private TableColumn<Librarian, String> dienThoai = new TableColumn();
    @FXML
    private TextField librarianID = new TextField();
    @FXML
    private TextField librarianName = new TextField();
    @FXML
    private TextField gender = new TextField();
    @FXML
    private DatePicker birthday = new DatePicker();
    @FXML
    private TextField card = new TextField();
    @FXML
    private TextField mail = new TextField();
    @FXML
    private TextField phoneNum = new TextField();
    @FXML
    private TextField librarianKeyword = new TextField();

    public Controller3() {
    }

    public void showWindow0(ActionEvent event) {
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    private ArrayList<Librarian> selLibrarianList;
    private ObservableList<Librarian> librarianList = FXCollections.observableArrayList();
    private ObservableList<Librarian> librarianSearchList = FXCollections.observableArrayList();
    private LibrarianManager librarianManager = new LibrarianManager();
    private Librarian mainLibrarian = new Librarian();

    public void search() {
        librarianSearchList.clear();
        String keyword = librarianKeyword.getText();
        for (int i = 0; i < librarianList.size(); i++) {
            Librarian tmp = librarianList.get(i);
            if (tmp.getMaTT().contains(keyword) || tmp.getTenTT().contains(keyword)
                    || tmp.getGioiTinh().contains(keyword) || tmp.getCMND().contains(keyword)
                    || tmp.getEmail().contains(keyword) || tmp.getDienThoai().contains(keyword))
                librarianSearchList.add(tmp);
        }
        if (librarianSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateLibrarianTable();
        }
        else {
            Collections.sort(librarianSearchList);
            maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
            tenTT.setCellValueFactory(new PropertyValueFactory("tenTT"));
            gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
            ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
            CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
            email.setCellValueFactory(new PropertyValueFactory("email"));
            dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
            tableViewLibrarian.setItems(this.librarianSearchList);
        }
        librarianKeyword.setText("");
    }

    public void addNewLibrarian() {
        Librarian tmp = getLibrarianInfo();
        if (librarianManager.addLibrarian(tmp)) {
            librarianList.add(tmp);
            tableViewLibrarian.setItems(librarianList);
            updateLibrarianTable();
            clearLibrarianInfo();
            (new Controller0()).setAlert("Thêm thành công!");
        } else
            (new Controller0()).setAlert("Thêm thất bại! Mời kiểm tra lại dữ liệu!");
        this.clearLibrarianInfo();
    }

    public void updateLibrarian() {
        Librarian tmp = getLibrarianInfo();
        if (librarianManager.updateLibrarian(tmp)) {
            int i = 0;
            while (!librarianList.get(i).getMaTT().equals(tmp.getMaTT()))
                i++;
            librarianList.set(i, tmp);
            updateLibrarianTable();
            clearLibrarianInfo();
            (new Controller0()).setAlert("Sửa thành công!");
        } else
            (new Controller0()).setAlert("Sửa thất bại");
    }

    public void deleteLibrarian() {
        boolean choice = (new Controller0()).setConfirm("Bạn có chắc chắn muốn xóa không?");
        if (choice) {
            if (librarianManager.deleteLibrarian(mainLibrarian)) {
                for (int i = 0; i < librarianList.size(); i++) {
                    if (librarianList.get(i).getMaTT().equals(mainLibrarian.getMaTT()))
                        librarianList.remove(i);
                }
                updateLibrarianTable();
                clearLibrarianInfo();
                (new Controller0()).setAlert("Xóa thành công!");
            }
            else
                (new Controller0()).setAlert("Xóa thất bại! Mời kiểm tra lại!");
        }
    }

    public void cancelLibrarian() {
        clearLibrarianInfo();
    }

    public void getSelectedLibrarian() {
        tableViewLibrarian.setOnMouseClicked(e -> {
            mainLibrarian = tableViewLibrarian.getSelectionModel().getSelectedItem();
            librarianID.setText(mainLibrarian.getMaTT());
            librarianName.setText(mainLibrarian.getTenTT());
            gender.setText(mainLibrarian.getGioiTinh());
            birthday.setValue(mainLibrarian.getNgaySinh());
            card.setText(String.valueOf(mainLibrarian.getCMND()));
            mail.setText(String.valueOf(mainLibrarian.getEmail()));
            phoneNum.setText(mainLibrarian.getDienThoai());
        });
    }

    void updateLibrarianTable() {
        Collections.sort(librarianList);
        maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
        tenTT.setCellValueFactory(new PropertyValueFactory("tenTT"));
        gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
        ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
        CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
        email.setCellValueFactory(new PropertyValueFactory("email"));
        dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
        tableViewLibrarian.setItems(this.librarianList);
    }

    Librarian getLibrarianInfo() {
        String ma_sach = librarianID.getText();
        String ten_sach = librarianName.getText();
        String gioi_tinh = gender.getText();
        LocalDate ngay_sinh = birthday.getValue();
        String CMND = card.getText();
        String email = mail.getText();
        String dien_thoai = phoneNum.getText();
        Librarian tmp = new Librarian(ma_sach, ten_sach, gioi_tinh, ngay_sinh, CMND, email, dien_thoai);
        updateLibrarianTable();
        return tmp;
    }

    void clearLibrarianInfo() {
        librarianID.setText("");
        librarianName.setText("");
        gender.setText("");
        birthday.setValue(null);
        card.setText("");
        mail.setText("");
        phoneNum.setText("");
        updateLibrarianTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selLibrarianList = librarianManager.selectLibrarian();
            var3 = selLibrarianList.iterator();
            while (var3.hasNext()) {
                Librarian l = (Librarian) var3.next();
                librarianList.add(l);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateLibrarianTable();
        getSelectedLibrarian();
    }
}
