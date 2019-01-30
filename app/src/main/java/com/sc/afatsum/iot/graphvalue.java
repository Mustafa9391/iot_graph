package com.sc.afatsum.iot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.sc.afatsum.iot.Config.ValToShow;

public class graphvalue extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    Random rand = new Random();
    String json_finalstring;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    double y, x;
    GraphView graph;
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    public String capteur = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tx_capteur = (TextView) findViewById(R.id.textView);
        tx_capteur.setText(capteur);

        x = -5.0;
        graph = (GraphView) findViewById(R.id.graph1);
        DataPoint v = new DataPoint(0, 0);
        DataPoint v2 = new DataPoint(0, 0);
        DataPoint[] values = new DataPoint[2];
        values[0] = v;
        values[1] = v2;
        series = new LineGraphSeries<>(values);
        graph.addSeries(series);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {

            public void run() {
                //affiche_graph();
                getjson();
                mHandler.postDelayed(this, 3000);
            }

        };
        mHandler.postDelayed(mTimer1, 3000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        super.onPause();
    }

    //********************
    public void getjson() {


        //ouvrir la fonction back_grounftask en mode "background"
        new back_grounftask().execute();


    }

    class back_grounftask extends AsyncTask<Void, Void, String> {
        String json_url;
        String json_string;

        @Override
        protected void onPreExecute() {
            json_url = "http://agri-innov.com/plugins/echarts/charts/val_temp.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String Result) {
            try {
                json_finalstring = Result;
                generateData();

            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                //ouvrir la connection au script
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                //récuperer la chaine de sortie sous le format JSON
                while ((json_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(json_string + "\n");
                }
                //fermer le tampon
                bufferedReader.close();
                is.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //******************************
    public void generateData() {
        int count = 100;
        // DataPoint[] values = new DataPoint[count];
        ArrayList<DataPoint> values = new ArrayList<DataPoint>();
        int x = 0, y = 0, j = 0;
        json_string = json_finalstring;
        try {

            //Décodage de la chaine "json_string"
            jsonObject = new JSONObject("{ \"capteur\":" + json_string + "}");
            jsonArray = jsonObject.getJSONArray("capteur");
            Log.d("jsonval", jsonArray.getJSONObject(0).toString());


            for (int i = 0; i < jsonArray.length(); i++) {
                //System.out.println("value y "+ja.getString(i));
                y = Integer.parseInt(jsonArray.getJSONObject(i).getString(ValToShow == 1 ? "val_temp_exter" : "val_humd_exter"));
                x = x + 1;
                DataPoint v = new DataPoint(x, y);
                values.add(v);
            }
            System.out.println(values.size());
            DataPoint[] stringArray = values.toArray(new DataPoint[0]);
            series.resetData(stringArray);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(json_finalstring);
        }


    }

}

