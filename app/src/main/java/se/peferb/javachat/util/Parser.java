package se.peferb.javachat.util;

public class Parser {

    /** @return empty string if no name found (that's when you send bad formatted data) */
    public String parseName(String data) {
        try {
            data = data.substring(data.indexOf("name=")+5);
            if (data.contains(", photoUrl=")) {
                return data.substring(0, data.indexOf(", photoUrl="));
            }
            return data.substring(0, data.indexOf('}'));
        } catch (Exception e) {
            System.err.println("Data string had bad format, string was: " + data);
            e.printStackTrace();
            return "";
        }
    }

    /** @return empty string if no id found (that's when you send bad formatted data) */
    public String parseId(String data) {
        try {
            return data.substring(data.indexOf("value = {")+9, data.indexOf("={"));
        } catch (Exception e) {
            System.err.println("Data string had bad format, string was: " + data);
            e.printStackTrace();
            return "";
        }
    }

    /** @return "Just sent a image" if no text message found
     * (because in image messages there is no text) */
    public String parseMessage(String data) {
        if (data.contains("text=")) {
            return data.substring(data.indexOf("text=")+5, data.lastIndexOf(", name="));
        }
        return "Just sent a image";
    }
}
