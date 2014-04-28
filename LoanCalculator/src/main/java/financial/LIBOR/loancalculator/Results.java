package financial.LIBOR.loancalculator;

/**
 * Created by casa on 15/11/13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Results extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle extra = this.getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,###.##");

        TextView paymentTV = (TextView) findViewById(R.id.result_text);
        String paymentString = '\t' + myFormatter.format(extra.getDouble("financial.LIBOR.loancalculator.PAYMENT")) + '\n';
        paymentTV.setText(paymentString);
    }
}

