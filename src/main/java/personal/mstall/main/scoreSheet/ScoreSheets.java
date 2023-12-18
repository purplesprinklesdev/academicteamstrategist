package personal.mstall.main.scoreSheet;
import java.util.ArrayList;
import java.util.Collections;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import personal.mstall.main.util.FileType;
import personal.mstall.main.util.SaveManager;

@XmlRootElement
public class ScoreSheets {
    // Singleton
    @XmlTransient
    public static ScoreSheets scoreSheets;

    @XmlElement
    private ArrayList<Sheet> sheets;

    public static ArrayList<Sheet> getSheets() {
        return scoreSheets.sheets;
    }
    public static void setSheets(ArrayList<Sheet> newSheets) {
        Collections.sort(newSheets, new SortSheetByName());
        scoreSheets.sheets = newSheets;
    }

    public ScoreSheets(ArrayList<Sheet> sheets) {
        if (scoreSheets != null)
            return;
        
        this.sheets = sheets;
        scoreSheets = this;
    }
    public ScoreSheets(ScoreSheets that) {
        if (scoreSheets != null)
            return;
        
        scoreSheets = that;
    }
    public ScoreSheets() {
        if (scoreSheets != null)
            return;
        
        this.sheets = new ArrayList<>();
        scoreSheets = this;
    }
    public static ScoreSheets loadFromFile() {
        if (scoreSheets != null)
            return scoreSheets;
        
        Object sheetsObject = SaveManager.Load(FileType.SCORESHEETS);

        if (sheetsObject == null)
            new ScoreSheets();
        else
            new ScoreSheets((ScoreSheets) sheetsObject);

        return scoreSheets;
    }

    public Sheet find(String name) {
        for (Sheet sheet : sheets) {
            if (sheet.name == name) { 
                return sheet;
            }
        }
        return null;
    }
    
    public int findIndex(String name) { 
        for (int i = 0; i < sheets.size(); i++) {
            if (sheets.get(i).name == name) { 
                return i;
            }
        }
        return -1;
    }
}