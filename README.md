# Untitled Academic Team Strategist

A Java GUI application which assists in the creation of strategies for Academic Team matches. Based on the scores of past matches, the application predicts how well a team setup will perform. This specifically follows the rules of the [SBAAC Academic Conference](http://sbaac.com/confStandings.aspx?sat=20). <br>
Created by Matthew Stall for the Williamsburg Academic Team.

## Compatibility Information

Untitled Academic Team Strategist requires Java 19 or greater, [get it here from Oracle](https://www.oracle.com/java/technologies/downloads/) or from your package manager if on Linux. <br>

This app uses JavaFX to render GUI elements. If you encounter visual bugs while using it, they may be due to compatibility issues between your OS and JavaFX. If possible, try testing the application on a different OS. <br>

#### OS Specific Testing:
Everything *should* still work on platforms that are untested, but I can't say for certain.

- Windows 10/11:  **WORKING**
- MacOS:    **UNTESTED**
- GNU/Linux:
    - pop_OS!:          **WORKING**
    - Other distros:    **UNTESTED**

## Getting Started
[Download the latest release](https://github.com/purplesprinklesdev/academicteamstrategist/releases/)

Start by running the application and clicking on the `Manage Roster` button. Here you should add all of your team's players.<br><br>

Next, close the roster menu and click on `Manage Scoresheets`. Click the `+` to add a new scoresheet. Scoresheets are how you will input your players' previous performances.<br>

### Score Sheet Syntax

Score sheets measure the performance of each player on a team during a game. Each row represents a player, and each column represents a section of the game. Section 1 is American Lit through Life Science. Section 2 is World Lit through American History. Alphabet and Lightning are self expanatory. Scores here are measured by questions answered, not points earned. <br>

Values in section 1 and 2 can be from 0 to 15, while values in Alphabet and Lightning can range from 0 to 10. These reflect the number of questions asked in each category. Lastly, if a player did not take part in a certain section, you can put a hyphen `-` so that their average for this section is not affected by this score sheet. <br>

### Creating a Team Setup

Back on the main menu, create a team setup by clicking in the dropdowns. Predicted scores will show automatically. (this is represented in number of questions answered) By clicking "Manage Team Setups", you can save this team setup to be reloaded in the future. <br>

### Import & Export Data

Click on `Import Strategist Data` to start the **import** process. You will then be prompted to navigate to the data folder you want to import. It should be a folder called `strategist`. The contents must be unchanged from when it was exported, and the folder **cannot** be a `.zip`. If it is, you must unzip it first. <br>

Click on `Export Strategist Data` to start the **export** process. You will then be prompted to navigate to the folder you want the exported data to be placed in. This can be any folder on your PC, but make sure you can easily navigate to it after the export. <br>

