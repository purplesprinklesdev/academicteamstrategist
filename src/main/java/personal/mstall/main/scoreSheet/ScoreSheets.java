package personal.mstall.main.scoreSheet;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ScoreSheets {
    // Singleton
    @XmlTransient
    public static ScoreSheets scoreSheets;

    public ArrayList<Sheet> sheets;

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
}
