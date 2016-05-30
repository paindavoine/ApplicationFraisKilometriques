package my.mysql_php_register_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class Welcome extends Activity {

    private Button log_out;
    private  Button Valider;
    private Button Soumettre;

    private EditText kilometre;
    private TextView y;
    private TextView TOT;
    private TextView textView2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        final String str = (String) getIntent().getSerializableExtra("infosIdent");



         final EditText kilometre = (EditText) findViewById(R.id.NbKilometre);
        final TextView y = (TextView) findViewById(R.id.calcule);
        final  TextView TOT = (TextView) findViewById(R.id.tot);



        log_out = (Button) findViewById(R.id.button);
        Valider = (Button) findViewById(R.id.valider);
        Soumettre = (Button) findViewById(R.id.soumission);





        //bouton deconnexion
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        // affichages des donné dans les spinners
        final Spinner spinnerPuissance = (Spinner) findViewById(R.id.puissance);
        final Spinner spinnerCarburant = (Spinner) findViewById(R.id.carburant);

        final List<String> spinnerArrayPuissance = new ArrayList<String>();
        final ArrayAdapter<String> spinnerAdapterPuissance = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayPuissance);

        final List<String> spinnerArraycarburant = new ArrayList<String>();
        final ArrayAdapter<String> spinnerAdaptercarburant = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArraycarburant);

        final List<String> spinner = new ArrayList<String>();
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArraycarburant);

        final Map<String, String> dictionaryPuissance = new HashMap<String, String>();
        final Map<String, String> dictionaryCarburant = new HashMap<String, String>();

        Thread thread = new Thread(new Runnable() {
            @Override

            public void run (){

                String resultpuissance = WebService.request("service_puissance","");
                String resultCarburant = WebService.request("service_carburant","");

                try {

                    JSONArray jsonArrayPsuissance = new JSONArray(resultpuissance);
                    JSONArray jsonArrayCarburant = new JSONArray(resultCarburant);
                    //Affichage des puissances dans le spinner
                    for (int i =0; i < jsonArrayPsuissance.length();i++ ){
                        JSONObject jsonObject = jsonArrayPsuissance.getJSONObject(i);

                        dictionaryPuissance.put(jsonObject.optString("ID"), jsonObject.optString("LIBELLE"));

                        spinnerArrayPuissance.add(dictionaryPuissance.get(jsonObject.optString("ID")));



                    }

                    // Affichage des carburants dans le spinner
                    for (int i =0; i < jsonArrayCarburant.length();i++ ){
                        JSONObject jsonObject = jsonArrayCarburant.getJSONObject(i);

                        dictionaryCarburant.put(jsonObject.optString("ID"), jsonObject.optString("LIBELLE"));

                        spinnerArraycarburant.add(dictionaryCarburant.get(jsonObject.optString("ID")));
                        //spinnerArraycarburant.add(jsonObject.optString("ID").concat(" - ").concat(jsonObject.optString("LIBELLE")));

                    }
                }
                catch (Exception e){
                    Log.d("WebServices select1", e.toString());
                }
            }

        });

        thread.start();
        spinnerArrayPuissance.add("Puissance");
        spinnerPuissance.setAdapter(spinnerAdapterPuissance);
        spinnerArraycarburant.add("Carburant");
        spinnerCarburant.setAdapter(spinnerAdaptercarburant);

        final   String[] infoPuissanceCarburant2 = new String[2];


        Valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              final   String[] infoPuissanceCarburant = new String[2];

                final CountDownLatch latch =new CountDownLatch(1);
                 new Thread(new Runnable() {
                    @Override

                    public void run() {

                        try {



                            String Puissance = ""+spinnerPuissance.getSelectedItemPosition();
                            Log.d("puissance", Puissance);
                            Log.d("id",str);
                            for (Map.Entry<String, String> entry : dictionaryPuissance.entrySet()){
                                String clePuissance = entry.getKey();
                                String valeurPuissance = entry.getValue();
                                Log.d("ClePuissance", clePuissance);
                                Log.d("ValeurPuissance", valeurPuissance);
                                if (Puissance.equals(clePuissance))
                                {
                                    Puissance = clePuissance.concat(" - ").concat(valeurPuissance);
                                    Log.d("Puissance finale", Puissance);
                                }
                            }

                          // String Carburant = spinnerCarburant.getSelectedItem().toString();
                            //Log.d("carburant", Carburant);
                            String Carburant = ""+spinnerCarburant.getSelectedItemPosition();
                            Log.d("carburant", Carburant);
                            for (Map.Entry<String, String> entry : dictionaryCarburant.entrySet()){
                                String cleCarburant = entry.getKey();
                                String valeurCarburant = entry.getValue();
                                Log.d("Cle", cleCarburant);
                                Log.d("Valeur", valeurCarburant);
                                if (Carburant.equals(cleCarburant))
                                {
                                    Carburant = cleCarburant.concat(" - ").concat(valeurCarburant);
                                    Log.d("Carburant final", Carburant);
                                }
                            }


                            Map<String, String> mapo = new HashMap<String, String>();
                            mapo.put("idpuissance", Puissance.toString());
                            mapo.put("idcarburant", Carburant.toString());


                            JSONObject jsonPuissanceCarburant = new JSONObject(mapo);

                            infoPuissanceCarburant[0] = WebService.request("service_tarif_android", jsonPuissanceCarburant.toString());


                            Map<String, String> mapy = new HashMap<String, String>();
                            mapy.put("idpuissance", Puissance.toString());
                            mapy.put("idcarburant", Carburant.toString());


                            JSONObject jsonPuissanceCarburant2 = new JSONObject(mapo);
                           infoPuissanceCarburant2[0] = WebService.request("service_tarif_id", jsonPuissanceCarburant2.toString());



                            Log.d("TOT", infoPuissanceCarburant[0]);
                           // Log.d("IDTOT", infoPuissanceCarburant2[0]);



                             String Nombrekilometre = kilometre.getText().toString();
                            Log.d("Nombre kilometre", Nombrekilometre);




                            float a = Float.parseFloat(Nombrekilometre);
                            Log.d("FLOAT KILOMETRE", "" + a);


                            float b = Float.parseFloat(infoPuissanceCarburant[0]);
                            Log.d("FLOAT carburant", "" + b);

                            float c = a * b;
                            String s = Float.toString(c);
                            infoPuissanceCarburant[1]= s ;
                            TextView o = (TextView) findViewById(R.id.tot);
                            Log.d("FLOAT", s);



                            Log.d("AAAAAAAAAAA", "");


                        } catch (Exception e) {
                            Log.d("WebServices select2", e.toString());
                        }
                    latch.countDown();
                    }

                })
                .start();

                try{

                    latch.await(3000, TimeUnit.MILLISECONDS);
                }
                catch (Exception e){

                }
                y.setText(infoPuissanceCarburant[1]);
                TOT.setText(infoPuissanceCarburant[0]);


            }
        });




        Soumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        ((Button) findViewById(R.id.soumission)).setEnabled(false);
                final Thread thread = new Thread(new Runnable() {
                    @Override

                    public void run() {

                        try {
                            Map<String, String> map = new HashMap<String, String>();



                            map.put("idvisiteur", str.toString()); // id visiteur
                            Log.d("SOUidvis", str);


                            EditText kilometre = (EditText) findViewById(R.id.NbKilometre);
                            String kilometre2 = kilometre.getText().toString();
                            map.put("km", kilometre2.toString()); // nombre de kilometre
                            Log.d("SOUkilometre", kilometre2);

                            String yy = y.getText().toString();
                            map.put("total", yy.toString()); // total
                            Log.d("SOUtoal", yy);

                           map.put("idprix", infoPuissanceCarburant2[0].toString()); // id en fonction carburant et puissance
                           Log.d("SouIDpuiscar", infoPuissanceCarburant2[0]);

                            JSONObject jsonsoumission = new JSONObject(map);

                            String soumission = WebService.request("service_soumission", jsonsoumission.toString());




                        } catch (Exception e) {
                            Log.d("WebServices select3", e.toString());
                        }

                    }

                });
                thread.start();



                    new Handler().postDelayed(new Runnable() {



                        @Override
                        public void run() {
                            ((Button) findViewById(R.id.soumission))
                                    .setEnabled(true);

                                Toast.makeText(getApplicationContext(),
                                        "Vos données ont été envoyées", Toast.LENGTH_LONG).show();


                        }
                    }, 3000);



            }
        });



    }




}
