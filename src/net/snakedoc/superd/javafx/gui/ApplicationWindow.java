/*******************************************************************************
 *  Copyright 2013 Jason Sipula                                                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd.javafx.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.system.SysInfo;
import net.snakedoc.superd.javafx.gui.controller.ThreadDedupe;
import net.snakedoc.superd.javafx.gui.model.TableData;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 
 * JavaFX implementation of a superD GUI.
 *
 */
public class ApplicationWindow extends Application {
    
    /**
     * COLOR SCHEME:
     * 
     * FF9500   BF8430  A66100  FFB040  FFC573
     * 
     * 0C5DA5   26537C  043A6B  408DD2  679FD2
     * 
     */
    
    private static final String TOP_STYLE = "-fx-background-color: #679FD2;";
    @SuppressWarnings("unused")
    private static final String CENTER_STYLE = "-fx-background-color: #679FD2;";
    private static final String LABEL_STYLE = "-fx-background-color: #FFC573";
    private final DropShadow shadow = new DropShadow();
    
    private static TextField targetTextField = null;
    private static TextField delimiterTextField = null;
    private static Label hashAlgoLabel = null;
    
    private Config cfg = new Config("props/superD.properties");
    
    private volatile static ObservableList<TableData> data = FXCollections.observableArrayList();
    @SuppressWarnings("rawtypes")
    private static TableView table = new TableView();
    
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
    
    public static String getTargetTextField() {
        if (targetTextField != null) {
            return targetTextField.getText();
        } else {
            return "";
        }
    }
    
    public static void setTargetTextField(String t) {
        if (targetTextField != null) {
            targetTextField.setText(t);
        }
    }
    
    public static String getDelimiterTextField() {
        if (delimiterTextField != null) {
            return delimiterTextField.getText();
        } else {
            return "";
        }
    }
    
    public static void setDelimiterTextField(String d) {
        if (delimiterTextField != null) {
            delimiterTextField.setText(d);
        }
    }
    
    @SuppressWarnings("unused")
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
        
        ActionButton actionButton = new ActionButton();
        
        // Setup a VBox for the stacked buttons
        VBox rightButtonVBox = new VBox();
        rightButtonVBox.setPadding(new Insets(0));
        rightButtonVBox.setSpacing(7);
        rightButtonVBox.setStyle(TOP_STYLE);
        
        final Button buttonBrowse = new Button("Browse");
        buttonBrowse.setEffect(shadow);
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
        buttonAddBrowse.setEffect(shadow);
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
                    targetTextField.appendText(delimiterTextField + file.getAbsolutePath());
                } else {
                    targetTextField.setText(file.getAbsolutePath());
                }
            }
            
        });
        
        rightButtonVBox.getChildren().addAll(buttonBrowse, buttonAddBrowse);
        
        rightButtonHBox.getChildren().addAll(rightButtonVBox, actionButton.getActionButton());
        
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
        
        targetTextField = new TextField();
        targetTextField.setEffect(shadow);
        targetTextField.setMinHeight(30);
        targetTextField.setMinWidth(700);
        
        delimiterTextField = new TextField();
        delimiterTextField.setEffect(shadow);
        delimiterTextField.setMinHeight(30);
        delimiterTextField.setMinWidth(25);
        delimiterTextField.setMaxWidth(25);
        try {
            String d = cfg.getConfig("ROOT_DEL");
            if (d.equalsIgnoreCase("") || d.equalsIgnoreCase(null)) {
                delimiterTextField.setText(";;");
            } else {
                delimiterTextField.setText(d);
            }
        } catch (ConfigException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        final Button buttonDelimiter = new Button("Set Delimiter");
        buttonDelimiter.setEffect(shadow);
        buttonDelimiter.setPrefSize(100, 30);
        buttonDelimiter.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {

                cfg.setConfig("ROOT_DEL", delimiterTextField.getText(), false);
                
            }
            
        });
        
        final VBox hashAlgoVBox = new VBox();
        hashAlgoVBox.setPadding(new Insets(0));
        hashAlgoVBox.setSpacing(7);
        hashAlgoVBox.setStyle(TOP_STYLE);
        
        final BorderPane hashAlgoBorder = new BorderPane();
        hashAlgoBorder.setPadding(new Insets(0));
        
        final BorderPane hashAlgoBorderCur = new BorderPane();
        hashAlgoBorderCur.setPadding(new Insets(0));
        
        final Label labelHashAlgo = new Label(" Hash Algorithm ");
        labelHashAlgo.setStyle(LABEL_STYLE);
        
        final VBox centerHashAlgoVBox = new VBox();
        centerHashAlgoVBox.setPadding(new Insets(0));
        centerHashAlgoVBox.setSpacing(0);
        centerHashAlgoVBox.setStyle(TOP_STYLE);
        
        hashAlgoLabel = new Label(" " + this.getRecAlgo() + " ");
        hashAlgoLabel.setEffect(shadow);
        hashAlgoLabel.setMinHeight(30);
        hashAlgoLabel.setStyle(LABEL_STYLE);
        
        hashAlgoBorder.setCenter(labelHashAlgo);
        hashAlgoBorderCur.setCenter(hashAlgoLabel);
        
       // centerHashAlgoVBox.getChildren().addAll(hashAlgoBorder, hashAlgoBorderCur);
        
        /**
         * File Hashes used:
         * MD2     - Fastest, but least secure/reliable. Has well known methods of forcibly  
         *                  causing hash collisions and is generally not good for highly secure 
         *                  or important data.
         * MD5     - Still quite fast, and more secure/reliable than MD2, but has 
         *                  well known methods of forcibly causing hash collisions 
         *                  and is generally not good for highly secure or important data.
         * SHA-1   - Quite fast while still decently secure/reliable. 
         * SHA-256 - Decently fast while still being quite secure/reliable. Recommended for 32 bit Hardware/OS.
         * SHA-384 - Compromise between SHA-256 and SHA-512, medium performance 
         *                  while being very secure/reliable.
         * SHA-512 - Slowest, but most secure/reliable. 
         *                  Found to run faster on 64 bit Hardware/OS than SHA-256. 
         *                  Recommended for 64 bit Hardware/OS.
         */
        final Slider sliderHashAlgo = new Slider();
        sliderHashAlgo.setMin(10);
        sliderHashAlgo.setMax(60);
        sliderHashAlgo.setValue(50);
        sliderHashAlgo.setMinorTickCount(0);
        sliderHashAlgo.setMajorTickUnit(10);
        sliderHashAlgo.setBlockIncrement(10);
        sliderHashAlgo.setShowTickMarks(true);
        sliderHashAlgo.setSnapToTicks(true);
        
        sliderHashAlgo.valueProperty().addListener(new ChangeListener<Number>() {
           public void changed (ObservableValue<? extends Number> ov,
                   Number old_val, Number new_val) {
               
           }
        });
        
        centerHashAlgoVBox.getChildren().addAll(sliderHashAlgo, hashAlgoBorder);
        
        HBox sliderHBox = new HBox();
        sliderHBox.setPadding(new Insets(0));
        sliderHBox.setSpacing(7);
        sliderHBox.setStyle(TOP_STYLE);
        
        sliderHBox.getChildren().addAll(hashAlgoBorderCur, centerHashAlgoVBox); //sliderHashAlgo);
        
        //hashAlgoVBox.getChildren().addAll(centerHashAlgoVBox, sliderHashAlgo);
        
        delimiterHBox.getChildren().addAll(delimiterTextField, buttonDelimiter, sliderHBox);//hashAlgoVBox);
        
        centerTextVBox.getChildren().addAll(targetTextField, delimiterHBox);
        
        centerTextHBox.getChildren().addAll(centerTextVBox);
        
        // Create left label
        HBox leftLabelHBox = new HBox();
        leftLabelHBox.setPadding(new Insets(10, 10, 10, 10));
        leftLabelHBox.setSpacing(7);
        leftLabelHBox.setStyle(TOP_STYLE);
        
        VBox leftLabelVBox = new VBox();
        leftLabelVBox.setPadding(new Insets(0, 0, 0, 0));
        leftLabelVBox.setSpacing(5);
        leftLabelVBox.setStyle(TOP_STYLE);
        
        Label labelBrowse = new Label(" Target : ");
        labelBrowse.setMinHeight(30);
        labelBrowse.setStyle(LABEL_STYLE);
        labelBrowse.setEffect(shadow);
        
        Label labelDelimiter = new Label(" Delimiter : ");
        labelDelimiter.setMinHeight(30);
        labelDelimiter.setStyle(LABEL_STYLE);
        labelDelimiter.setEffect(shadow);
        
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

        table.setMinWidth(1000);
        table.setEditable(true);
        
        TableColumn fileNameCol = new TableColumn("File");
        fileNameCol.setMinWidth(125);
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("fileName"));
        fileNameCol.setCellFactory(
                new Callback<TableColumn<TableData, String>, TableCell<TableData, String>>() {
            @Override
            public TableCell<TableData, String> call(TableColumn<TableData, String> p) {
                return new CenteredOverrunTableCell();
            }
                });
        
        TableColumn directoryCol = new TableColumn("Directory");
        directoryCol.setMinWidth(375);
        directoryCol.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("directory"));
        directoryCol.setCellFactory(
                new Callback<TableColumn<TableData, String>, TableCell<TableData, String>>() {
            @Override
            public TableCell<TableData, String> call(TableColumn<TableData, String> p) {
                return new CenteredOverrunTableCell();
            }
                });
        
        TableColumn sizeCol = new TableColumn("Size (MB)");
        sizeCol.setMinWidth(45);
        sizeCol.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("size"));
        sizeCol.setCellFactory(
                new Callback<TableColumn<TableData, String>, TableCell<TableData, String>>() {
            @Override
            public TableCell<TableData, String> call(TableColumn<TableData, String> p) {
                return new CenteredOverrunTableCell();
            }
                });
              
        TableColumn fileHashCol = new TableColumn("Hash");
        fileHashCol.setMinWidth(375);
        fileHashCol.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("fileHash"));
        fileHashCol.setCellFactory(
                new Callback<TableColumn<TableData, String>, TableCell<TableData, String>>() {
            @Override
            public TableCell<TableData, String> call(TableColumn<TableData, String> p) {
                return new CenteredOverrunTableCell();
            }
                });
        
        table.getColumns().addAll(fileNameCol, sizeCol, directoryCol, fileHashCol);
        

        table.setItems(data);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        mainPane.setCenter(table);
        
        return mainPane;
        
    }
    
    /**
     * Gets the system's recommened hashing algorithm
     *      based on system cpu and os arch type.
     * 
     * @return "SHA-512" for 64 bit systems, and "SHA-256" for all others.
     */
    private String getRecAlgo() {
        
        // get system info
        SysInfo sys = new SysInfo();
        
        if ((sys.getOSArch().contains("64")) && (sys.getCPUArch().contains("64"))) {
            return "SHA-512";
        } else {
            return "SHA-256";
        }
        
    }
    
    public synchronized static void addData(TableData td) {
        data.add(td);
        table.scrollTo(table.getItems().size());
    }
    
    public synchronized static void clearData() {
        data.clear();
    }

}

class CenteredOverrunTableCell extends TableCell<TableData, String> {
    public CenteredOverrunTableCell() {
        this(null);
        this.setAlignment(Pos.CENTER);
    }

    public CenteredOverrunTableCell(String ellipsisString) {
        super();
        setTextOverrun(OverrunStyle.CENTER_WORD_ELLIPSIS);
        if (ellipsisString != null) {
            setEllipsisString(ellipsisString);
        }  
    }

    @Override protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item == null ? "" : item);
    }
}

class ActionButton {
    
    private final String ACTION_BUTTON_STYLE_GO = "-fx-base: #00FF00"; // GREEN
    private final String ACTION_BUTTON_STYLE_STOP = "-fx-base: #FF0000"; // RED
    private final String ACTION_BUTTON_STYLE_NEXT = "-fx-base: #FFFF00"; // YELLOW
    private final DropShadow shadow = new DropShadow();
    
    private ThreadDedupe threadDedupe = new ThreadDedupe();
    
    private final int BLANK  =  0;
    private final int DEDUPE =  1;
    private final int NEXT   =  2;
    private final int STOP   =  3;
    private final int ERROR  = -1;
    
    private int buttonState = 0;
    
    private Button actionButton;
    
    public Button getActionButton() {
     
        actionButton = new Button();
        setActionButtonState(1);
        actionButton.setEffect(this.shadow);
        actionButton.setPrefSize(100, 70);
        actionButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                operateButton();
                
                
            }
            
        });
        return actionButton;
    }
    
    private void operateButton() {
        
        switch (this.buttonState) {
        
        case BLANK:
            // do nothing
            setActionButtonState(BLANK);
            break;
        case DEDUPE:
            threadDedupe.setNewThread();
            threadDedupe.startDedupeTask();
            setActionButtonState(STOP);
            break;
        case NEXT:
            //TODO take to next page
            setActionButtonState(NEXT);
            break;
        case STOP:
            threadDedupe.stopDedupeTask();
            setActionButtonState(DEDUPE);
            break;
        default:
            // do nothing
            setActionButtonState(ERROR);
            break;
        
        }
        
    }
    
    private void setButtonState(int state) {
        
        this.buttonState = state;
        
    }
    
    private void setActionButtonState(final int op) {
        
        class ActionButtonState {
            
            private void validateOp() {
                
                switch (op) {
                
                case BLANK:
                    doBLANK();
                    break;
                case DEDUPE:
                    doDEDUPE();
                    break;
                case NEXT:
                    doNEXT();
                    break;
                case STOP:
                    doSTOP();
                    break;
                default:
                    doERROR();
                    break;
                
                }
                
            }
            
            private void doBLANK() {
                
                actionButton.setText("");
                actionButton.setStyle(ACTION_BUTTON_STYLE_GO);
                setButtonState(BLANK);
                
            }
            
            private void doDEDUPE() {
                
                actionButton.setText("Dedupe!");
                actionButton.setStyle(ACTION_BUTTON_STYLE_GO);
                setButtonState(DEDUPE);
                
            }
            
            private void doNEXT() {
                
                actionButton.setText("Next!");
                actionButton.setStyle(ACTION_BUTTON_STYLE_NEXT);
                setButtonState(NEXT);
                
            }
            
            private void doSTOP() {
                
                actionButton.setText("STOP!");
                actionButton.setStyle(ACTION_BUTTON_STYLE_STOP);
                setButtonState(STOP);
                
            }
            
            private void doERROR() {
                
                actionButton.setText("ERROR!");
                actionButton.setStyle(ACTION_BUTTON_STYLE_STOP);
                setButtonState(ERROR);
                
            }
            
        }
        
        ActionButtonState acbs = new ActionButtonState();
        acbs.validateOp();
    }
}
