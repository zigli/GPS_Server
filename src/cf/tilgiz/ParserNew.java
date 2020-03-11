package cf.tilgiz;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserNew {
    private String inputString;
    private String clientIpAddress;

    private List<String> stringList = new ArrayList<>();
    private List<Query> queryList = new ArrayList<>();
    private long now;

    public ParserNew(String inputString, String clientIpAddress) {
        this.inputString = inputString;
        this.clientIpAddress = clientIpAddress;
        this.now = System.currentTimeMillis() / 1000;
    }

    public void parseString() {
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            stringList.add(matcher.group(1));
        }
    }
    public List<Query> buildQueryList() throws IOException {
        for (String stringItem :stringList) {
            queryList.add(createQuery(stringItem.split("[*,]")));
        }
        return queryList;
    }

    private Query createQuery(String[] parsedStringArray) throws IOException{
        Query query;
        StringBuilder sqlVariable = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        for (int i = 0; i < parsedStringArray.length; i++) {
            if (i == (parsedStringArray.length - 1)) {
                sqlVariable.append("m").append(i);
                sqlValues.append("'").append(parsedStringArray[i], 0, Math.min(parsedStringArray[i].length(), 20)).append("'");
            } else {
                sqlVariable.append("m").append(i).append(",");
                sqlValues.append("'").append(parsedStringArray[i], 0, Math.min(parsedStringArray[i].length(), 20)).append("',");
            }
        }

        if (parsedStringArray[3].contains("LK")) {
            query = new Query("UPDATE online set ip='" + clientIpAddress + "',date=" + now + ",m0='" + parsedStringArray[0] + "',m1='" + parsedStringArray[1] + "',m2='" + parsedStringArray[2] + "',m3='" + parsedStringArray[3] + "',m4='" + parsedStringArray[4] + "',m5='" + parsedStringArray[5] + "',m6='" + parsedStringArray[6] + "' where id=1");
            writeFile(Server.ONLINE_STATUS_FILE, now + "," + parsedStringArray[1] + "," + parsedStringArray[6]);
        } else {
            query = new Query("INSERT INTO history (ip,date," + sqlVariable + ") VALUES('" + clientIpAddress + "'," + now + "," + sqlValues + ")",checkDbSkip(parsedStringArray));
            if (parsedStringArray[3].contains("UD") || parsedStringArray[3].contains("UD2"))
                writeFile(Server.ONLINE_TRACK_FILE, now + "," + parsedStringArray[1] + "," + parsedStringArray[7] + "," + parsedStringArray[9] + "," + parsedStringArray[11]);
        }
        return query;
    }

    public boolean checkDbSkip(String[] parsedStringArray) throws IOException {
        if (new File(Server.ONLINE_TRACK_FILE).isFile() && (parsedStringArray[3].contains("UD") || parsedStringArray[3].contains("UD2"))) {
            String stringFromStatusFile = readFile(Server.ONLINE_TRACK_FILE);
            long time = Long.parseLong(stringFromStatusFile.split(",")[0]);
            String lon = stringFromStatusFile.split(",")[2].substring(0, 6);
            String lat = stringFromStatusFile.split(",")[3].substring(0, 6);
            return (now - time) < Server.SKIP_DB_TIMEOUT && lon.equals(parsedStringArray[7].substring(0, 6)) && lat.equals(parsedStringArray[9].substring(0, 6));
        }
        return false;
    }


    public void writeFile(String filename, String string) throws IOException {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(string);
        fileWriter.flush();
        fileWriter.close();
    }

    public String readFile(String filename) throws IOException {
        String output;
        File file = new File(filename);
        FileReader fileReader = new FileReader(file);
        StringBuilder stringBuilder = new StringBuilder();
        int numCharsRead;
        char[] charArray = new char[1024];
        while ((numCharsRead = fileReader.read(charArray)) > 0) {
            stringBuilder.append(charArray, 0, numCharsRead);
        }
        fileReader.close();
        output = stringBuilder.toString();
        return output;
    }
}
