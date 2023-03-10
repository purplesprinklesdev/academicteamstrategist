package personal.mstall.main.stages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import personal.mstall.main.teamLogic.Player;
import personal.mstall.main.teamLogic.Roster;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

public class RosterStage extends Stage {
    private static final String ROSTER_LABEL = "Add players by clicking a cell in the list:";
    private static final double ROSTER_LIST_HEIGHT = 500;
    private static final double ROSTER_LIST_WIDTH = 400;
    private static final double ROSTER_VBOX_GAP = 8;

    public RosterStage() {
        setTitle("Roster");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(ROSTER_VBOX_GAP);

        Label label = new Label(ROSTER_LABEL);

        ListView<String> list = new ListView<>();
        list.minHeight(ROSTER_LIST_HEIGHT);
        list.minWidth(ROSTER_LIST_WIDTH);
        list.setEditable(true);
        list.setCellFactory(TextFieldListCell.forListView());
        updateListView(list);

        Button add = new Button();
        add.setText("+");

        Button sub = new Button();
        sub.setText("-");

        HBox addSubRow = new HBox(10);
        addSubRow.getChildren().addAll(add, sub);

        Button save = new Button();
        save.setText("Save");

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(label, list, addSubRow, save);
        Scene scene = new Scene(layout, ROSTER_LIST_WIDTH + 40, ROSTER_LIST_HEIGHT + 30);
        setScene(scene);
        setResizable(false);

        // -- Action Handlers -- //
        add.setOnAction(e -> {
            try {
                ObservableList<String> oList = list.getItems();
                oList.add("New Player");
                list.setItems(oList);
            }
            catch (Exception ex) {}
        });
        
        sub.setOnAction(e -> {
            try {
                ObservableList<String> oList = list.getItems();
                int i = list.getSelectionModel().getSelectedIndex();
                oList.remove(i);
                list.setItems(oList);
            } catch(Exception ex) {
                Alert alert = new Alert(AlertType.ERROR, "No element selected. Please select an element that is to be removed.", ButtonType.OK);
                alert.showAndWait();
            }
            
        });

        save.setOnAction(e -> {
            if(save(list))
                close();
        });

        setOnCloseRequest(e -> {
            e.consume();

            // Check for changes
            ObservableList<String> oList = list.getItems();
            boolean changed = false;
            for (int i = 0; i < oList.size(); i++) {
                if (oList.get(i) != Roster.roster.players.get(i).name)
                    changed = true;
            }
            
            if (!changed) {
                close();
                return;
            }
            
            CloseWarning warning = new CloseWarning();
            switch (warning.show()) {
                case SAVE:
                    if(save(list))
                        close();
                    break;
                case DISCARD:
                    close();
                    break;
                default:
                    break;
            };
        });
    }

    private boolean save(ListView<String> list) {
        ObservableList<String> oList = list.getItems();

        ArrayList<String> playerNames = new ArrayList<>();

        // If the save operation gets aborted midway through, 
        // then this way we aren't overwriting roster.players
        ArrayList<Player> tempPlayers = Roster.roster.players;

        System.out.println("SAVING...");

        for (Player player : tempPlayers) {
            playerNames.add(player.name);
        }


        HashSet<String> dupeCheck = new HashSet<>();
        // Adding players which are found in the new list
        for (int i = 0; i < oList.size(); i++) {
            String name = oList.get(i);

            boolean unique = dupeCheck.add(name);
            if(!unique) {
                Alert duplicates = new Alert(AlertType.ERROR, "Two or more players in the Roster have the name, \"" + name + "\"\nNames must be unique. Please correct this issue.", ButtonType.OK);
                duplicates.showAndWait();
                System.out.println("SAVE FAILED: NAME DUPLICATE ERROR");
                return false;
            }

            if(!playerNames.contains(name)) {
                Player newPlayer = new Player(tempPlayers.size() + 1, name);
                tempPlayers.add(newPlayer);
            } else {
                playerNames.remove(name);
            }
        }

        // Removing players that aren't found in the new list
        if(playerNames.size() != 0) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Some players have been removed from the roster. Are you sure that you'd like to save?\nThis action cannot be undone.", ButtonType.NO, ButtonType.YES);
            ButtonType result = confirm.showAndWait().orElse(ButtonType.NO);

            if(result == ButtonType.NO) {
                System.out.println("SAVE FAILED: ABORTED BY USER");
                return false;
            }

            // Go through tempPlayers, (using iterator to avoid exception) and remove players that match the list of removed players
            Iterator<Player> it = tempPlayers.iterator();
            while(it.hasNext()) {
                Player player = it.next();
                if (playerNames.contains(player.name))
                    it.remove();
            }
        }

        Roster.roster.players = tempPlayers;

        boolean save = SaveManager.Save(Roster.roster, FileType.ROSTER);
        if(save) {
            System.out.println("SAVED SUCCESSFULLY");
            return true;
        } else {
            System.out.println("SAVING FAILED: FILE ERROR");
            Alert error = new Alert(AlertType.ERROR, "Saving failed because a file error occured. Files were not updated.", ButtonType.OK);
            error.showAndWait();
            return false;
        }
    }

    private void updateListView(ListView<String> list) {
        ObservableList<String> oList = FXCollections.observableArrayList();
        ArrayList<Player> players = Roster.roster.players;
        for (int i = 0; i < players.size(); i++) {
            oList.add(players.get(i).name);
        }

        list.setItems(oList);
    }
}

