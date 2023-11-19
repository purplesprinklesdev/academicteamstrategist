# Untitled Academic Team Strategist

A Java GUI application which assists in the creation of strategies for Academic Team matches. Based on the scores of past matches, the application predicts how well a team setup will perform. This specifically follows the rules of the [SBAAC Academic Conference](http://sbaac.com/confStandings.aspx?sat=20).
Created by Matthew Stall for the Williamsburg Academic Team.

## Compatibility Information

<p>Untitled Academic Team Strategist is a desktop only application and currently has no plans for a mobile release.</p>

<p>Untitled Academic Team Strategist uses JavaFX to render GUI elements. If you encounter visual bugs while using this application, they may be due to compatibility issues between your OS and JavaFX. If possible, try testing the application on a different OS.</p>

#### OS Specific Testing:
Everything *should* still work on platforms that are untested, but don't be surprised if they do have problems. 

- Windows:  **WORKING**
- MacOS:    **UNTESTED**
- GNU/Linux:
    - pop_OS!:          **WORKING**
    - Other distros:    **UNTESTED**

## Getting Started
[Download the latest release](https://github.com/purplesprinklesdev/academicteamstrategist/releases/tag/Release)

<p>Start by running the application and clicking on the "Manage Roster" button. Here you should add all of your team's players.</p>

<p>Next, close the roster menu and click on "Manage Scoresheets". Click the "+" to add a new scoresheet. Scoresheets are how you will input your players' previous performances.</p>

---

### Score Sheet Syntax

<p>Score sheets measure the performance of each player on a team during a game. Each row represents a player, and each column represents a section of the game. Section 1 is American Lit through Life Science. Section 2 is World Lit through American History. Alphabet and Lightning are self expanatory. Scores here are measured by questions answered, not points earned.</p>
<p>Values in section 1 and 2 can be from 0 to 15, while values in Alphabet and Lightning can range from 0 to 10. These reflect the number of questions asked in each category. Lastly, if a player did not take part in a certain section, you can put a hyphen ("-") so that their average for this section is not affected by this score sheet.</p>

---

<p>Back on the main menu, create a team setup by clicking in the dropdowns. Predicted scores will show automatically. (this is represented in number of questions answered) By clicking "Manage Team Setups", you can save this team setup to be reloaded in the future through the same menu.</p>
