package personal.mstall.main.stages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personal.mstall.main.teamLogic.TeamComp;
import personal.mstall.main.teamLogic.TeamCompSaves;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;
import personal.mstall.main.util.DateFormat;

public class CompsMenuStage extends Stage {
    private static final String COMPSMENU_LABEL = "Select a sheet to edit, or add a new one.";
    private static final double COMPSMENU_LIST_HEIGHT = 500;
    private static final double COMPSMENU_LIST_WIDTH = 400;
    private static final double COMPSMENU_VBOX_GAP = 8;

    public CompsMenuStage() {
        setTitle("Team Setups Menu");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(COMPSMENU_VBOX_GAP);

        Label label = new Label(COMPSMENU_LABEL);

        ListView<String> list = new ListView<>();
        list.minHeight(COMPSMENU_LIST_HEIGHT);
        list.minWidth(COMPSMENU_LIST_WIDTH);
        list.setEditable(false);
        updateListView(list);

        Button save = new Button();
        save.setText("Save Current Team Setup");

        Button load = new Button();
        load.setText("Load Selected");

        Button remove = new Button();
        remove.setText("Remove");

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(save, load, remove);

        Button close = new Button();
        close.setText("Close");

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(label, list, buttonRow, close);
        Scene scene = new Scene(layout, COMPSMENU_LIST_WIDTH + 40, COMPSMENU_LIST_HEIGHT + 30);
        setScene(scene);
        setResizable(false);

        // -- Action Handlers -- //

        save.setOnAction(e -> {
            System.out.println("SAVING...");

            TeamComp current = TeamComp.getTeamCompFromChoiceBoxes();
            current.name = DateFormat.getCurrentDate(DateFormat.Type.DAYANDTIME);

            TeamCompSaves.teamCompSaves.comps.add(current);
            boolean saveResult = SaveManager.Save(TeamCompSaves.teamCompSaves, FileType.TEAMCOMPS);

            if (saveResult)
                System.out.println("SAVED SUCCESSFULLY");
            else {
                System.out.println("SAVING FAILED: FILE ERROR");
                TeamCompSaves.teamCompSaves.comps.remove(current);
                Alert error = new Alert(AlertType.ERROR,
                    "Saving failed because a file error occured. Files were not updated.", ButtonType.OK);
                error.showAndWait();
            }

            updateListView(list);
        });

        load.setOnAction(e -> {
            try {
                ObservableList<String> oList = list.getItems();
                int i = list.getSelectionModel().getSelectedIndex();
                String targetName = oList.get(i);

                for (TeamComp comp : TeamCompSaves.teamCompSaves.comps) {
                    if (comp.name == targetName) {
                        TeamComp.setChoiceBoxesWithTeamComp(comp);
                        break;
                    }
                }

                close();
            } catch(Exception ex) {
                Alert alert = new Alert(AlertType.ERROR, "No element selected. Please select an element that is to be edited.", ButtonType.OK);
                alert.showAndWait();
            }
        });
        remove.setOnAction(e -> {
            try {
                ObservableList<String> oList = list.getItems();
                int i = list.getSelectionModel().getSelectedIndex();
                String targetName = oList.get(i);

                for (TeamComp comp : TeamCompSaves.teamCompSaves.comps) {
                    if (comp.name == targetName) {
                        TeamCompSaves.teamCompSaves.comps.remove(comp);
                        break;
                    }
                }

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
        for (TeamComp comp : TeamCompSaves.teamCompSaves.comps) {
            oList.add(comp.name);
        }
        list.setItems(oList);
    }
}
