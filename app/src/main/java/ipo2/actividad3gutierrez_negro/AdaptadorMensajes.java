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

public class AdaptadorMensajes extends ArrayAdapter {
    Activity context;
    private ArrayList<Mensajes> mensajes;

    AdaptadorMensajes(Activity context, ArrayList<Mensajes> mensajes) {
        super(context, R.layout.layout_mensajes, mensajes);
        this.context = context;
        this.mensajes = mensajes;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_mensajes, null);
        TextView lblMensaje = (TextView) item.findViewById(R.id.lblMensaje);
        lblMensaje.setText(mensajes.get(position).getMensaje());
        // TextView lblTelefono = (TextView) item.findViewById(R.id.lblTelefono);
        // lblTelefono.setText(contactos.get(position).getTelefono());
        ImageView imagContacto = (ImageView) item.findViewById(R.id.imagContacto);
        switch (mensajes.get(position).getEnvio()) {
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
