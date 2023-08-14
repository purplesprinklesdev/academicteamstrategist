package personal.mstall.main.stages;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import personal.mstall.main.scoreSheet.ScoreSheets;
import personal.mstall.main.scoreSheet.Sheet;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

public class SheetViewerStage extends Stage {
    private static final double SHEETSVIEWER_HEIGHT = 500;
    private static final double SHEETSVIEWER_WIDTH = 470;

    private static final double SHEETSVIEWER_VBOX_GAP = 8;

    private static final double SHEETSVIEWER_NAME_CELL_WIDTH = 175;
    private static final double SHEETSVIEWER_CELL_WIDTH = 80;

    private static final String[] SHEET_TITLES = { "Player", "Section 1", "Section 2", "Alphabet", "Lightning" };
    private static final int SHEET_COL = 5;

    private boolean isNew = false;
    private String oldName;

    public SheetViewerStage(Sheet sheetObj) {
        oldName = sheetObj.name;
        if (oldName == "New Sheet")
            isNew = true;

        ArrayList<String[]> sheet = sheetObj.getSheet();

        setTitle("Score Sheets Menu");

        VBox layout = new VBox();
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.setSpacing(SHEETSVIEWER_VBOX_GAP);

        TextField nameField = new TextField();
        nameField.setText(sheetObj.name);

        TableView<String[]> table = initializeTable(FXCollections.observableArrayList(sheet));
        
        Button save = new Button();
        save.setText("Save");

        Button delete = new Button();
        delete.setText("Delete");

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(save, delete);

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(nameField, table, buttonRow);
        Scene scene = new Scene(layout, SHEETSVIEWER_WIDTH + 40, SHEETSVIEWER_HEIGHT);
        setScene(scene);
        setResizable(false);

        // -- Action Handlers -- //
        save.setOnAction(e -> {
            if (save(nameField.getText(), table))
                close();
        });

        delete.setOnAction(e -> {
            Alert deleteAlert = new Alert(AlertType.WARNING, "Are you sure you want to delete this score sheet?\n" + 
                "This cannot be undone.", ButtonType.YES, ButtonType.NO);
            ButtonType result = deleteAlert.showAndWait().orElse(ButtonType.NO);

            if (result == ButtonType.YES) {
                int i = ScoreSheets.scoreSheets.findIndex(oldName);
                ScoreSheets.scoreSheets.sheets.remove(i);

                boolean deleteSave = SaveManager.Save(ScoreSheets.scoreSheets, FileType.SCORESHEETS);
                saveReporter(deleteSave);
                close();
            }
        });

        setOnCloseRequest(e -> {
            e.consume();

            CloseWarning warning = new CloseWarning();
            switch (warning.show()) {
                case SAVE:
                    if (save(nameField.getText(), table))
                        close();
                    break;
                case DISCARD:
                    close();
                    break;
                default:
                    break;
            }
        });
    }

    private TableView<String[]> initializeTable(ObservableList<String[]> data) {
        TableView<String[]> table = new TableView<>();

        for (int i = 0; i < SHEET_COL; i++) {
            final int colNo = i;

            TableColumn<String[], String> tc = new TableColumn<>(SHEET_TITLES[i]);

            tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                // Black magic
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });

            if (colNo == 0) {
                tc.setPrefWidth(SHEETSVIEWER_NAME_CELL_WIDTH);
                tc.setEditable(false);
            } else {
                tc.setPrefWidth(SHEETSVIEWER_CELL_WIDTH);

                tc.setCellFactory(TextFieldTableCell.forTableColumn());
                tc.setEditable(true);

                // For some reason, table edits don't save into the table's Items.
                // We have to do this manually when an edit happens.
                // credit: Prosper Grateful - https://www.section.io/engineering-education/building-javafx-editable-table-views/#make-the-table-cells-editable
                tc.setOnEditCommit(e -> {
                    boolean valid = checkValidity(e.getNewValue(), colNo);
                    String result = valid ? e.getNewValue() : e.getOldValue();

                    e.getTableView().getItems().get(e.getTablePosition().getRow())[colNo] = result;
                });
            }
            table.getColumns().add(tc);
        }
        table.setItems(data);
        table.maxWidth(SHEETSVIEWER_WIDTH);
        table.prefHeight(SHEETSVIEWER_HEIGHT);
        table.setEditable(true);
        return table;
    }

    private boolean save(String name, TableView<String[]> table) {
        System.out.println("SAVING...");
        ObservableList<String[]> oList = table.getItems();

        boolean unique;
        try {
            unique = isUnique(name);
        } catch (Exception e) {
            return false;
        }

        ArrayList<String[]> data = new ArrayList<>(oList);
        checkValidity(data);

        Sheet sheet = new Sheet(name, data);

        boolean overwrite = !unique || !isNew;
        if (overwrite) { 
            // Remove the sheet to be replaced
            String replaceName = isNew ? name : oldName;
            int replace = ScoreSheets.scoreSheets.findIndex(replaceName);

            ScoreSheets.scoreSheets.sheets.remove(replace);
        }
        ScoreSheets.scoreSheets.sheets.add(sheet);

        boolean save = SaveManager.Save(ScoreSheets.scoreSheets, FileType.SCORESHEETS);
        saveReporter(save);
        return save;
    }

    private void saveReporter(boolean save) {
        if (save)
            System.out.println("SAVED SUCCESSFULLY");
        else {
            System.out.println("SAVING FAILED: FILE ERROR");
            Alert error = new Alert(AlertType.ERROR,
                    "Saving failed because a file error occured. Files were not updated.", ButtonType.OK);
            error.showAndWait();
        }
    }

    private boolean isUnique(String name) throws Exception {
        if (ScoreSheets.scoreSheets.find(name) != null) {
            Alert match = new Alert(AlertType.CONFIRMATION,
                    "The name of this sheet matches the name of an existing sheet.\n" +
                            "Would you like to overwrite the existing sheet?",
                    ButtonType.YES, ButtonType.NO);
            ButtonType result = match.showAndWait().orElse(ButtonType.NO);
            if (result == ButtonType.YES)
                return false;
            else {
                System.out.println("SAVING FAILED: ABORTED BY USER");
                throw new Exception();
            }
        }
        return true;
    }

    private boolean checkValidity(ArrayList<String[]> data) {
        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                if (i == 0)
                    continue;
                if (i <= 2) {
                    if (!row[i].matches("1[0-5]||[0-9]||-")) {
                        validityError();
                        return false;
                    }
                } else {
                    if (!row[i].matches("10||[0-9]||-")) {
                        validityError();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkValidity(String value, int colNo) {
        if (colNo == 0)
            return true;
        if (colNo <= 10) {
            if (!value.matches("1[0-5]||[0-9]||-")) {
                validityError(value);
                return false;
            }
        } else {
            if (!value.matches("10||[0-9]||-")) {
                validityError(value);
                return false;
            }
        }
        return true;
    }

    private void validityError() {
        Alert error = new Alert(AlertType.ERROR, "There are cells in the scoresheet with invalid values.\n" +
                "Sections 1 and 2 can be numbers 0 through 15 or \"-\", while Alphabet and Lightning can have numbers 0 through 10 or \"-\".",
                ButtonType.OK);
        error.showAndWait();
    }

    private void validityError(String value) {
        Alert error = new Alert(AlertType.ERROR, "\"" + value + "\" is not a valid value for this cell.\n" +
                "Sections 1 and 2 can be numbers 0 through 15 or \"-\", while Alphabet and Lightning can have numbers 0 through 10 or \"-\".",
                ButtonType.OK);
        error.showAndWait();
    }
}
