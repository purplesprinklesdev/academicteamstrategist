# sqweezer

A Java GUI application to calculate the statistically most optimal team setup for Academic Quiz Bowl. This specifically follows the rules of the [SBAAC Academic Conference](http://sbaac.com/confStandings.aspx?sat=20).
Created by Matthew Stall for the Williamsburg Academic Team.

## Usage

After opening the application, click "Manage Roster" to create a team roster. Then click "Manage ScoreSheets" to add score sheets from previous games. After updating your score sheets, returning to the main menu will recalculate your most optimal team.

### Score Sheet Structure

Score sheets measure the performance of each player on a team during a game. Each row represents a player, and each column represents a question category. Scores here are measured by *questions answered*, **not** points earned. Values in columns 1-10 (American Lit through American History) can vary from 0 to 3, while values in columns 11 and 12 (Alphabet and Lightning rounds) can range from 0-10. These reflect the number of questions asked in each category.
