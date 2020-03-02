package cf.tilgiz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Parse {
    public static void main(String[] args) {
//        String inputStr = "[3G*1208014868*00E3*UD2,290318,170736,V,54.891907,N,52.3444133,E,0.00,133.6,0.0,7,100,100,0,0,00000008,6,255,250,2,1634,46522,149,1634,47882,138,1634,46532,137,1634,49333,137,1634,47941,137,1634,47884,136,1,Keenetic-2154,28:28:5d:b9:7d:f0,-79,37.3]";
        String inputStr = "[3G*1208014868*000A*LK,0,0,100]";

        String clearStr = inputStr.substring(inputStr.indexOf("[") + 1, inputStr.indexOf("]"));
        String[] splitedClearStr = clearStr.split("\\*|,");
        StringBuilder sqlVariable = new StringBuilder();
        StringBuilder sqlValues = new StringBuilder();
        for (int i = 0; i < splitedClearStr.length; i++) {
            if (i == (splitedClearStr.length - 1)) {
                sqlVariable.append("m").append(i);
                sqlValues.append("'").append(splitedClearStr[i]).append("'");
            } else {
                sqlVariable.append("m").append(i).append(",");
                sqlValues.append("'").append(splitedClearStr[i]).append("',");
            }
        }

        String client_ip = "8.8.8.8";
        long now = System.currentTimeMillis() / 1000;

        System.out.println(sqlVariable);
        System.out.println(sqlValues);

        String query = null;
        if (splitedClearStr[3].contains("LK")) {
            query = "UPDATE online set ip='" + client_ip + "',date=" + now + ",m0='" + splitedClearStr[0] + "',m1='" + splitedClearStr[1] + "',m2='" + splitedClearStr[2] + "',m3='" + splitedClearStr[3] + "',m4='" + splitedClearStr[4] + "',m5='" + splitedClearStr[5] + "',m6='" + splitedClearStr[6] + "' where id=1";
            writeFile("E:\\temp\\online.log",now + "," + splitedClearStr[1] + "," + splitedClearStr[6]);
        } else {
            query = "INSERT INTO history (ip,date," + sqlVariable + ") VALUES('" + client_ip + "'," + now + "," + sqlValues + ")";
            writeFile("E:\\temp\\online_track.log",now + "," + splitedClearStr[1] + "," + splitedClearStr[7] + "," + splitedClearStr[9] + "," + splitedClearStr[11]);
        }

        System.out.println(query);


    }

    public static void writeFile(String filename, String string) {
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
}
