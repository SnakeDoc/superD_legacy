package net.snakedoc.superd.javafx.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.superd.javafx.gui.model.TableData;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * 
 * JavaFX implementation of a superD GUI.
 *
 */
public class ApplicationWindow extends Application {
    
    private static String TOP_STYLE = "-fx-background-color: #336699;";
    private static String CENTER_STYLE = "-fx-background-color: #113B63;";
    
    private TextField targetTextField = null;
    private TextField delimiterTextField = null;
    
    private Config cfg = new Config("props/superD.properties");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(ApplicationWindow.class, args);
    }
    
    /* (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        
        // use a border pane as the root for scene
        BorderPane border = new BorderPane();
        border.setMinSize(1000, 600);
        
        // set top of window
        border.setTop(this.getTop());
        
        // set center of window
        border.setCenter(this.getCenter());
        
        Scene scene = new Scene(border);
        primaryStage.setScene(scene);
        primaryStage.setTitle("superD | A File (super)Deduplicator");
        Image ico = null;
        
        try {
            ico = new Image(new FileInputStream("resources/logo.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        if (ico != null) {
            primaryStage.getIcons().add(ico);
        }
        
        primaryStage.show();
        
    }
    
    public String getTargetTextField() {
        if (this.targetTextField != null) {
            return this.escapeDirs(this.targetTextField.getText());
        } else {
            return "";
        }
    }
    
    public void setTargetTextField(String targetTextField) {
        if (this.targetTextField != null) {
            this.targetTextField.setText(targetTextField);
        }
    }
    
    public String getDelimiterTextField() {
        if (this.delimiterTextField != null) {
            return this.escapeDirs(this.delimiterTextField.getText());
        } else {
            return "";
        }
    }
    
    public void setDelimiterTextField(String delimiterTextField) {
        if (this.delimiterTextField != null) {
            this.delimiterTextField.setText(delimiterTextField);
        }
    }
    
    private String escapeDirs(String raw) {
        System.out.println("escape");
        return raw.replace("\\", "/");
    }
    
    /**
     * Builds the top portion of the window. 
     * 
     * @return BorderPane of formated top portion window
     */
    private BorderPane getTop() {
        
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle(TOP_STYLE);
        
        // HBox to hold buttons on top-right side
        HBox rightButtonHBox = new HBox();
        rightButtonHBox.setPadding(new Insets(10, 10, 10, 5));
        rightButtonHBox.setSpacing(7);
        rightButtonHBox.setStyle(TOP_STYLE);
        
        final Button buttonDedupe = new Button("Dedupe!");
        buttonDedupe.setPrefSize(100, 70);
        buttonDedupe.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                buttonDedupe.setDisable(true);
                
            }
            
        });
        
        // Setup a VBox for the stacked buttons
        VBox rightButtonVBox = new VBox();
        rightButtonVBox.setPadding(new Insets(0));
        rightButtonVBox.setSpacing(7);
        rightButtonVBox.setStyle(TOP_STYLE);
        
        final Button buttonBrowse = new Button("Browse");
        buttonBrowse.setPrefSize(100, 30);
        
        buttonBrowse.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory To Deduplicate");

                Stage dialog = new Stage();
                BorderPane bp = new BorderPane();
                bp.setMinSize(400, 400);
                Scene scene = new Scene(bp);
                dialog.setScene(scene);
                File file = directoryChooser.showDialog(dialog);
                
                cfg.setConfig("ROOT", file.getAbsolutePath(), false);
                
                targetTextField.setText(file.getAbsolutePath());
                
            }
            
        });
        
        final Button buttonAddBrowse = new Button("Add");
        buttonAddBrowse.setPrefSize(100, 30);
        
        buttonAddBrowse.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Another Directory To Deduplicate");

                Stage dialog = new Stage();
                BorderPane bp = new BorderPane();
                bp.setMinSize(400, 400);
                Scene scene = new Scene(bp);
                dialog.setScene(scene);
                File file = directoryChooser.showDialog(dialog);
                
                if (!(targetTextField.getText().equalsIgnoreCase(""))) {
                    targetTextField.appendText(";;" + file.getAbsolutePath());
                } else {
                    targetTextField.setText(file.getAbsolutePath());
                }
            }
            
        });
        
        rightButtonVBox.getChildren().addAll(buttonBrowse, buttonAddBrowse);
        
        rightButtonHBox.getChildren().addAll(rightButtonVBox, buttonDedupe);
        
        // Create centered Text Field
        HBox centerTextHBox = new HBox();
        centerTextHBox.setPadding(new Insets(10, 5, 10, 5));
        centerTextHBox.setSpacing(7);
        centerTextHBox.setStyle(TOP_STYLE);
        
        VBox centerTextVBox = new VBox();
        centerTextVBox.setPadding(new Insets(0));
        centerTextVBox.setSpacing(7);
        centerTextVBox.setStyle(TOP_STYLE);
        
        HBox delimiterHBox = new HBox();
        delimiterHBox.setPadding(new Insets(0));
        delimiterHBox.setSpacing(7);
        delimiterHBox.setStyle(TOP_STYLE);
        
        this.targetTextField = new TextField();
        this.targetTextField.setMinWidth(700);
        
        this.delimiterTextField = new TextField();
        this.delimiterTextField.setMinWidth(25);
        this.delimiterTextField.setMaxWidth(25);
        
        final Button buttonDelimiter = new Button("Set Delimiter");
        buttonDelimiter.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {

                cfg.setConfig("ROOT_DIL", delimiterTextField.getText(), false);
                
            }
            
        });
        
        delimiterHBox.getChildren().addAll(this.delimiterTextField, buttonDelimiter);
        
        centerTextVBox.getChildren().addAll(this.targetTextField, delimiterHBox);
        
        centerTextHBox.getChildren().addAll(centerTextVBox);
        
        // Create left label
        HBox leftLabelHBox = new HBox();
        leftLabelHBox.setPadding(new Insets(10, 10, 10, 10));
        leftLabelHBox.setSpacing(7);
        leftLabelHBox.setStyle(TOP_STYLE);
        
        VBox leftLabelVBox = new VBox();
        leftLabelVBox.setPadding(new Insets(5, 0, 5, 0));
        leftLabelVBox.setSpacing(10);
        leftLabelVBox.setStyle(TOP_STYLE);
        
        Label labelBrowse = new Label("Target:");
        
        Label labelDelimiter = new Label("Delimiter:");
        
        leftLabelVBox.getChildren().addAll(labelBrowse, labelDelimiter);
        
        leftLabelHBox.getChildren().addAll(leftLabelVBox);
        
        // set positions in Border Pane
        mainPane.setRight(rightButtonHBox);
        mainPane.setCenter(centerTextHBox);
        mainPane.setLeft(leftLabelHBox);
   
        return mainPane;
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private BorderPane getCenter() {
        
        BorderPane mainPane = new BorderPane();

        TableView<String> table = new TableView<String>();
        table.setMaxWidth(1000);
        table.setEditable(false);
        
        TableColumn fileNameCol = new TableColumn("File Name");
        fileNameCol.setMinWidth(100);
        
        TableColumn directoryCol = new TableColumn("Directory");
        directoryCol.setMinWidth(375);
        
        TableColumn sizeCol = new TableColumn("Size");
        sizeCol.setMinWidth(50);
        
        TableColumn hashAlgoCol = new TableColumn("Hash Algo");
        hashAlgoCol.setMinWidth(100);
        
        TableColumn fileHashCol = new TableColumn("File Hash");
        fileHashCol.setMinWidth(375);
        
        table.getColumns().addAll(fileNameCol, directoryCol, sizeCol, hashAlgoCol, fileHashCol);
        
        // set some blank data so our table will be visible
        ObservableList data = 
                FXCollections.observableArrayList(
                    new TableData(" ", " ", " ", " ", " ")
                );
        table.setItems(data);
        
        mainPane.setCenter(table);
        
        return mainPane;
        
    }

}
