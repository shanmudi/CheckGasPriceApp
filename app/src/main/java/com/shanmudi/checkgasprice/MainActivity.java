package com.shanmudi.checkgasprice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> OldNews = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            //Toast.makeText(MainActivity.this,"Saved State", Toast.LENGTH_SHORT).show();
            TextView UpdateTimeTextBox = (TextView) findViewById(R.id.UpdateTimeText);
            UpdateTimeTextBox.setText("last Updates: " + UpdateTime());
            new News680().execute();
            new GasPredictor().execute();
            new GasMarkham().execute();
            new GasSheppard().execute();
            new GasVictoria().execute();
        }

        final Button RefreshButton = (Button) findViewById(R.id.RefreshButton);
        assert RefreshButton != null;
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new News680().execute();
                new GasPredictor().execute();
                new GasMarkham().execute();
                new GasSheppard().execute();
                new GasVictoria().execute();
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                TextView UpdateTimeTextBox = (TextView) findViewById(R.id.UpdateTimeText);
                UpdateTimeTextBox.setText("last Updates: " + UpdateTime());

            }
        });

        //makes the textbox about gas station open url when user taps on it
        TextView MarkahmText = (TextView) findViewById(R.id.textView3);
        MarkahmText.setMovementMethod(LinkMovementMethod.getInstance());

        TextView SheppardText = (TextView) findViewById(R.id.textView2);
        SheppardText.setMovementMethod(LinkMovementMethod.getInstance());

        TextView VictoriaText = (TextView) findViewById(R.id.textView);
        VictoriaText.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private class News680 extends AsyncTask<Void, Void, Void> {
        String result;
        String result2;
        String result3;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("http://www.680news.com/toronto-gta-gas-prices/").get();

                Elements links = doc.select("p");
                result = links.first().text();
                result2 = links.get(1).text();
                result3 = links.get(2).text();
                if (result3.startsWith("We've")) {
                    result3 = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView News680Gas = (TextView) findViewById(R.id.News680_1);
            TextView EnPro = (TextView) findViewById(R.id.News680_2);
            TextView Future = (TextView) findViewById(R.id.Future);
            News680Gas.setText(result);
            EnPro.setText(result2);
            Future.setText(result3);

            super.onPostExecute(aVoid);
        }
    }

    private class GasPredictor extends AsyncTask<Void, Void, Void> {
        String date, trend, trend23, recommendation;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc5 = Jsoup.connect("http://www.gaspredictor.com/").get();
                Elements link5 = doc5.select("P");
                date = link5.get(2).text();
                trend = link5.get(6).text();
                trend23 = link5.get(7).text();
                recommendation = link5.get(8).text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView GasPDate = (TextView) findViewById(R.id.GasPDate);
            TextView GasPTrend = (TextView) findViewById(R.id.GasPTrend);
            TextView GasPTrend23 = (TextView) findViewById(R.id.GasPTrend23);
            TextView GasPRecommend = (TextView) findViewById(R.id.GasPRecommend);
            GasPDate.setText(date);
            GasPTrend.setText(trend);
            GasPTrend23.setText(trend23);
            GasPRecommend.setText(recommendation);
            super.onPostExecute(aVoid);
        }


    }

    private class GasMarkham extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... params) {
            //1525 Markham Road
            try {
                Document doc2 = Jsoup.connect("http://www.petro-canada.ca/en/locations/find-a-gas-station.aspx?MODE=DTS&ID=699").get();
                result = Listtext(doc2);
                //if OldNews has old data about Markham Gas Price
                if (OldNews.containsKey("MarkhamOld")) {
                    try {
                        if (OldNews.get("MarkhamOld").toString().startsWith("Gas prices are not available") && result.startsWith("As of")) {
                            OldNews.put("MarkhamOld", result);
                        } else if (OldNews.get("MarkhamOld").toString().startsWith("As of") && result.startsWith("Gas prices")) {
                            result = OldNews.get("MarkhamOld").toString().concat("*");
                        }
                    } catch (NullPointerException e) {

                    }
                } else {
                    if (result.startsWith("As of")) {
                        OldNews.put("MarkhamOld", result);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView MarkhamGasPrice = (TextView) findViewById(R.id.GasMarkham);

            MarkhamGasPrice.setText(result);
            super.onPostExecute(aVoid);
        }
    }

    private class GasSheppard extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //5110 Sheppard Ave. East
                Document doc3 = Jsoup.connect("http://www.petro-canada.ca/en/locations/find-a-gas-station.aspx?MODE=DTS&ID=683").get();
                result = Listtext(doc3);
                //if OldNews has old data about Sheppard Gas Price
                if (OldNews.containsKey("SheppardOld")) {
                    try {
                        if (OldNews.get("SheppardOld").toString().startsWith("Gas prices are not available") && result.startsWith("As of")) {
                            OldNews.put("SheppardOld", result);
                        } else if (OldNews.get("SheppardOld").toString().startsWith("As of") && result.startsWith("Gas prices")) {
                            result = OldNews.get("SheppardOld").toString().concat("*");
                        }
                    } catch (NullPointerException e) {

                    }
                } else {
                    if (result.startsWith("As of")) {
                        OldNews.put("SheppardOld", result);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView SheppardGasPrice = (TextView) findViewById(R.id.GasSheppard);
            SheppardGasPrice.setText(result);
            super.onPostExecute(aVoid);
        }
    }

    private class GasVictoria extends AsyncTask<Void, Void, Void> {
        String result, oldresult = "";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //2250 Victoria Park Avenue
                Document doc4 = Jsoup.connect("http://www.petro-canada.ca/en/locations/4085.aspx?MODE=DTS&ID=59124").get();
                result = Listtext(doc4);
                //if OldNews has old data about Victoria Gas Price
                if (OldNews.containsKey("VictoriaOld")) {
                    try {
                        if (OldNews.get("VictoriaOld").toString().startsWith("Gas prices are not available") && result.startsWith("As of")) {
                            OldNews.put("VictoriaOld", result);
                        } else if (OldNews.get("VictoriaOld").toString().startsWith("As of") && result.startsWith("Gas prices")) {
                            result = OldNews.get("VictoriaOld").toString().concat("*");
                        }
                    } catch (NullPointerException e) {
                    }
                } else {
                    if (result.startsWith("As of")) {
                        OldNews.put("VictoriaOld", result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView VictoriaGas = (TextView) findViewById(R.id.GasVictoria);
            VictoriaGas.setText(result);
            super.onPostExecute(aVoid);
        }
    }

    private static String Listtext(Document doc) {
        Elements element = doc.select("tr");
        for (Element link : element) {
            if (link.text().startsWith("As")) {
                return link.text();
//                break;
            }
            if (link.text().startsWith("Gas prices are not available at this time.")) {
                return link.text();
//                break;
            }

        }
        return "Nothing";
    }

    private String UpdateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(("M-D HH:mm:ss"));
        String UpdateTime = dateFormat.format(c.getTime());

        return UpdateTime;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView News680Gas = (TextView) findViewById(R.id.News680_1);
        outState.putString("News680Gas", News680Gas.getText().toString());
        TextView EnPro = (TextView) findViewById(R.id.News680_2);
        outState.putString("EnPro", EnPro.getText().toString());
        TextView Future = (TextView) findViewById(R.id.Future);
        outState.putString("Future", Future.getText().toString());


        TextView GasPDate = (TextView) findViewById(R.id.GasPDate);
        outState.putString("GasPDate", GasPDate.getText().toString());
        TextView GasPTrend = (TextView) findViewById(R.id.GasPTrend);
        outState.putString("GasPTrend", GasPTrend.getText().toString());
        TextView GasPTrend23 = (TextView) findViewById(R.id.GasPTrend23);
        outState.putString("GasPTrend23", GasPTrend23.getText().toString());
        TextView GasPRecommend = (TextView) findViewById(R.id.GasPRecommend);
        outState.putString("GasPRecommend", GasPRecommend.getText().toString());


        TextView MarkhamGasPrice = (TextView) findViewById(R.id.GasMarkham);
        outState.putString("SaveMarkham", MarkhamGasPrice.getText().toString());

        TextView SheppardGasPrice = (TextView) findViewById(R.id.GasSheppard);
        outState.putString("SheppardGasPrice", SheppardGasPrice.getText().toString());

        TextView VictoriaGas = (TextView) findViewById(R.id.GasVictoria);
        outState.putString("VictoriaGas", VictoriaGas.getText().toString());

        TextView UpdateTimeTextBox = (TextView) findViewById(R.id.UpdateTimeText);
        outState.putString("TimeUpdate", UpdateTimeTextBox.getText().toString());
//        Toast.makeText(MainActivity.this,"Saved State", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        TextView News680Gas = (TextView) findViewById(R.id.News680_1);
        News680Gas.setText(savedInstanceState.get("News680Gas").toString());
        TextView EnPro = (TextView) findViewById(R.id.News680_2);
        EnPro.setText(savedInstanceState.get("EnPro").toString());
        TextView Future = (TextView) findViewById(R.id.Future);
        Future.setText(savedInstanceState.get("Future").toString());

        TextView GasPDate = (TextView) findViewById(R.id.GasPDate);
        GasPDate.setText(savedInstanceState.get("GasPDate").toString());
        TextView GasPTrend = (TextView) findViewById(R.id.GasPTrend);
        GasPTrend.setText(savedInstanceState.get("GasPTrend").toString());
        TextView GasPTrend23 = (TextView) findViewById(R.id.GasPTrend23);
        GasPTrend23.setText(savedInstanceState.get("GasPTrend23").toString());
        TextView GasPRecommend = (TextView) findViewById(R.id.GasPRecommend);
        GasPRecommend.setText(savedInstanceState.get("GasPRecommend").toString());

        TextView MarkhamGasPrice = (TextView) findViewById(R.id.GasMarkham);
        MarkhamGasPrice.setText(savedInstanceState.get("SaveMarkham").toString());

        TextView SheppardGasPrice = (TextView) findViewById(R.id.GasSheppard);
        SheppardGasPrice.setText(savedInstanceState.get("SheppardGasPrice").toString());

        TextView VictoriaGas = (TextView) findViewById(R.id.GasVictoria);
        VictoriaGas.setText(savedInstanceState.get("VictoriaGas").toString());

        TextView UpdateTimeTextBox = (TextView) findViewById(R.id.UpdateTimeText);
        UpdateTimeTextBox.setText(savedInstanceState.get("TimeUpdate").toString());
//        Toast.makeText(MainActivity.this,"Saved Restore", Toast.LENGTH_SHORT).show();

    }


}
