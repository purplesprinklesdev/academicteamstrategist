package personal.mstall.main.teamLogic;

import jakarta.xml.bind.annotation.*;

public class Player {
    @XmlAttribute
    public String name;
    @XmlTransient
    private double[] sectionAverages;

    public double[] getSectionAverages() {
        return sectionAverages;
    }
    public void setSectionAverages(double[] sectionAverages) {
        this.sectionAverages = sectionAverages;
    }

    public Player() {
        this.name = "Empty Slot";
        double[] array = { 0, 0, 0, 0 };
        sectionAverages = array;
    }

    public Player(String name) {
        this.name = name;
    }
}
