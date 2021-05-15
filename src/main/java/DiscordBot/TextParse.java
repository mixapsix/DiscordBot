package DiscordBot;

public class TextParse {

    //Метод пытается получить число из строки, если не получается то возвращается -1
    public static int tryParseInt(String numberString) {
        int number;
        try {
            String count = numberString;
            if (numberString.indexOf(",") != -1)
                count = numberString.replace(",", "");
            if (numberString.indexOf(" ") != -1)
                count = numberString.replace(" ", "");
            number = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            number = -1;
        }
        return number;
    }
}

