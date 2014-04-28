package financial.LIBOR.loancalculator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import com.google.ads.*;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.File;

public class MainActivity extends Activity implements OnClickListener {

    EditText notionalED;
    EditText rateED;
    EditText spreadED;
    EditText paymentFrequencyED;
    EditText yearsED;
    AlertDialog alertExit;
    AlertDialog Error;
    Mortgage mortgage;
    ProgressDialog progressDialog;
    AdView adView;



    public class RetrieveRate extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... url){
            try {
                URL x = new URL(url[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(x.openStream()));
                String cadena = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    cadena = cadena.concat(inputLine);
                in.close();
                int inicio = cadena.indexOf("Euribor hipotecario");
                inicio = cadena.indexOf("<dd>", inicio);
                int fin = cadena.indexOf("%", inicio);
                return cadena.substring(inicio+4, fin-1);
            } catch (Exception e){
                this.exception = e;
                return "";
            }
        }

        protected void onPostExecute(String rate){
            DecimalFormat myFormatter = new DecimalFormat("#.#####");
            rateED = (EditText) findViewById(R.id.editText2);
            rate = rate.replace(',','.');
            rateED.setText(myFormatter.format(Double.parseDouble(rate)).replace(',','.'));
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

  //      TableLayout _anuncio = (TableLayout) findViewById(R.id.main_table);
  //      adView = new AdView(this, AdSize.BANNER, "ca-app-pub-2613488639187412/6980628685");
  //      _anuncio.addView(adView);
  //      AdRequest adRequest = new AdRequest();
  //      adRequest.addTestDevice("4E2D831C8290D0E64812B6945ACFDEFF");
  //      adView.loadAd(adRequest);


        notionalED = (EditText) findViewById(R.id.editText1);
        rateED = (EditText) findViewById(R.id.editText2);
        spreadED = (EditText) findViewById(R.id.editText3);
        paymentFrequencyED = (EditText) findViewById(R.id.editText4);
        yearsED = (EditText) findViewById(R.id.editText5);


        progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
        new RetrieveRate().execute("http://www.bde.es/bde/es/");
        View __button = findViewById(R.id.button1);
        __button.setOnClickListener(this);
        View __button_2 = findViewById(R.id.button2);
        __button_2.setOnClickListener(this);
        View __button_3 = findViewById(R.id.button3);
        __button_3.setOnClickListener(this);
        View __button_4 = findViewById(R.id.button4);
        __button_4.setOnClickListener(this);
        View __button_5 = findViewById(R.id.button5);
        __button_5.setOnClickListener(this);

        __button.setFocusableInTouchMode(true);
        __button.requestFocus();

        AlertDialog.Builder builderYesNo = new AlertDialog.Builder(this);
        builderYesNo.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertExit = builderYesNo.create();

        AlertDialog.Builder builderOk = new AlertDialog.Builder(this);
        builderOk.setMessage("Numeric Error")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        Error = builderOk.create();

    }

    private Boolean validate(String _notional, String _rate, String _spread, String _frequency, String _years){
        int fieldNumber = 0;
        String textError;
        try {
                fieldNumber = 1;
                double notional = Double.parseDouble(_notional);
                fieldNumber = 2;
                double rate = Double.parseDouble(_rate);
                fieldNumber = 3;
                double spread = Double.parseDouble(_spread);
                fieldNumber = 4;
                int paymentFrequency = Integer.parseInt(_frequency);
                fieldNumber = 5;
                int years = Integer.parseInt(_years);
        } catch(Exception e){
            switch (fieldNumber) {
                case 1:
                    textError = "Notional must be a real number";
                    break;
                case 2:
                    textError = "Rate must be a real number";
                    break;
                case 3:
                    textError = "Spread must be a real number";
                    break;
                case 4:
                    textError = "Frequency must be an integer number";
                    break;
                case 5:
                    textError = "Years must be an integer number";
                    break;
                default:
                    textError = "Fatal error";
                    break;
            }
            Error.setMessage(textError);
            Error.show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button1){
            if (!(this.validate(notionalED.getText().toString(), rateED.getText().toString(),
                    spreadED.getText().toString(), paymentFrequencyED.getText().toString(),
                    yearsED.getText().toString()))) {
                double notional = Double.parseDouble(notionalED.getText().toString());
                double rate = Double.parseDouble(rateED.getText().toString())/100.0;
                double spread = Double.parseDouble(spreadED.getText().toString())/100.0;
                int paymentFrequency = Integer.parseInt(paymentFrequencyED.getText().toString());
                int years = Integer.parseInt(yearsED.getText().toString());
                mortgage = new Mortgage(notional, rate, spread, paymentFrequency, years);

                Intent i = new Intent(this, Results.class);
                i.putExtra("financial.LIBOR.loancalculator.PAYMENT", mortgage.getPayment());
                startActivity(i);

            }
        }
        else if (v.getId()==R.id.button2){
            Intent i = new Intent(this, AcercaDe.class);
            startActivity(i);
        }
        else if (v.getId()==R.id.button3){
            alertExit.show();
        }
        else if (v.getId()==R.id.button4){
            if (!(this.validate(notionalED.getText().toString(), rateED.getText().toString(),
                    spreadED.getText().toString(), paymentFrequencyED.getText().toString(),
                    yearsED.getText().toString()))){
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");

                class ExecuteAmortizationDetails extends AsyncTask<String, Void, Void>{
                    private Exception exception;

                    protected Void doInBackground(String... url){
                        try{
                            double notional = Double.parseDouble(notionalED.getText().toString());
                            double rate = Double.parseDouble(rateED.getText().toString())/100.0;
                            double spread = Double.parseDouble(spreadED.getText().toString())/100.0;
                            int paymentFrequency = Integer.parseInt(paymentFrequencyED.getText().toString());
                            int years = Integer.parseInt(yearsED.getText().toString());
                            Intent i = new Intent(MainActivity.this, amortization_detail.class);
                            i.putExtra("financial.LIBOR.loancalculator.NOTIONAL", notional);
                            i.putExtra("financial.LIBOR.loancalculator.RATE", rate);
                            i.putExtra("financial.LIBOR.loancalculator.SPREAD", spread);
                            i.putExtra("financial.LIBOR.loancalculator.PAYMENT_FREQUENCY", paymentFrequency);
                            i.putExtra("financial.LIBOR.loancalculator.YEARS", years);
                            startActivity(i);
                        } catch (Exception e) {
                            this.exception = e;
                            return null;
                        }
                        return null;
                    }

                    protected void onPostExecute(Void nothing){
                        progressDialog.dismiss();
                    }
                }
                new ExecuteAmortizationDetails().execute();
            }
        }
        else if (v.getId()==R.id.button5){
            if (!(this.validate(notionalED.getText().toString(), rateED.getText().toString(),
                spreadED.getText().toString(), paymentFrequencyED.getText().toString(),
                yearsED.getText().toString()))){
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Writing to file...");

                class WriteAmortizationDetails extends AsyncTask<String, Void, Void>{
                    private Exception exception;

                    protected Void doInBackground(String... url){
                        try{
                            String testSDCard = Environment.getExternalStorageState();
                            File root = Environment.getExternalStorageDirectory();
                            File dir = new File(root.getAbsolutePath()+"/dataLoanCalculator");
                            if (!dir.exists()) dir.mkdirs();
                            File file = new File(dir, "AmortizationData.txt");

                            String numberField = "   ";
                            String amortizationField = "            ";
                            String interestField = "          ";
                            String notionalField = "              ";
                            Boolean FirstPayment = true;

                            FileOutputStream fos = new FileOutputStream(file);
                            PrintWriter f = new PrintWriter(fos);

                            double notional = Double.parseDouble(notionalED.getText().toString());
                            double rate = Double.parseDouble(rateED.getText().toString())/100.0;
                            double spread = Double.parseDouble(spreadED.getText().toString())/100.0;
                            int paymentFrequency = Integer.parseInt(paymentFrequencyED.getText().toString());
                            int years = Integer.parseInt(yearsED.getText().toString());

                            Mortgage mortgage = new Mortgage(notional, rate, spread, paymentFrequency, years);

                            DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,###.##");

                            for (int i = 0; i < paymentFrequency*years; ++i){
                                double amortization = mortgage.getAmortization(i+1);
                                double pay = mortgage.getPayment();
                                double accum = mortgage.accumulativeAmortization(i+1);
                                f.println(numberField.substring(0, numberField.length() - myFormatter.format(i + 1).length()) +
                                        myFormatter.format(i + 1) + ';' +
                                        amortizationField.substring(0, amortizationField.length() -
                                                myFormatter.format(amortization).length()) +
                                        myFormatter.format(amortization) + ';' +
                                        interestField.substring(0, interestField.length() -
                                                myFormatter.format(pay - amortization).length()) +
                                        myFormatter.format(pay - amortization) + ';' +
                                        notionalField.substring(0, notionalField.length() -
                                                myFormatter.format(notional - accum).length()) +
                                        myFormatter.format(notional - accum));
                            }
                            f.flush();
                            f.close();
                        } catch (Exception e) {
                            this.exception = e;
                            return null;
                            }
                        return null;
                    }

                    protected void onPostExecute(Void nothing){
                        progressDialog.dismiss();
                    }
                }
                new WriteAmortizationDetails().execute();
            }
        }

    }

    @Override
    public void onPause(){
        adView = (AdView) findViewById(R.id.adView);
        if (adView != null){
            ((ViewGroup)adView.getParent()).removeView(adView);
            adView.destroy();
        }
        super.onPause();
    }
}
