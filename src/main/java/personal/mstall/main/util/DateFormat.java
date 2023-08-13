package personal.mstall.main.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    public enum Type {
        DAY("dd/MM/Y"),
        TIME("HH:mm"),
        DAYANDTIME("dd/MM/Y HH:mm");

        public final String format;
        private Type(String format) {
            this.format = format;
        }
    }
    
    public static String getCurrentDate(DateFormat.Type type) {
        Date date = new Date();
        return formatDate(date, type);
    }

    public static String formatDate(Date date, DateFormat.Type type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(type.format);
        return dateFormat.format(date);
    }
}
