package org.bigbio.pgatk.pepgenome.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    Hyperlink helpLink;

    @FXML
    Button closeBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onCloseWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}
