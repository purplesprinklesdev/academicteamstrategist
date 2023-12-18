package personal.mstall.main.stages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import personal.mstall.main.scoreSheet.ScoreSheets;
import personal.mstall.main.scoreSheet.Sheet;

public class SheetsMenuStage extends Stage {
    private static final String SHEETSMENU_LABEL = "Select a sheet to edit, or add a new one.";
    private static final double SHEETSMENU_LIST_HEIGHT = 500;
    private static final double SHEETSMENU_LIST_WIDTH = 400;
    private static final double SHEETSMENU_VBOX_GAP = 8;

    public SheetsMenuStage() {
        setTitle("Score Sheets Menu");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(SHEETSMENU_VBOX_GAP);

        Label label = new Label(SHEETSMENU_LABEL);

        ListView<String> list = new ListView<>();
        list.minHeight(SHEETSMENU_LIST_HEIGHT);
        list.minWidth(SHEETSMENU_LIST_WIDTH);
        list.setEditable(false);
        updateListView(list);

        Button add = new Button();
        add.setText("+");

        Button edit = new Button();
        edit.setText("Edit");

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(add, edit);

        Button close = new Button();
        close.setText("Close");

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(label, list, buttonRow, close);
        Scene scene = new Scene(layout, SHEETSMENU_LIST_WIDTH + 40, SHEETSMENU_LIST_HEIGHT + 30);
        setScene(scene);
        setResizable(false);

        // -- Action Handlers -- //

        add.setOnAction(e -> {
            SheetViewerStage sheetViewerStage = new SheetViewerStage(new Sheet());
            sheetViewerStage.showAndWait();
            updateListView(list);
        });

        edit.setOnAction(e -> {
            try {
                ObservableList<String> oList = list.getItems();
                int i = list.getSelectionModel().getSelectedIndex();
                String targetName = oList.get(i);

                Sheet target = null;
                for (Sheet sheet : ScoreSheets.getSheets()) {
                    if (sheet.name == targetName) {
                        target = sheet;
                        break;
                    }
                }

                SheetViewerStage sheetViewerStage = new SheetViewerStage(target);
                sheetViewerStage.showAndWait();
                updateListView(list);
            } catch(Exception ex) {
                Alert alert = new Alert(AlertType.ERROR, "No element selected. Please select an element that is to be edited.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        close.setOnAction(e -> {
            close();
        });
    }

    private void updateListView(ListView<String> list) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        for (Sheet sheet : ScoreSheets.getSheets()) {
            oList.add(sheet.name);
        }
        list.setItems(oList);
    }
}
