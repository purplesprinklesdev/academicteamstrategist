package personal.mstall.main;

import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;

public class CloseWarning {
    public Dialog<String> dialog;
    private static final String CLOSE_WARNING_TITLE = "Save Changes?";
    private static final String CLOSE_WARNING_CONTENT = "Files have unsaved changes. Do you want to save and exit?";
    private static final String CLOSE_WARNING_YES = "Save";
    private static final String CLOSE_WARNING_NO = "Discard Changes";

    public static enum State {
        SAVE,
        DISCARD,
        CANCEL
    }

    public State show() {
        String result = dialog.showAndWait().orElse("CANCEL");

        // IDK why, but the dialog.setResultConverter can only output Strings
        // unfortunately, I love enums so here's the switch statement to translate:
        switch (result) {
            case "YES":
                return State.SAVE;
            case "NO":
                return State.DISCARD;
            default:
                return State.CANCEL;
        }
    }

    public CloseWarning() {
        dialog = new Dialog<String>();

        dialog.setTitle(CLOSE_WARNING_TITLE);
        dialog.setContentText(CLOSE_WARNING_CONTENT);

        //  Default button order is: No - Yes - Cancel
        //  This changes the button order to: Yes - No - Cancel
        ButtonBar buttonBar = (ButtonBar) dialog.getDialogPane().lookup(".button-bar");
        buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);


        ButtonType yes = new ButtonType(CLOSE_WARNING_YES, ButtonData.YES);
        // ButtonData.NO is also a default cancel button,
        // this causes issues when there is another cancel button present
        ButtonType no = new ButtonType(CLOSE_WARNING_NO, ButtonData.OTHER);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(yes, no, cancel);

        dialog.setResultConverter(buttonType -> {
            if (buttonType.getButtonData() == ButtonData.YES)
                return "YES";
            else if (buttonType.getButtonData() == ButtonData.OTHER)
                return "NO";
            return "CANCEL";
        });
    }
}