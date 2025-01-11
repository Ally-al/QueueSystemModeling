package app;

import controllers.AutoModeController;
import controllers.MenuController;
import controllers.SettingsController;
import controllers.StepModeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private Stage primaryStage;
    private static Settings settings;
    private static Simulator simulator;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        settings = new Settings();
        openMainMenu();
        primaryStage.show();
    }

    public void openMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("SMO");

            MenuController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openStepModeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/StepModelWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("SMO Step Mode");

            StepModeController controller = loader.getController();
            controller.setMainApp(this);
            controller.setSettings(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openAutoModeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AutoModeWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("SMO Auto Mode");

            AutoModeController controller = loader.getController();
            controller.setMainApp(this);
            controller.setSettings(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSettingsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SettingsWindow.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("SMO Settings");

            SettingsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setSettings(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);

//        for (int i = 1; i <= 15; i++) {
//            for (int j = 1; j <= 15; j++) {
//                for (int k = 6; k <= 5; k++) {
//                    for (int l = 0; l < 10; l++) {
//                        Settings settings = new Settings();
//                        settings.setSourceCount(i);
//                        settings.setBufferSize(j);
//                        settings.setDeviceCount(k);
//                        simulator = new Simulator(settings);
//                        simulator.simulate();
//                    }
//                    System.out.println(" ");
//                }
//            }
//        }
    }
}
