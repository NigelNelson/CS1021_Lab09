/*
 * Course: CS1021 - 021
 * Winter
 * Lab 9 - Final Project Continued
 * Name: Nigel Nelson
 * Created: 02/19/20
 */
package msoe.nelsonni.lab09;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * The class that is responsible for running the
 * Image manipulator window that the user sees
 */
public class Lab9 extends Application {

    /**
     * Integer value representing the x-offset
     */
    private static final int X_OFFSET = 1450;
    /**
     * Integer value representing the Y-offset
     */
    private static final int Y_OFFSET = 800;
    /**
     * Integer value representing primary width
     */
    private static final int PRIMARY_WIDTH = 750;
    /**
     * Integer value representing primary height
     */
    private static final int PRIMARY_HEIGHT = 750;
    /**
     * Integer value representing the filter width
     */
    private static final int FILTER_WIDTH = 357;
    /**
     * Integer value representing the filter height
     */
    private static final int FILTER_HEIGHT = 191;

    /**
     * Start method of Lab8 Application.
     * Implements a logger to log to a Lab8.txt file
     * Sets up primary stage and a filter kernel stage.
     * Sets up both controllers for both stages.
     *
     * @param primaryStage main stage of program
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader primaryLoader = new FXMLLoader();
        FXMLLoader kernelLoader = new FXMLLoader();

        Parent primaryRoot = primaryLoader.load(getClass().getResource("Lab9.fxml").
                openStream());
        primaryStage.setTitle("Image Manipulator");
        primaryStage.setScene(new Scene(primaryRoot, PRIMARY_WIDTH, PRIMARY_HEIGHT));
        primaryStage.setResizable(false);

        Stage filterKernelStage = new Stage();
        Parent filterKernelRoot = kernelLoader.load(getClass().getResource("KernelUI.fxml")
                .openStream());
        filterKernelStage.setTitle("Filter Kernel");
        filterKernelStage.setScene(new Scene(filterKernelRoot, FILTER_WIDTH, FILTER_HEIGHT));
        filterKernelStage.setResizable(false);
        filterKernelStage.setX(X_OFFSET);
        filterKernelStage.setY(Y_OFFSET);

        Lab9Controller lab9Controller = primaryLoader.getController();
        lab9Controller.setKernelStage(filterKernelStage);
        KernelController kernelController = kernelLoader.getController();
        kernelController.setStage(filterKernelStage);
        kernelController.setController(lab9Controller);

        primaryStage.show();
    }

    /**
     * The main method of the class that launches
     * the args
     * @param args ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
}
