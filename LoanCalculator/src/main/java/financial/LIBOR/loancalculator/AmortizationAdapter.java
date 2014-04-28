package financial.LIBOR.loancalculator;

/**
 * Created by casa on 26/11/13.
 */
import android.app.Activity;
import java.util.Vector;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.*;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

/** public class AmortizationAdapter<T> extends ArrayAdapter<T> {

    public AmortizationAdapter(Context context, int layoutId, int textViewResourceId, Vector<T> Objects)
    {
        super(context, layoutId, textViewResourceId, Objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 1) {
            view.setBackgroundColor(Color.GRAY);
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }
        return view;
    }
} **/

public class AmortizationAdapter extends BaseAdapter {
    private final Activity actividad;
    private final Vector<String> lista;

    public AmortizationAdapter(Activity actividad, Vector<String> lista){
        super();
        this.actividad = actividad;
        this.lista = lista;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String elemento;
        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(R.layout.amortization_element, null, true);
        elemento = lista.elementAt(position);
        TextView amortization_number = (TextView) view.findViewById(R.id.amortization_amount_number);
        TextView amortization_amount = (TextView) view.findViewById(R.id.amortization_amount_amortization);
        TextView amortization_interest = (TextView) view.findViewById(R.id.amortization_amount_interest);
        TextView amortization_pending = (TextView) view.findViewById(R.id.amortization_amount_pending);
        amortization_number.setText(elemento.substring(0,4));
        amortization_amount.setText(elemento.substring(4,17));
        amortization_interest.setText(elemento.substring(17,28));
        amortization_pending.setText(elemento.substring(28));
        if (position % 2 == 1) {
            view.setBackgroundColor(Color.GRAY);
        }
        else {
            view.setBackgroundColor(Color.WHITE);
        }
        return view;
    }

    public int getCount(){
        return lista.size();
    }

    public Object getItem(int arg0){
        return lista.elementAt(arg0);
    }

    public long getItemId(int position){
        return position;
    }
}