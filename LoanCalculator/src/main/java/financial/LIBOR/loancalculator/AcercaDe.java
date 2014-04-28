package financial.LIBOR.loancalculator;

/**
 * Created by casa on 11/11/13.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AcercaDe extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
        TextView helpText = (TextView) findViewById(R.id.acercade);
        helpText.setText("This application calculates the periodic payment for a loan with ");
        helpText.append("a fixed rate and a fixed spread.\n\n");
        helpText.append("Notional: It is the principal amount of borrowed money\n\n");
        helpText.append("Rate: It is the fixed rferenced interest in parts of the unit\n\n");
        helpText.append("Spread: It is the interest added in parts of the unit to the rate\n\n");
        helpText.append("Payment Frequency: It is the number of payments in a year\n\n");
        helpText.append("Years: Number of year to the maturity date");
    }
}
