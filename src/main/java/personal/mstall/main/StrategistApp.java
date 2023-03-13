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

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFromFiles();

        SheetsMenuStage stage = new SheetsMenuStage();
        //RosterStage stage = new RosterStage();
        stage.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
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
