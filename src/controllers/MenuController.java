package controllers;


import app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    private Main mainApp;

    @FXML
    private Button stepModeButton;

    @FXML
    private Button autoModeButton;

    @FXML
    private Button settingsButton;



    @FXML
    void onClickStepMode(ActionEvent event) {
        mainApp.openStepModeWindow();
    }

    @FXML
    void onClickAutoMode(ActionEvent event) {
        mainApp.openAutoModeWindow();
    }

    @FXML
    void onClickSettings(ActionEvent event) {
        mainApp.openSettingsWindow();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
