package financial.LIBOR.loancalculator;

/**
 * Created by casa on 24/11/13.
 */

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.text.DecimalFormat;
import java.util.Vector;
import android.widget.Toast;

public class amortization_detail extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){

        Bundle extra = this.getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.amortization);

        double notional;
        double rate;
        double spread;
        int payment_frequency;
        int years;
        Vector<String> schedule = new Vector<String>();

        String numberField = "   ";
        String amortizationField = "            ";
        String interestField = "          ";
        String notionalField = "              ";

        notional = extra.getDouble("financial.LIBOR.loancalculator.NOTIONAL");
        rate = extra.getDouble("financial.LIBOR.loancalculator.RATE");
        spread = extra.getDouble("financial.LIBOR.loancalculator.SPREAD");
        payment_frequency = extra.getInt("financial.LIBOR.loancalculator.PAYMENT_FREQUENCY");
        years = extra.getInt("financial.LIBOR.loancalculator.YEARS");

        Mortgage mortgage = new Mortgage(notional, rate, spread, payment_frequency, years);

        schedule.clear();

        DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,###.##");

        for (int i = 0; i < payment_frequency*years; ++i){
            double amortization = mortgage.getAmortization(i+1);
            double pay = mortgage.getPayment();
            double accum = mortgage.accumulativeAmortization(i+1);
            schedule.add(numberField.substring(0, numberField.length()-myFormatter.format(i+1).length()) +
                    myFormatter.format(i+1) + ' ' +
                    amortizationField.substring(0, amortizationField.length()-
                            myFormatter.format(amortization).length()) +
                    myFormatter.format(amortization)+ ' ' +
                    interestField.substring(0, interestField.length()-
                            myFormatter.format(pay-amortization).length()) +
                    myFormatter.format(pay-amortization) + ' ' +
                    notionalField.substring(0, notionalField.length()-
                            myFormatter.format(notional-accum).length()) +
                    myFormatter.format(notional-accum));
        }
        setListAdapter(new AmortizationAdapter(this, schedule));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView, view, position, id);
        Object o = getListAdapter().getItem(position);
        String numberField = "   ";
        String amortizationField = "            ";
        String interestField = "          ";
        String notionalField = "              ";
        String cadena = o.toString();
        String texto = "Amortization : " + cadena.substring(numberField.length()+1,
                        numberField.length()+1+amortizationField.length()) + "\n" +
                       "Interst      : " + cadena.substring(numberField.length()+1+
                        amortizationField.length()+1,
                        numberField.length()+1+amortizationField.length() + 1 +
                        interestField.length()) + "\n" +
                       "Notional     : " + cadena.substring(numberField.length()+1+
                        amortizationField.length() + 1 + interestField.length()+1,
                        numberField.length()+1+amortizationField.length() + 1 +
                        interestField.length()+1+notionalField.length());
        Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }

}

