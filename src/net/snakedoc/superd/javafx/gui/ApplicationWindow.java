package net.snakedoc.superd.javafx.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.superd.javafx.gui.controller.ThreadMonitor;
import net.snakedoc.superd.javafx.gui.model.TableData;
import net.snakedoc.superd.launcher.DedupeR;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
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
    
    private TextField targetTextField = null;
    private TextField delimiterTextField = null;
    
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
                    targetTextField.appendText(";;" + file.getAbsolutePath());
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
        
        this.targetTextField = new TextField();
        this.targetTextField.setEffect(shadow);
        this.targetTextField.setMinWidth(700);
        
        this.delimiterTextField = new TextField();
        this.delimiterTextField.setEffect(shadow);
        this.delimiterTextField.setMinWidth(25);
        this.delimiterTextField.setMaxWidth(25);
        try {
            this.delimiterTextField.setText(cfg.getConfig("ROOT_DEL"));
        } catch (ConfigException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        final Button buttonDelimiter = new Button("Set Delimiter");
        buttonDelimiter.setEffect(shadow);
 /*       buttonDelimiter.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {

                cfg.setConfig("ROOT_DEL", delimiterTextField.getText(), false);
                
            }
            
        });
   */     
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
        labelBrowse.setStyle(LABEL_STYLE);
        labelBrowse.setEffect(shadow);
        
        Label labelDelimiter = new Label("Delimiter:");
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
        
/*        TableColumn hashAlgoCol = new TableColumn("Hash Algo");
        hashAlgoCol.setMinWidth(80);
        hashAlgoCol.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("hashAlgo"));
        hashAlgoCol.setCellFactory(
                new Callback<TableColumn<TableData, String>, TableCell<TableData, String>>() {
            @Override
            public TableCell<TableData, String> call(TableColumn<TableData, String> p) {
                return new CenteredOverrunTableCell();
            }
                });
*/        
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
    
    public synchronized static void addData(TableData td) {
        data.add(td);
        table.scrollTo(table.getItems().size());
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
            setActionButtonState(STOP);
            ThreadMonitor.getInstance().startDedupeTask();
            break;
        case NEXT:
            //TODO take to next page
            setActionButtonState(NEXT);
            break;
        case STOP:
            ThreadMonitor.getInstance().stopDedupeTask();
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
