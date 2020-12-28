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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Controller3 implements Initializable {
    @FXML
    private TableView<Reader> tableViewReader = new TableView();
    @FXML
    private TableColumn<Reader, String> maDG = new TableColumn();
    @FXML
    private TableColumn<Reader, String> tenDG = new TableColumn();
    @FXML
    private TableColumn<Reader, String> gioiTinh = new TableColumn();
    @FXML
    private TableColumn<Reader, String> ngaySinh = new TableColumn();
    @FXML
    private TableColumn<Reader, Integer> CMND = new TableColumn();
    @FXML
    private TableColumn<Reader, Integer> email = new TableColumn();
    @FXML
    private TableColumn<Reader, String> dienThoai = new TableColumn();
    @FXML
    private TextField readerID = new TextField();
    @FXML
    private TextField readerName = new TextField();
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
    private TextField readerKeyword = new TextField();
    @FXML
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public Controller3() {
    }

    public void showWindow0(ActionEvent event) {
        GUI.window.setScene(GUI.scene0);
        GUI.window.show();
    }

    private ArrayList<Reader> selReaderList;
    private ObservableList<Reader> readerList = FXCollections.observableArrayList();
    private ObservableList<Reader> readerSearchList = FXCollections.observableArrayList();
    private ReaderManager readerManager = new ReaderManager();
    private Reader mainReader = new Reader();

    public void search() {
        readerSearchList.clear();
        String keyword = readerKeyword.getText();
        for (int i = 0; i < readerList.size(); i++) {
            Reader tmp = readerList.get(i);
            if (tmp.getMaDG().contains(keyword) || tmp.getTenDG().contains(keyword)
                    || tmp.getGioiTinh().contains(keyword) || tmp.getCMND().contains(keyword)
                    || tmp.getEmail().contains(keyword) || tmp.getDienThoai().contains(keyword))
                readerSearchList.add(tmp);
        }
        if (readerSearchList.isEmpty()) {
            (new Controller0()).setAlert("Không tìm thấy kết quả nào!");
            updateReaderTable();
        }
        else {
            Collections.sort(readerSearchList);
            maDG.setCellValueFactory(new PropertyValueFactory("maDG"));
            tenDG.setCellValueFactory(new PropertyValueFactory("tenDG"));
            gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
            ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
            CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
            email.setCellValueFactory(new PropertyValueFactory("email"));
            dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
            tableViewReader.setItems(this.readerSearchList);
        }
        readerKeyword.setText("");
    }

    public void addNewReader() {
        Reader tmp = getReaderInfo();
        if (readerManager.addReader(tmp)) {
            readerList.add(tmp);
            tableViewReader.setItems(readerList);
            updateReaderTable();
            clearReaderInfo();
            (new Controller0()).setAlert("Thêm thành công!");
        } else
            (new Controller0()).setAlert("Thêm thất bại! Mời kiểm tra lại dữ liệu!");
        this.clearReaderInfo();
    }

    public void updateReader() {
        Reader tmp = getReaderInfo();
        if (readerManager.updateReader(tmp)) {
            int i = 0;
            while (!readerList.get(i).getMaDG().equals(tmp.getMaDG()))
                i++;
            readerList.set(i, tmp);
            updateReaderTable();
            clearReaderInfo();
            (new Controller0()).setAlert("Sửa thành công!");
        } else
            (new Controller0()).setAlert("Sửa thất bại");
    }

    public void deleteReader() {
        boolean choice = (new Controller0()).setConfirm("Bạn có chắc chắn muốn xóa không?");
        if (choice) {
            if (readerManager.deleteReader(mainReader)) {
                for (int i = 0; i < readerList.size(); i++) {
                    if (readerList.get(i).getMaDG().equals(mainReader.getMaDG()))
                        readerList.remove(i);
                }
                updateReaderTable();
                clearReaderInfo();
                (new Controller0()).setAlert("Xóa thành công!");
            }
            else
                (new Controller0()).setAlert("Xóa thất bại! Mời kiểm tra lại!");
        }
    }

    public void cancelReader() {
        clearReaderInfo();
    }

    public void getSelectedReader() {
        tableViewReader.setOnMouseClicked(e -> {
            mainReader = tableViewReader.getSelectionModel().getSelectedItem();
            readerID.setText(mainReader.getMaDG());
            readerName.setText(mainReader.getTenDG());
            gender.setText(mainReader.getGioiTinh());
            birthday.setValue(mainReader.getNgaySinh());
            card.setText(String.valueOf(mainReader.getCMND()));
            mail.setText(String.valueOf(mainReader.getEmail()));
            phoneNum.setText(mainReader.getDienThoai());
        });
    }

    void updateReaderTable() {
        Collections.sort(readerList);
        maDG.setCellValueFactory(new PropertyValueFactory("maDG"));
        tenDG.setCellValueFactory(new PropertyValueFactory("tenDG"));
        gioiTinh.setCellValueFactory(new PropertyValueFactory("gioiTinh"));
        ngaySinh.setCellValueFactory(new PropertyValueFactory("ngaySinh"));
        CMND.setCellValueFactory(new PropertyValueFactory("CMND"));
        email.setCellValueFactory(new PropertyValueFactory("email"));
        dienThoai.setCellValueFactory(new PropertyValueFactory("dienThoai"));
        tableViewReader.setItems(this.readerList);
    }

    Reader getReaderInfo() {
        String ma_doc_gia = readerID.getText();
        String ten_doc_gia = readerName.getText();
        String gioi_tinh = gender.getText();
        LocalDate ngay_sinh = birthday.getValue();
        String CMND = card.getText();
        String email = mail.getText();
        String dien_thoai = phoneNum.getText();
        Reader tmp = new Reader(ma_doc_gia, ten_doc_gia, gioi_tinh, ngay_sinh, CMND, email, dien_thoai);
        updateReaderTable();
        return tmp;
    }

    void clearReaderInfo() {
        readerID.setText("");
        readerName.setText("");
        gender.setText("");
        birthday.setValue(null);
        card.setText("");
        mail.setText("");
        phoneNum.setText("");
        updateReaderTable();
    }

    public void insertReaderByFile() throws IOException {
        ArrayList<Reader> insertList = readerManager.insertReaderByFile();
        boolean res = true;
        int n = insertList.size();
        for (int i = 0; i < n; i++) {
            Reader r = insertList.get(i);
            res = res && readerManager.addReader(r);
            if (res == false) {
                (new Controller0()).setAlert("Nhập file thất bại!");
                return;
            }
            readerList.add(r);
            tableViewReader.setItems(readerList);
            updateReaderTable();
        }
        if (res == true)
            (new Controller0()).setAlert("Nhập file thành công!");
        else
            (new Controller0()).setAlert("Nhập file thất bại!");
    }

    public void getReaderFileURL() throws IOException {
        File selectedFile = fileChooser.showOpenDialog(GUI.window);
        readerManager.readerFileURL = selectedFile.getAbsolutePath();
        insertReaderByFile();
    }

    public void exportReaderListingFile() {
        try {
            File readerDataFile = new File(readerManager.saverURL + "//readerData.txt");
            FileWriter readerDataWriter = new FileWriter(readerDataFile.getAbsolutePath());
            int n = readerList.size();
            readerDataWriter.write("\n          ----------- Thống kê độc giả trong thư viện -----------\n\n");
            for (int i = 0; i < n; i++) {
                readerDataWriter.write(" ------------------\n" + "No " + (i + 1) + ":\n" + "Mã độc giả: "
                        + readerList.get(i).getMaDG() + "\nTên độc giả: " + readerList.get(i).getTenDG() + "\n" + "Giới tính: "
                        + readerList.get(i).getGioiTinh() + "\nNgày sinh: " + readerList.get(i).getNgaySinh() + "\nCMND: "
                        + readerList.get(i).getCMND() + "\nEmail: " + readerList.get(i).getEmail() + "\nĐiện thoại: "
                        + readerList.get(i).getDienThoai() + "\n");
            }
            readerDataWriter.close();
            (new Controller0()).setAlert("Xuất dữ liệu thành công!");
        } catch (IOException e) {
            (new Controller0()).setAlert("Xuất dữ liệu thất bại!");
            e.printStackTrace();
        }
    }

    public void listReader() {
        boolean sel = (new Controller0()).setConfirm("Chọn nơi lưu!");
        if (sel) {
            File selectedDirectory = directoryChooser.showDialog(GUI.window);
            readerManager.saverURL = selectedDirectory.getAbsolutePath();
            exportReaderListingFile();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Iterator var3;
        try {
            selReaderList = readerManager.selectReader();
            var3 = selReaderList.iterator();
            while (var3.hasNext()) {
                Reader l = (Reader) var3.next();
                readerList.add(l);
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        updateReaderTable();
        getSelectedReader();
    }
}
