package cf.tilgiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestParser {
    public static void main(String[] args) throws IOException {
//        String inputString = "[3G*1208014868*0009*LK,0,0,99][3G*1208014868*0124*UD,110320,062250,V,54.900475,N,52.2779483,E,0.00,0.0,0.0,0,100,99,0,0,00000000,1,0,250,1,16205,29103,147,5,Corp_INET,b4:fb:e4:97:d0:45,-73,DIRECT-pHPhaser 3260,9e:93:4e:3a:25:ac,-76,Tetra,b4:fb:e4:97:d1:20,-78,DIRECT-E7-HP M428fdw LJ,fa:b4:6a:e0:d0:e7,-78,WIFI-LINK,90:f6:52:52:8f:7a,-82,18.8]";
        String inputString = "[3G*1208014868*0009*LK,0,0,97][3G*1208014868*004F*CONFIG,TY:g72,UL:60,SY:0,CM:0,WT:0,HR:0,TB:2,CS:0,PP:1,AB:0,HH:1,TR:0,MO:1,FL:1][3G*1208014868*019A*UD2,110320,071837,A,54.900505,N,52.2780400,E,0.00,189.3,0.0,8,100,95,0,0,00000000,7,255,250,1,16205,13561,151,16205,29103,149,16205,13563,142,16205,13562,141,16205,29102,141,16205,23561,139,16205,10453,136,5,Tetra,b4:fb:e4:97:d1:20,-74,Corp_INET,b4:fb:e4:97:d0:45,-75,DIRECT-pHPhaser 3260,9e:93:4e:3a:25:ac,-75,DIRECT-E7-HP M428fdw LJ,fa:b4:6a:e0:d0:e7,-79,DIRECT-78-HP M426 LaserJet,92:32:4b:83:2b:78,-82,20.1][3G*1208014868*00D3*UD2,110320,071854,A,54.900538,N,52.2780167,E,0.00,28.2,0.0,8,100,95,0,0,00000000,7,255,250,1,16205,13561,154,16205,29103,146,16205,13563,142,16205,29102,140,16205,13562,139,16205,11163,138,16205,10453,134,0,19.7]";
//        String inputString = "[3G*1208014868*0009*LK,0,0,97]";
//        String inputString = "[3G*1208014868*004F*CONFIG,TY:g72,UL:60,SY:0,CM:0,WT:0,HR:0,TB:2,CS:0,PP:1,AB:0,HH:1,TR:0,MO:1,FL:1]";
//        String inputString = "[3G*1208014868*019A*UD2,110320,071837,A,54.900505,N,52.2780400,E,0.00,189.3,0.0,8,100,95,0,0,00000000,7,255,250,1,16205,13561,151,16205,29103,149,16205,13563,142,16205,13562,141,16205,29102,141,16205,23561,139,16205,10453,136,5,Tetra,b4:fb:e4:97:d1:20,-74,Corp_INET,b4:fb:e4:97:d0:45,-75,DIRECT-pHPhaser 3260,9e:93:4e:3a:25:ac,-75,DIRECT-E7-HP M428fdw LJ,fa:b4:6a:e0:d0:e7,-79,DIRECT-78-HP M426 LaserJet,92:32:4b:83:2b:78,-82,20.1]";
//        String inputString = "[3G*1208014868*00D3*UD2,110320,071854,A,54.900538,N,52.2780167,E,0.00,28.2,0.0,8,100,95,0,0,00000000,7,255,250,1,16205,13561,154,16205,29103,146,16205,13563,142,16205,29102,140,16205,13562,139,16205,11163,138,16205,10453,134,0,19.7]";
//        String inputString = "[3G*1208014868*0009*LK,0,0,97]";

        ParserNew parser = new ParserNew(inputString, "127.0.0.1");
        parser.parseString();
        List<Query> queryList = parser.buildQueryList();

        for (Query query: queryList) {
            System.out.println(query.getQueryString());
            System.out.println(query.isSkip());
        }

    }
}
