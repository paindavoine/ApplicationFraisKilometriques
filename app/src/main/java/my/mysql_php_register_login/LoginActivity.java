package my.mysql_php_register_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText nom,password;
    private Button sign_in;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nom = (EditText) findViewById(R.id.nom);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in);



        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override

                    public void run (){

                        try {

                            Map<String, String>map=new HashMap<String, String>();
                            map.put("login",nom.getText().toString());
                            map.put("password", password.getText().toString());
                            JSONObject jsonconnexion = new JSONObject(map);

                            String connexion = WebService.request("service_connexion", jsonconnexion.toString());

                            Log.d("test",connexion);



                           if (!connexion.equals("0") ){ // remmetre le ! avant connexion


                               Intent intent = new Intent(LoginActivity.this, Welcome.class);
                               intent.putExtra("infosIdent", connexion);
                               startActivity(intent);



                           }

                        }
                        catch (Exception e){
                            Log.d("WebServices select", e.toString());
                        }
                    }

                });
                thread.start();


            }
        });
    }
}


