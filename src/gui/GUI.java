package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    static Stage window;
    static Parent root0;
    static Parent root1;
    static Parent root2;
    static Parent root3;
    static Parent root4;
    static Parent root5;
    static Scene scene0;
    static Scene scene1;
    static Scene scene2;
    static Scene scene3;
    static Scene scene4;
    static Scene scene5;

    public GUI() {
    }

    public void start(Stage primaryStage) {
        try {
            window = primaryStage;
            window.setTitle("Quản lý thư viện - Lê Nhật Nam - 20183956");
            root0 = FXMLLoader.load(this.getClass().getResource("/gui/GUI0.fxml"));
            scene0 = new Scene(root0);
            window.setScene(scene0);
            window.show();
            root1 = FXMLLoader.load(this.getClass().getResource("/gui/GUI1.fxml"));
            scene1 = new Scene(root1);
            root2 = FXMLLoader.load(this.getClass().getResource("/gui/GUI2.fxml"));
            scene2 = new Scene(root2);
            root3 = FXMLLoader.load(this.getClass().getResource("/gui/GUI3.fxml"));
            scene3 = new Scene(root3);
            root4 = FXMLLoader.load(this.getClass().getResource("/gui/GUI4.fxml"));
            scene4 = new Scene(root4);
            root5 = FXMLLoader.load(this.getClass().getResource("/gui/GUI5.fxml"));
            scene5 = new Scene(root5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
