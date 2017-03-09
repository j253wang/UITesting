package nick.com.uitesting;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Jianing Wang on 2/26/2017.
 */

public class ApplicationMain {
    public static final String Key_Locations = "KeyLocations";
    public static final String StorageFile = "StorageFile.txt";

    class ListItem {
        String textdata;
        String caption;
        Integer hour;
        Integer minute;

        ListItem(String text, String capshun){
            caption = capshun;
            textdata = text;
        }

        String getCaption()
        {
            return caption;
        }

        String getTextdata()
        {
            return textdata;
        }

        Integer getHour()
        {
            return hour;
        }

        Integer getMinute()
        {
            return minute;
        }
    }

    public static void ClearFile(String file) throws IOException {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {}
    }
}
