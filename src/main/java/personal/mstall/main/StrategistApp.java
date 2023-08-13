package personal.mstall.main;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import personal.mstall.main.scoreSheet.*;
import personal.mstall.main.teamLogic.*;
import personal.mstall.main.stages.*;


public class StrategistApp extends Application {
    private static final String WINDOW_TITLE = "Strategist";
    private static final double WINDOW_SIZE_X = 765;
    private static final double WINDOW_SIZE_Y = 650;

    private static final String MAINMENU_LABEL = "Academic Team Strategist";
    private static final double MAINMENU_VBOX_GAP = 8;
    private static final double THREEPANE_HBOX_GAP = 12;

    private static final double TEAMCOMPPANE_VBOX_GAP = 8;
    private static final double RIGHTPANE_VBOX_GAP = 12;

    private static final double CHOICEBOX_WIDTH = 200;
    private static final double BUTTONS_WIDTH = 250;

    private static final String SCORES_LABEL = "Expected Score (# of correct answers)";
    private static final String TOTALSCORE_LABEL = "Total: ";
    private static final String FIRSTSCORE_LABEL = "First Half: ";
    private static final String SECONDSCORE_LABEL = "Second Half: ";

    private ArrayList<ChoiceBox<String>> allChoiceBoxes = new ArrayList<>();
    public static boolean ignoreChoiceBoxEvents = false;

    private Label scoresLabel;
    private Label totalScore;
    private Label firstScore;
    private Label secondScore;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Roster.loadFromFile();
        ScoreSheets.loadFromFile();

        // -- Create Layouts and Elements -- //

        VBox mainMenu = new VBox();
        mainMenu.setPadding(new Insets(20, 20, 20, 20));
        mainMenu.setSpacing(MAINMENU_VBOX_GAP);

        Label topLabel = new Label(MAINMENU_LABEL);

        HBox threePaneMenu = new HBox();
        mainMenu.setPadding(new Insets(20, 20, 20, 20));
        mainMenu.setSpacing(THREEPANE_HBOX_GAP);

        VBox leftPane = new VBox();
        leftPane.setPadding(new Insets(0, THREEPANE_HBOX_GAP, 0, THREEPANE_HBOX_GAP));
        VBox centerPane = new VBox();
        centerPane.setPadding(new Insets(0, THREEPANE_HBOX_GAP, 0, THREEPANE_HBOX_GAP));
        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(0, THREEPANE_HBOX_GAP, 0, THREEPANE_HBOX_GAP));

        setupTeamCompPanes(leftPane, centerPane);

        rightPane.setSpacing(RIGHTPANE_VBOX_GAP);

        Button clear = new Button("Clear Team Setup");
        clear.setMinWidth(BUTTONS_WIDTH);

        Button manageSheets = new Button("Manage Scoresheets");
        manageSheets.setMinWidth(BUTTONS_WIDTH);

        Button manageRoster = new Button("Manage Roster");
        manageRoster.setMinWidth(BUTTONS_WIDTH);

        scoresLabel = new Label(SCORES_LABEL);
        totalScore = new Label(TOTALSCORE_LABEL);
        firstScore = new Label(FIRSTSCORE_LABEL);
        secondScore = new Label(SECONDSCORE_LABEL);

        Roster.updatePlayerAverages();
        refreshChoiceBoxItems();
        //checkChoiceBoxItemValidity();
        updateTeamCompRating();        

        // -- Finalize Scene -- //

        rightPane.getChildren().addAll(clear, manageSheets, manageRoster, scoresLabel, totalScore, firstScore, secondScore);

        threePaneMenu.getChildren().addAll(leftPane, centerPane, rightPane);

        mainMenu.setAlignment(Pos.TOP_CENTER);
        mainMenu.getChildren().addAll(topLabel, threePaneMenu);

        Scene scene = new Scene(mainMenu, WINDOW_SIZE_X, WINDOW_SIZE_Y);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();

        // -- Action Handlers -- //

        clear.setOnAction(e -> {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure you want to clear all values in the current Team Comp?\nThis action cannot be undone.", ButtonType.YES, ButtonType.NO);
            ButtonType result = confirm.showAndWait().orElse(ButtonType.NO);

            if (result == ButtonType.YES) {
                clearChoiceBoxes();
            }
        });

        manageSheets.setOnAction(e -> {
            SheetsMenuStage sheetsMenuStage = new SheetsMenuStage();
            sheetsMenuStage.showAndWait();

            Roster.updatePlayerAverages();
        });

        manageRoster.setOnAction(e -> {
            RosterStage rosterStage = new RosterStage();
            rosterStage.showAndWait();

            refreshChoiceBoxItems();
            Roster.updatePlayerAverages();
        });
    }

    private void setupTeamCompPanes(VBox firstHalfVBox, VBox secondHalfVBox) {
        for (int half = 1; half < 3; half++) {
            VBox targetVBox = half == 1 ? firstHalfVBox : secondHalfVBox;
            targetVBox.setSpacing(TEAMCOMPPANE_VBOX_GAP);

            Label halfLabel = new Label("Half " + half);
            targetVBox.getChildren().add(halfLabel);

            for (int section = 1; section < 5; section++) {
                VBox sectionVBox = new VBox();

                Label sectionLabel = new Label("Section " + section);
                sectionVBox.getChildren().add(sectionLabel);

                for (int i = 0; i < 4; i++) {
                    ChoiceBox<String> choiceBox = new ChoiceBox<>();
                    choiceBox.setMinWidth(CHOICEBOX_WIDTH);

                    choiceBox.setOnAction(e -> onChoiceSelected(choiceBox));

                    allChoiceBoxes.add(choiceBox);
                    sectionVBox.getChildren().add(choiceBox);
                }

                targetVBox.getChildren().add(sectionVBox);
            }
        }

        TeamComp teamComp = TeamComp.loadFromFile();
        TeamComp.setChoiceBoxesWithTeamComp(allChoiceBoxes, teamComp);
    }

    private void refreshChoiceBoxItems() {
        ignoreChoiceBoxEvents = true;
        ArrayList<Player> roster = Roster.roster.players;
        String[] playerNames = new String[roster.size()];

        for (int i = 0; i < roster.size(); i++)
            playerNames[i] = roster.get(i).name;

        for (ChoiceBox<String> choiceBox : allChoiceBoxes) {
            String prevValue = choiceBox.getValue();

            choiceBox.getItems().clear();
            choiceBox.getItems().add("Empty Slot");
            choiceBox.getItems().addAll(playerNames);

            try {
                choiceBox.setValue(prevValue);
            } catch(Exception e) {
                choiceBox.setValue("Empty Slot");
            }
        }
        
        // -- Check Validity For Each Half -- //
        final double lastFirstHalfIndex = 15;

        ArrayList<String> firstHalfItems = new ArrayList<>();
        ArrayList<String> secondHalfItems = new ArrayList<>();

        for (int i = 0; i < allChoiceBoxes.size(); i++) {
            ChoiceBox<String> box = allChoiceBoxes.get(i);
            String value = box.getValue();

            ArrayList<String> targetList = i <= lastFirstHalfIndex ? firstHalfItems : secondHalfItems;
        
            if (value == "Empty Slot")
                continue;

            if (targetList.contains(value))
                continue;

            targetList.add(box.getValue());
        }

        for (int i = 0; i < allChoiceBoxes.size(); i++) {
            ChoiceBox<String> box = allChoiceBoxes.get(i);

            // these are flipped because now we are excluding everything in the opposite half's list
            ArrayList<String> targetList = i <= lastFirstHalfIndex ? secondHalfItems : firstHalfItems;

            for (String value : targetList)
                box.getItems().remove(value);
        }

        // -- Check Validity Within Each Section -- //

        // lookup to access other players within each section using relative indexes
        final int[][] otherPlayersMatrix = {
            { 1, 2, 3 },
            { -1, 1, 2 },
            { -2, -1, 1 },
            { -3, -2, -1 }
        };
        int i = 0;
        for (int section = 0; section < 8; section++) {
            for (int player = 0; player < 4; player++) {
                ChoiceBox<String> box = allChoiceBoxes.get(i);
                String name = box.getValue();

                if (name == "Empty Slot") {
                    i++;
                    continue;
                }

                for (int other = 0; other < 3; other++) {
                    int shift = otherPlayersMatrix[player][other];
                    ChoiceBox<String> otherBox = allChoiceBoxes.get(i + shift);
                    otherBox.getItems().remove(name);
                }
                i++;
            }
        }

        ignoreChoiceBoxEvents = false;
    }

    private void updateTeamCompRating() {
        TeamComp teamComp = TeamComp.getTeamCompFromChoiceBoxes(allChoiceBoxes);
        
        Rating rating = teamComp.getRating();

        totalScore.setText(TOTALSCORE_LABEL + rating.total);
        firstScore.setText(FIRSTSCORE_LABEL + rating.firstHalf);
        secondScore.setText(SECONDSCORE_LABEL + rating.secondHalf);
    }

    private void clearChoiceBoxes() {
        ignoreChoiceBoxEvents = true;
        for (ChoiceBox<String> choiceBox : allChoiceBoxes) {
            choiceBox.setValue("Empty Slot");
        }
        refreshChoiceBoxItems();
        updateTeamCompRating();
        ignoreChoiceBoxEvents = false;
    }

    private void onChoiceSelected(ChoiceBox<String> choiceBox) {
        if (!ignoreChoiceBoxEvents) {
            refreshChoiceBoxItems();
            //checkChoiceBoxItemValidity();
            updateTeamCompRating();
        }
    }
}
