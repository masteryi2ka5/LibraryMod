package gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class Controller0 implements Initializable {
    public Controller0() {
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
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();
    }

    boolean setConfirm(String mess) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    public void initialize(URL location, ResourceBundle resources) {
    }
}
