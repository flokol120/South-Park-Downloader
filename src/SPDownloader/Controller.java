package SPDownloader;

import SPEditor.Editor;
import SPTools.DB;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static SPDownloader.Downloader.*;
import static SPDownloader.Main.stage;

public class Controller {

    private ArrayList<String> selectedEpisodes = new ArrayList<>();

    @FXML
    private TabPane seasonTabs;

    @FXML
    private javafx.scene.control.ListView<String> selectedList;

    @FXML
    private TextArea console;

    /*@FXML
    private Button download;
    @FXML
    private Button stop;
    @FXML
    private Button databaseEditor;
    @FXML
    private Button selectAll;
    @FXML
    private Button deselectAll;*/

    @FXML
    private CheckBox languageEnglish;
    @FXML
    private CheckBox languageGerman;

    /*@FXML
    private ChoiceBox threads;*/

    @FXML
    private SplitPane upperSplit;
    /*@FXML
    private SplitPane split;

    @FXML
    private AnchorPane lower;*/

    @Deprecated
    @FXML
    public void initialize() throws SQLException {
        updatePanes(true);
    }

    @Deprecated
    private void updatePanes(boolean selectAll) throws SQLException {
        //clear all
        seasonTabs.getTabs().clear();
        selectedEpisodes.clear();
        selectedList.getItems().clear();
        //init variables
        DB db = new DB();
        ArrayList<String> s = db.getSeasonCount();

        for (final String season : s) {
            //add Tab
            Tab test = new Tab("Season " + season);
            //add ListView
            AnchorPane pane = new AnchorPane();
            ListView<String> listView = new ListView<>();
            listView.setMinHeight(seasonTabs.getMinHeight());
            listView.setMinWidth(seasonTabs.getMinWidth());
            listView.setItems(db.getEpisodesBySeason(season));
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


            listView.setCellFactory(CheckBoxListCell.forListView(item -> {
                BooleanProperty observable = new SimpleBooleanProperty();
                if (selectAll) {
                    observable.setValue(true);
                } else {
                    observable.setValue(false);
                }
                String temp = item + "," + "Season " + season;
                if (!selectedEpisodes.contains(temp) && selectAll) {
                    selectedEpisodes.add(temp);
                }
                selectedList.setItems(FXCollections.observableArrayList(selectedEpisodes));
                observable.addListener((observable1, oldValue, newValue) -> {
                    if (selectedEpisodes.contains(item + "," + seasonTabs.getSelectionModel().getSelectedItem().getText())) {
                        selectedEpisodes.remove(item + "," + seasonTabs.getSelectionModel().getSelectedItem().getText());
                        selectedList.setItems(FXCollections.observableArrayList(selectedEpisodes));
                    } else {
                        selectedEpisodes.add(item + "," + seasonTabs.getSelectionModel().getSelectedItem().getText());
                        selectedList.setItems(FXCollections.observableArrayList(selectedEpisodes));
                    }
                });
                return observable;
            }));
            test.setContent(pane);
            pane.getChildren().add(listView);

            Button deselectButton = new Button();
            deselectButton.setLayoutX(250);
            deselectButton.setLayoutX(250);
            deselectButton.setText("deselect all from\n Season " + season);
            deselectButton.setOnAction(event -> listView.setCellFactory(CheckBoxListCell.forListView(param -> {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.setValue(false);
                for (int j = 0; j < selectedEpisodes.size(); j++) {
                    if (selectedEpisodes.get(j).contains("Season " + season)) {
                        selectedEpisodes.remove(j);
                    }
                }
                selectedList.setItems(FXCollections.observableArrayList(selectedEpisodes));
                return observable;
            })));
            pane.getChildren().add(deselectButton);

            Button selectButton = new Button();
            selectButton.setLayoutX(250);
            selectButton.setLayoutY(50);
            selectButton.setText("select all from\n Season " + season);
            selectButton.setOnAction(event -> listView.setCellFactory(CheckBoxListCell.forListView(param -> {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.setValue(true);
                selectedEpisodes.add(param + ",Season " + season);
                ObservableList list = FXCollections.observableArrayList(selectedEpisodes);
                selectedList.setItems(list);
                return observable;
            })));
            pane.getChildren().add(selectButton);
            seasonTabs.getTabs().add(test);
        }
    }

    @FXML
    public void onGerman() {
        //languageEnglish.setSelected(false);
    }

    @FXML
    public void onEnglish() {
        //languageGerman.setSelected(false);
    }

    @FXML
    public void onDatabaseEditor() throws Exception {
        Editor editor = new Editor();
        editor.start(stage);
    }


    @FXML
    public void onDownload() {
        if (!isDownloading) {
            upperSplit.setDividerPositions(0.3);
            downloader(languageGerman, languageEnglish, selectedEpisodes, console, upperSplit);
        } else {
            JOptionPane.showMessageDialog(null, "A download process is already in the action. Please wait until it is finished or stop it!");
        }
    }

    @Deprecated
    @FXML
    public void onStop() {
        upperSplit.setDividerPositions(0.5);
        isDownloading = false;
        //t.stop();
    }

    @Deprecated
    @FXML
    public void onSelectAll() throws SQLException {
        updatePanes(true);
    }

    @Deprecated
    @FXML
    public void onDeselectAll() throws SQLException {
        updatePanes(false);
    }

}
