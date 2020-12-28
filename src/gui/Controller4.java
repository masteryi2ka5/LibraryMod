package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.Librarian;
import model.LibrarianManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller4 implements Initializable {
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
    private TableColumn<Librarian, String> mail = new TableColumn();
    @FXML
    private TableColumn<Librarian, String> dienThoai = new TableColumn();
    @FXML
    private TextField librarianID = new TextField();
    @FXML
    private TextField librarianName = new TextField();
    @FXML
    private TextField gender = new TextField();
    @FXML
    private DatePicker dateOfBirth = new DatePicker();
    @FXML
    private TextField card = new TextField();
    @FXML
    private TextField email = new TextField();
    @FXML
    private TextField phoneNumber = new TextField();
    @FXML
    private TextField librarianKeyword = new TextField();
    @FXML
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public Controller4() {
    }

    public void showWindow0(ActionEvent event) {
        clearLibrarianInfo();
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    public void showLibrarianListing(ActionEvent event) {
        GUI.window.setScene(GUI.scene0);
    }

    private ArrayList<Librarian> selLibrarianList;
    private ObservableList<Librarian> librarianList = FXCollections.observableArrayList();
    private ObservableList<Librarian> librarianSearchList = FXCollections.observableArrayList();
    private LibrarianManager librarianManager = new LibrarianManager();
    private Librarian mainLibrarian = new Librarian();

    public void searchLibrarian() {
        librarianSearchList.clear();
        String keyword = librarianKeyword.getText();
        for (int i = 0; i < librarianList.size(); i++) {
            Librarian tmp = librarianList.get(i);
            if (tmp.getMaTT().contains(keyword) || tmp.getTenTT().contains(keyword)
                    || tmp.getGioiTinh().contains(keyword) || tmp.getCMND().contains(keyword))
                librarianSearchList.add(tmp);
        }
        if (librarianSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateLibrarianTable();
        }
        else {
            Collections.sort(librarianList);
            maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
            tenTT.setCellValueFactory(new PropertyValueFactory("tenTT"));
            gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
            ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
            CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
            mail.setCellValueFactory(new PropertyValueFactory("email"));
            dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
            tableViewLibrarian.setItems(this.librarianSearchList);
        }
        librarianKeyword.setText("");
    }

    public void addNewLibrarian() {
        Librarian tmp = getLibrarianInfo();
        try {
            String insQuery = "INSERT INTO dbo.MuonTra VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = this.librarianManager.cnn.prepareStatement(insQuery, 1);
            pstm.setString(1, tmp.getMaTT());
            pstm.setString(2, tmp.getTenTT());
            pstm.setString(3, tmp.getGioiTinh());
            pstm.setDate(4, Date.valueOf(tmp.getNgaySinh()));
            pstm.setString(5, tmp.getCMND());
            pstm.setString(6, tmp.getEmail());
            pstm.setString(7, tmp.getDienThoai());
            pstm.execute();
            this.librarianList.add(tmp);
            this.tableViewLibrarian.setItems(this.librarianList);
            (new Controller0()).setAlert("Thêm thành công!");
        } catch (SQLException var5) {
            var5.printStackTrace();
            (new Controller0()).setAlert("Thêm thất bại!");
        }

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
            } else
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
            dateOfBirth.setValue(mainLibrarian.getNgaySinh());
            card.setText(String.valueOf(mainLibrarian.getCMND()));
            email.setText(String.valueOf(mainLibrarian.getEmail()));
            phoneNumber.setText(mainLibrarian.getDienThoai());
        });
    }

    void updateLibrarianTable() {
        Collections.sort(librarianList);
        maTT.setCellValueFactory(new PropertyValueFactory("maTT"));
        tenTT.setCellValueFactory(new PropertyValueFactory("tenTT"));
        gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
        ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
        CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
        mail.setCellValueFactory(new PropertyValueFactory("email"));
        dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
        tableViewLibrarian.setItems(this.librarianList);
    }

    Librarian getLibrarianInfo() {
        String ma_TT = librarianID.getText();
        String ten_TT = librarianName.getText();
        String gioi_tinh = gender.getText();
        LocalDate ngay_sinh = dateOfBirth.getValue();
        String CMND = card.getText();
        String dia_chi_thu_dien_tu = email.getText();
        String dien_thoai = phoneNumber.getText();
        Librarian tmp = new Librarian(ma_TT, ten_TT, gioi_tinh, ngay_sinh, CMND, dia_chi_thu_dien_tu, dien_thoai);
        updateLibrarianTable();
        return tmp;
    }

    void clearLibrarianInfo() {
        librarianID.setText("");
        librarianName.setText("");
        gender.setText("");
        dateOfBirth.setValue(null);
        card.setText("");
        email.setText("");
        phoneNumber.setText("");
        updateLibrarianTable();
    }

    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selLibrarianList = librarianManager.selectLibrarian();
            var3 = selLibrarianList.iterator();
            while (var3.hasNext()) {
                Librarian b = (Librarian) var3.next();
                librarianList.add(b);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateLibrarianTable();
        getSelectedLibrarian();
    }

    public void getLibrarianFileURL() throws IOException {
        File selectedFile = fileChooser.showOpenDialog(GUI.window);
        librarianManager.librarianFileURL = selectedFile.getAbsolutePath();
        insertLibrarianByFile();
    }

    public void insertLibrarianByFile() throws IOException {
        ArrayList<Librarian> insertList = librarianManager.insertLibrarianByFile();
        boolean res = true;
        int n = insertList.size();
        for (int i = 0; i < n; i++) {
            Librarian b = insertList.get(i);
            res = res && librarianManager.addLibrarian(b);
            if (res == false) {
                (new Controller0()).setAlert("Nhập file thất bại!");
                return;
            }
            librarianList.add(b);
            tableViewLibrarian.setItems(librarianList);
            updateLibrarianTable();
        }
        if (res == true)
            (new Controller0()).setAlert("Nhập file thành công!");
        else
            (new Controller0()).setAlert("Nhập file thất bại!");
    }

    public void exportListingFile() {
        try {
            File bookDataFile = new File(librarianManager.saverURL + "//librarianData.txt");
            FileWriter librarianDataWriter = new FileWriter(bookDataFile.getAbsolutePath());
            int n = librarianList.size();
            librarianDataWriter.write("-- Thống kê " + n + " thủ thư trong thư viện --\n");
            for (int i = 0; i < n; i++) {
                librarianDataWriter.write("No " + i + 1 + ":\n");
                librarianDataWriter.write(librarianList.get(i).getMaTT() + "\n" + librarianList.get(i).getTenTT() + "\n"
                        + librarianList.get(i).getGioiTinh() + "\n" + librarianList.get(i).getNgaySinh() + "\n"
                        + librarianList.get(i).getCMND() + "\n" + librarianList.get(i).getEmail() + "\n"
                        + librarianList.get(i).getDienThoai() + "\n ---------------\n");
            }
            librarianDataWriter.close();
            (new Controller0()).setAlert("Xuất dữ liệu thành công!");
        } catch (IOException e) {
            (new Controller0()).setAlert("Xuất dữ liệu thất bại!");
            e.printStackTrace();
        }
    }

    public void listLibrarian() {
        boolean sel = (new Controller0()).setConfirm("Chọn nơi lưu!");
        if (sel) {
            File selectedDirectory = directoryChooser.showDialog(GUI.window);
            librarianManager.saverURL = selectedDirectory.getAbsolutePath();
            exportListingFile();
        }
    }
}
