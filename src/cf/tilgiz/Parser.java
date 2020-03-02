package cf.tilgiz;

import java.io.*;

public class Parser {

    private String inputString;
    private String clientIpAddress;
    private String[] parsedString;
    private long now;

    public Parser(String inputString, String clientIpAddress) {
        this.inputString = inputString;
        this.clientIpAddress = clientIpAddress;
        this.now = System.currentTimeMillis() / 1000;
    }

    public void parseString() {
        String clearStr = inputString.substring(inputString.indexOf("[") + 1, inputString.indexOf("]"));
        parsedString = clearStr.split("[*,]");
    }

    public String buildQuery() {
        String query;
        StringBuilder sqlVariable = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        for (int i = 0; i < parsedString.length; i++) {
            if (i == (parsedString.length - 1)) {
                sqlVariable.append("m").append(i);
                sqlValues.append("'").append(parsedString[i]).append("'");
            } else {
                sqlVariable.append("m").append(i).append(",");
                sqlValues.append("'").append(parsedString[i]).append("',");
            }
        }

        if (parsedString[3].contains("LK")) {
            query = "UPDATE online set ip='" + clientIpAddress + "',date=" + now + ",m0='" + parsedString[0] + "',m1='" + parsedString[1] + "',m2='" + parsedString[2] + "',m3='" + parsedString[3] + "',m4='" + parsedString[4] + "',m5='" + parsedString[5] + "',m6='" + parsedString[6] + "' where id=1";
            writeFile(Server.ONLINE_STATUS_FILE, now + "," + parsedString[1] + "," + parsedString[6]);
        } else {
            query = "INSERT INTO history (ip,date," + sqlVariable + ") VALUES('" + clientIpAddress + "'," + now + "," + sqlValues + ")";
            if (parsedString[3].contains("UD") || parsedString[3].contains("UD2"))
                writeFile(Server.ONLINE_TRACK_FILE, now + "," + parsedString[1] + "," + parsedString[7] + "," + parsedString[9] + "," + parsedString[11]);
        }
        return query;
    }

    public boolean checkDbSkip() {
        if (new File(Server.ONLINE_TRACK_FILE).isFile() && !parsedString[3].contains("LK")) {
            String stringFromStatusFile = readFile(Server.ONLINE_TRACK_FILE);
            long time = Long.parseLong(stringFromStatusFile.split(",")[0]);
            String lon = stringFromStatusFile.split(",")[2].substring(0, 6);
            String lat = stringFromStatusFile.split(",")[3].substring(0, 6);
            //System.out.println(now + " - "+ time +" = "+ (now - time));
            return (now - time) < Server.SKIP_DB_TIMEOUT && lon.equals(parsedString[7].substring(0, 6)) && lat.equals(parsedString[9].substring(0, 6));
        }
        return false;
    }

    public void writeFile(String filename, String string) {
        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(string);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String filename) {
        String output = null;
        try {
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
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("File " + filename + " not found!");
        }
        return output;
    }
}
