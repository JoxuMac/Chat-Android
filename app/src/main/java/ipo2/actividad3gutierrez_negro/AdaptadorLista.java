/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/

package ipo2.actividad3gutierrez_negro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorLista extends ArrayAdapter {
    Activity context;
    private ArrayList<Contacto> contactos;

    AdaptadorLista(Activity context, ArrayList<Contacto> contactos) {
        super(context, R.layout.layout_item, contactos);
        this.context = context;
        this.contactos = contactos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_item, null);
        TextView lblNombre = (TextView) item.findViewById(R.id.lblNombre);
        lblNombre.setText(contactos.get(position).getNombre());
        TextView lblTelefono = (TextView) item.findViewById(R.id.lblTelefono);
        lblTelefono.setText(contactos.get(position).getTelefono());
        ImageView imagContacto = (ImageView) item.findViewById(R.id.imagContacto);
        switch (contactos.get(position).getTipo()) {
            case 1:
                imagContacto.setImageResource(R.drawable.t1);
                break;
            case 2:
                imagContacto.setImageResource(R.drawable.t2);
                break;
            case 3:
                imagContacto.setImageResource(R.drawable.t3);
                break;
            case 4:
                imagContacto.setImageResource(R.drawable.t4);
        }
        return (item);
    }
}
