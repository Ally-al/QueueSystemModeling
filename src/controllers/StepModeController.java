package controllers;

import app.*;
import app.analytics.StepModel;
import app.timeDiagram.CoordinateInfo;
import app.timeDiagram.TimeDiagram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;




public class StepModeController implements Initializable {
    private Main mainApp;
    private Settings settings;
    private Simulator simulator;
    private ObservableList<DataTableModel> sourceTableList ,bufferTableList, deviceTableList;
    public ArrayList<StepModel> steps;
    private Integer currentStep, allStepCount, currentRequestNumber;
    private Double currentTime;

    private CoordinateInfo coordinateInfo;



    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button exitButton;

    @FXML
    private Button updateButton;

    @FXML
    private Label sourceLabel;
    @FXML
    private Label timeLabel;

    @FXML
    private Label stepLabel;

    @FXML
    private Label allStepsLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Pane timeDiagramLayout;

    @FXML
    public void initialize() {
        currentStep = 0;
        allStepCount = 0;
        currentTime = 0.0;
        currentRequestNumber = 0;
    }

    @FXML
    void onClickNextButton(ActionEvent event) {
        if (currentStep >= allStepCount) {
            return;
        }

        currentStep++;
        stepLabel.setText(currentStep.toString());



        TimeDiagram tm = new TimeDiagram();
        JFreeChart jfchart = tm.getTimeDiagram(simulator);

        int width = 1200; // Ширина изображения
        int height = 500; // Высота изображения
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = bufferedImage.createGraphics();
        jfchart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
        g2.dispose();

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        timeDiagramLayout.getChildren().add(imageView);

        commitStep(currentStep);
    }

    @FXML
    void onClickExitButton(ActionEvent event) {
        mainApp.openMainMenu();
    }

    @FXML
    void onClickUpdateButton(ActionEvent event) {
        simulator = new Simulator(settings);
        coordinateInfo = simulator.getCoordinateInfo();
        //coordinateInfo =  new CoordinateInfo(settings.getSourceCount(), settings.getBufferSize(), settings.getDeviceCount());
        simulator.simulate();
        steps = new ArrayList<>();
        steps.add(null);
        steps.addAll(simulator.getAnalytics().getSteps());

        //goToStepButton.setDisable(false);
        nextButton.setDisable(false);
        //prevButton.setDisable(false);

        allStepCount = steps.size() - 1;
        allStepsLabel.setText(allStepCount.toString());

        refresh();
    }

//    @FXML
//    void onClickGoToStepButton(ActionEvent event) {
//        String tmpStepStr = goToStepField.getText();
//        int tmpStep;
//        try {
//            tmpStep = Integer.parseInt(tmpStepStr);
//        } catch (Exception e) {
//            goToStepField.requestFocus();
//            return;
//        }
//        if (tmpStep < 0 || tmpStep > allStepCount) {
//            goToStepField.requestFocus();
//            return;
//        }
//
//        refresh();
//        for (int i = 0; i < tmpStep; i++) {
//            onClickNextButton(event);
//        }
//    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        simulator = new Simulator(settings);
    }

    private void commitStep(Integer currentStep) {
        StepModel model = steps.get(currentStep);
        Pair<Integer, Request> data = model.getData();
        currentTime = model.getCurrentTime();
        timeLabel.setText(currentTime.toString());
        String message = switch (model.getAction()) {
            case NEW_REQUEST -> {
                currentRequestNumber++;

                sourceTableList.set(data.getKey(), new DataTableModel("Source" + (data.getKey() + 1), "Busy", (data.getValue().getSourceNumber() + 1) + "." + currentRequestNumber));
                coordinateInfo.addImpulse("S" + (data.getValue().getSourceNumber() + 1), currentTime, (data.getKey() + 1) + "." + data.getValue().getRequestNumber());
                yield " Создана заявка " + (data.getKey() + 1) + "." + data.getValue().getRequestNumber() + " источником " + (data.getValue().getSourceNumber() + 1);
            }
            case ADD_TO_BUFFER -> {
                bufferTableList.set(data.getKey(), new DataTableModel("Buffer" + (data.getKey() + 1), "Busy", (data.getValue().getSourceNumber() + 1) + "." + currentRequestNumber));
                coordinateInfo.upImpulse("B" + (data.getKey() + 1), currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                yield " Заявка " + (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber() + " была добавлена в буфер " + (data.getKey() + 1);
            }
            case REMOVE_FROM_BUFFER -> {
                bufferTableList.set(data.getKey(), new DataTableModel("Buffer" + (data.getKey() + 1), "Free", "-"));
                coordinateInfo.downImpulse("B" + (data.getKey() + 1), currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                coordinateInfo.addImpulse("Rej", currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                yield " Заявка " + (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber() + " ушла в отказ из буфера " + (data.getKey() + 1);
            }
            case GET_FROM_BUFFER -> {

                bufferTableList.set(data.getKey(), new DataTableModel("Buffer" + (data.getKey() + 1), "Free", "-"));
                coordinateInfo.downImpulse("B" + (data.getKey() + 1), currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                yield " Заявка " + (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber() + " была выбрана из буфера " + (data.getKey() + 1);
            }
            case REMOVE_FROM_DEVICE -> {

                coordinateInfo.downImpulse("D" + (data.getKey() + 1), currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                deviceTableList.set(data.getKey(), new DataTableModel("Device" + (data.getKey() + 1), "Free", "-"));
                yield " Заявка " + (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber() + " была обработана прибором " + (data.getKey() + 1);
            }
            case ADD_TO_DEVICE -> {

                coordinateInfo.upImpulse("D" + (data.getKey() + 1), currentTime, (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber());
                deviceTableList.set(data.getKey(), new DataTableModel("Device" + (data.getKey() + 1), "Busy", (data.getValue().getSourceNumber() + 1) + "." + currentRequestNumber));
                yield " Заявка " + (data.getValue().getSourceNumber() + 1) + "." + data.getValue().getRequestNumber() + " была добавлена на прибор " + (data.getKey() + 1);
            }
        };
        descriptionTextArea.setText(message);
    }

    private void refresh() {
        currentTime = 0.0;
        timeLabel.setText(currentTime.toString());

        descriptionTextArea.clear();

        currentRequestNumber = 0;

        currentStep = 0;
        stepLabel.setText(currentStep.toString());

        sourceTableList = FXCollections.observableArrayList();
        for (int i = 1; i <= settings.getSourceCount(); i++) {
            sourceTableList.add(new DataTableModel("Source"+i, "Free","-"));
        }

        bufferTableList = FXCollections.observableArrayList();
        for (int i = 1; i <= settings.getBufferSize(); i++) {
            bufferTableList.add(new DataTableModel("Buffer" + i, "Free", "-"));
        }

        deviceTableList = FXCollections.observableArrayList();
        for (int i = 1; i <= settings.getDeviceCount(); i++) {
            deviceTableList.add(new DataTableModel("Device" + i, "Free", "-"));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
