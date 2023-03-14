package personal.mstall.main;

import javafx.application.Application;
import javafx.stage.Stage;
import personal.mstall.main.scoreSheet.ScoreSheets;
import personal.mstall.main.stages.RosterStage;
import personal.mstall.main.stages.SheetsMenuStage;
import personal.mstall.main.teamLogic.Roster;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;


public class StrategistApp extends Application {
    private static final String MAINMENU_LABEL = "Academic Team Strategist";
    private static final double MAINMENU_TABLE_HEIGHT = 500;
    private static final double MAINMENU_TABLE_WIDTH = 400;
    private static final double MAINMENU_VBOX_GAP = 8;

    private static final int MAINMENU_TABLE_COL = 3;
    private static final String[] MAINMENU_TABLE_TITLES = { "Half", "Section", "Team" };
    private static final String[] MAINMENU_TABLE_PROPERTIES = { "half", "section", "players" };
    private static final double MAINMENU_TABLE_TEAMCELL_WIDTH = 150;
    private static final double MAINMENU_TABLE_CELL_WIDTH = 45;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFromFiles();

        primaryStage.setTitle("Strategist");

        VBox layout = new VBox();
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(MAINMENU_VBOX_GAP);

        Label topLabel = new Label(MAINMENU_LABEL);

        TableView<Team> table = initializeTable();

        Label bottomLabel = new Label("Generated on: " + "");

        Button manageSheets = new Button();
        manageSheets.setText("Manage Scoresheets");

        Button manageRoster = new Button();
        manageRoster.setText("Manage Roster");

        layout.setAlignment(Pos.TOP_CENTER);
        layout.getChildren().addAll(topLabel, table, bottomLabel, manageSheets, manageRoster);
        Scene scene = new Scene(layout, MAINMENU_TABLE_WIDTH + 40, MAINMENU_TABLE_HEIGHT + 30);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // -- Action Handlers -- //

        manageSheets.setOnAction(e -> {
            SheetsMenuStage sheetsMenuStage = new SheetsMenuStage(new Sheet());
            sheetsMenuStage.showAndWait();
            // TODO: Refresh
        });

        manageRoster.setOnAction(e -> {
            RosterStage rosterStage = new RosterStage();
            rosterStage.showAndWait();
            // TODO: Refresh
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private TableView<Team> initializeTable() {
        TableView<Team> table = new TableView<>();

        for (int i = 0; i < MAINMENU_TABLE_COL; i++) {
            final int colNo = i;

            TableColumn<Team, String> tc = new TableColumn<>(MAINMENU_TABLE_TITLES[colNo]);
            tc.setCellValueFactory(new PropertyValueFactory<>(MAINMENU_TABLE_PROPERTIES[colNo]));
            
            if (colNo == 0)
                tc.setPrefWidth(MAINMENU_TABLE_TEAMCELL_WIDTH);
            else
                tc.setPrefWidth(MAINMENU_TABLE_CELL_WIDTH);

            table.getColumns().add(tc);
        }
        table.maxWidth(MAINMENU_TABLE_WIDTH);
        table.prefHeight(MAINMENU_TABLE_HEIGHT);
        table.setEditable(false);
        
        // TODO: Refresh
        return table;
    }

    private void loadFromFiles() {
        Object rosterObject = SaveManager.Load(FileType.ROSTER);
        
        if (rosterObject == null)
            new Roster();
        else
            new Roster((Roster) rosterObject);

        Object sheetsObject = SaveManager.Load(FileType.SCORESHEETS);

        if (sheetsObject == null)
            new ScoreSheets();
        else
            new ScoreSheets((ScoreSheets) sheetsObject);
    }
}
