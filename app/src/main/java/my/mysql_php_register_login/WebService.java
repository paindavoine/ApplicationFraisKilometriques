
package my.mysql_php_register_login;


import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class WebService {



   //private final static String link = "http://192.168.1.16:80/webservice/";
   // private final static String link = "http://172.16.9.203:80/webservice/";


 //private final static String link = "http://172.16.8.58/webservice/"; // IP quentin connextion server PREprode salle 8
   // private final static String link = "http://192.168.89.58/webservice/"; // IP quentin connextion server DMZ salle 8

    //private final static String link = "http://172.16.9.58/webservice/"; // IP quentin connextion server PREprode salle 9
 private final static String link = "http://192.168.89.78/webservice/"; // IP quentin connextion server DMZ salle 9



    public static String request(String function,String infos) {

        StringBuffer chaine = new StringBuffer("");
        try {
            String data = URLEncoder.encode("infos", "UTF-8") + "=" + URLEncoder.encode(infos, "UTF-8");


            URL url = new URL(link +function+".php");
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "", retour = "";

            while ((line = rd.readLine()) != null) {
                Log.d("retour : ", line);
                retour += line;
            }
            wr.close();
            rd.close();
            return retour;

        } catch (Exception e) {
            // writing exception to log

            Log.d("error","error");
            e.printStackTrace();
            return "";
        }


    }
}



