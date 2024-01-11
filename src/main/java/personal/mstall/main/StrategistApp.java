package personal.mstall.main;

import java.io.File;

import java.util.ArrayList;

import javax.swing.JFileChooser;

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
import personal.mstall.main.util.OSUtils;
import personal.mstall.main.util.SaveManager;
import personal.mstall.main.stages.*;


public class StrategistApp extends Application {
    private static final String WINDOW_TITLE = "Strategist";
    private static final double WINDOW_SIZE_X = 880;
    private static final double WINDOW_SIZE_Y = 650;

    private static final String MAINMENU_LABEL = "Academic Team Strategist";
    private static final double MAINMENU_VBOX_GAP = 8;
    private static final double THREEPANE_HBOX_GAP = 12;

    private static final double TEAMCOMPPANE_VBOX_GAP = 8;
    private static final double RIGHTPANE_VBOX_GAP = 12;

    private static final double CHOICEBOX_WIDTH = 200;
    private static final double BUTTONS_WIDTH = 250;

    private static final String[] SECTION_LABELS = { "Section 1", "Section 2", "Alphabet", "Lightning" };

    private static final String SCORES_LABEL = "Expected Score (# of correct answers)";
    private static final String TOTALSCORE_LABEL = "Total: ";
    private static final String FIRSTSCORE_LABEL = "First Half: ";
    private static final String SECONDSCORE_LABEL = "Second Half: ";

    public static ArrayList<ChoiceBox<String>> allChoiceBoxes = new ArrayList<>();
    public static ArrayList<Label> allChoiceBoxLabels = new ArrayList<>();
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
        TeamCompSaves.loadFromFile();

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

        scoresLabel = new Label(SCORES_LABEL);
        totalScore = new Label(TOTALSCORE_LABEL);
        firstScore = new Label(FIRSTSCORE_LABEL);
        secondScore = new Label(SECONDSCORE_LABEL);

        Roster.updatePlayerAverages();  
        setupTeamCompPanes(leftPane, centerPane);

        rightPane.setSpacing(RIGHTPANE_VBOX_GAP);

        Button manageTeamSetups = new Button("Manage Team Setups");
        manageTeamSetups.setMinWidth(BUTTONS_WIDTH);

        Button clear = new Button("Clear Team Setup");
        clear.setMinWidth(BUTTONS_WIDTH);

        Button manageSheets = new Button("Manage Scoresheets");
        manageSheets.setMinWidth(BUTTONS_WIDTH);

        Button manageRoster = new Button("Manage Roster");
        manageRoster.setMinWidth(BUTTONS_WIDTH);

        Button importData = new Button("Import Strategist Data");
        importData.setMinWidth(BUTTONS_WIDTH);
        
        Button exportData = new Button("Export Strategist Data");
        exportData.setMinWidth(BUTTONS_WIDTH);

        // -- Finalize Scene -- //

        rightPane.getChildren().addAll(manageTeamSetups, clear, manageSheets, manageRoster, importData, exportData, scoresLabel, totalScore, firstScore, secondScore);

        threePaneMenu.getChildren().addAll(leftPane, centerPane, rightPane);

        mainMenu.setAlignment(Pos.TOP_CENTER);
        mainMenu.getChildren().addAll(topLabel, threePaneMenu);

        Scene scene = new Scene(mainMenu, WINDOW_SIZE_X, WINDOW_SIZE_Y);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();

        // -- Action Handlers -- //

        manageTeamSetups.setOnAction(e -> {
            
            CompsMenuStage compsMenuStage = new CompsMenuStage();
            compsMenuStage.showAndWait();
            refreshChoiceBoxItems();
            updateTeamCompRating();
        });

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

        importData.setOnAction(e -> {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure you would like to import Strategist data? This will erase all existing data on this instance and cannot be undone. It is recommended to make a backup before doing this.", ButtonType.YES, ButtonType.NO);
            ButtonType result = confirm.showAndWait().orElse(ButtonType.NO);

            if(result == ButtonType.NO) {
                System.out.println("IMPORT FAILED: ABORTED BY USER");
                return;
            }

            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(OSUtils.documentsDir()));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = fc.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File sourceDir = fc.getSelectedFile();
                if (SaveManager.importDataFrom(sourceDir)) {
                    Alert error = new Alert(AlertType.INFORMATION, "Import operation completed sucessfully.", ButtonType.OK);
                    error.showAndWait();

                    clearChoiceBoxes();
                    refreshChoiceBoxItems();
                    Roster.updatePlayerAverages();
                } else {
                    Alert error = new Alert(AlertType.ERROR, "Import operation encountered an unexpected error. Make sure that the folder you chose is a valid save and the XML file names match.", ButtonType.OK);
                    error.showAndWait();
                }
            }
            else {
                Alert abort = new Alert(AlertType.ERROR, "Import operation aborted by user", ButtonType.OK);
                abort.showAndWait();
            }
        });

        exportData.setOnAction(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(OSUtils.documentsDir()));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int returnVal = fc.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File destDir = fc.getSelectedFile();
                if (SaveManager.exportDataTo(destDir)) {
                    Alert error = new Alert(AlertType.INFORMATION, "Export operation completed sucessfully.", ButtonType.OK);
                    error.showAndWait();
                } else {
                    Alert error = new Alert(AlertType.ERROR, "Export operation encountered an unexpected error. Make sure the source XML files are named appropriately.", ButtonType.OK);
                    error.showAndWait();
                }
            }
            else {
                Alert abort = new Alert(AlertType.ERROR, "Export operation aborted by user", ButtonType.OK);
                abort.showAndWait();
            }
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

                Label sectionLabel = new Label(SECTION_LABELS[section - 1]);
                sectionVBox.getChildren().add(sectionLabel);

                for (int i = 0; i < 4; i++) {
                    HBox choiceHBox = new HBox();
                    choiceHBox.setSpacing(8);

                    ChoiceBox<String> choiceBox = new ChoiceBox<>();
                    choiceBox.setMinWidth(CHOICEBOX_WIDTH);
                    choiceBox.setOnAction(e -> onChoiceSelected(choiceBox));

                    Label choiceBoxLabel = new Label("0");
                    choiceBoxLabel.setMinWidth(40);

                    allChoiceBoxes.add(choiceBox);
                    allChoiceBoxLabels.add(choiceBoxLabel);
                    choiceHBox.getChildren().addAll(choiceBox, choiceBoxLabel);
                    sectionVBox.getChildren().add(choiceHBox);
                }

                targetVBox.getChildren().add(sectionVBox);
            }
        }

        clearChoiceBoxes();
    }

    private void refreshChoiceBoxItems() {
        ignoreChoiceBoxEvents = true;
        ArrayList<Player> roster = Roster.roster.players;
        ArrayList<String> playerNameList = new ArrayList<>(); 

        for (int i = 0; i < roster.size(); i++)
            playerNameList.add(roster.get(i).name);

        // -- Check Validity For Each Half -- //
        final double lastFirstHalfIndex = 15;

        // lookup to access other players within each section using relative indexes
        final int[][] shiftMatrix = {
            { -3, -2, -1 },
            { 1, 2, 3 },
            { -1, 1, 2 },
            { -2, -1, 1 },
        };

        ArrayList<String> firstHalfList = new ArrayList<>(playerNameList);
        ArrayList<String> secondHalfList = new ArrayList<>(playerNameList);

        ArrayList<String> namesCovered = new ArrayList<>();

        for (int i = 0; i < allChoiceBoxes.size(); i++) {
            ChoiceBox<String> box = allChoiceBoxes.get(i);
            String name = box.getValue();

            if (name == "Empty Slot" || namesCovered.contains(name))
                continue;
            
            namesCovered.add(name);

            Boolean isFirstHalf = i < 16;
            if (isFirstHalf)
                secondHalfList.remove(name);
            else 
                firstHalfList.remove(name);
        }

        for (int i = 0; i < allChoiceBoxes.size(); i++) {
            ArrayList<String> thisBoxsItems = new ArrayList<>(i <= lastFirstHalfIndex ? firstHalfList : secondHalfList);

            ChoiceBox<String> box = allChoiceBoxes.get(i);
            String value = box.getValue();

            int thisBoxsPos = (i+1) % 4;
            for (int other = 0; other < 3; other++) {
                int shift = shiftMatrix[thisBoxsPos][other];
                ChoiceBox<String> otherBox = allChoiceBoxes.get(i + shift);

                thisBoxsItems.remove(otherBox.getValue());
            }
            
            box.getItems().clear();
            box.getItems().add("Empty Slot");
            box.getItems().addAll(thisBoxsItems);

            try {
                box.setValue(value);
            } catch(Exception e) {
                box.setValue("Empty Slot");
            }
        }
        
        ignoreChoiceBoxEvents = false;
    }

    private void updateTeamCompRating() {
        TeamComp teamComp = TeamComp.getTeamCompFromChoiceBoxes();
        
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
            updateTeamCompRating();
        }
    }
}
