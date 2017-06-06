/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/

package ipo2.actividad3gutierrez_negro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static ipo2.actividad3gutierrez_negro.Constantes.URL_server;

public class ConversacionActivity extends AppCompatActivity {

    public ArrayList<Mensajes> mensajes = new ArrayList<>();
    public ListView lstMensajes;
    public AdaptadorMensajes adaptador;

    public static final String TAG_CONTACTOS = "mensajes";
    public static final String TAG_ENVIO = "envio";
    public static final String TAG_RECEPTOR = "receptor";
    public static final String TAG_MENSAJE = "mensaje";
    private String sufURL;

    private EditText txtMsg;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion);
        bundle = getIntent().getExtras();
        this.setTitle(bundle.getString("nombre"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstMensajes = (ListView) findViewById(R.id.lstMensajes);
        adaptador = new AdaptadorMensajes(this, mensajes);
        lstMensajes.setAdapter(adaptador);

        txtMsg = (EditText) findViewById(R.id.txtMsg);

        sufURL = "vermensajes.php?envio=" + Usuario.getUser() + "&receptor=" + bundle.getInt("id");

        LeerMensajesURL msg = new LeerMensajesURL();
        msg.execute(URL_server + sufURL);

        ((BaseAdapter) lstMensajes.getAdapter()).notifyDataSetChanged();
    }

    public void click_VolverAmigos(View v) {
        Intent i = new Intent(this, ConversacionesActivity.class);
        this.startActivity(i);
    }

    public void click_EnviarMensaje(View v) {
        if ((txtMsg.getText()).toString().trim().length() > 0) {
            String sufijoURL = "escribirmensaje.php?envio=" + Usuario.getUser() + "&receptor=" + bundle.getInt("id") + "&mensaje=" + txtMsg.getText();

            EscribirMensajeURL escribirMsg = new EscribirMensajeURL();
            escribirMsg.execute(URL_server + sufijoURL);

            LeerMensajesURL msg = new LeerMensajesURL();
            msg.execute(URL_server + sufURL);

            txtMsg.setText("");
        }
    }

    class EscribirMensajeURL extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(ConversacionActivity.this, s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String s = params[0];
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(s);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String cadenaDevueltaPHP;
                cadenaDevueltaPHP = bufferedReader.readLine();
                return cadenaDevueltaPHP;
            } catch (Exception e) {
                return null;
            }
        }
    }

    class LeerMensajesURL extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String uri = params[0];
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String cadenaDevueltaPHP;
                while ((cadenaDevueltaPHP = bufferedReader.readLine()) != null) {
                    sb.append(cadenaDevueltaPHP + "\n");
                }
                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mensajes.removeAll(mensajes);
            mensajes = parsearJSON(s);

            if (mensajes != null) {
                ((BaseAdapter) lstMensajes.getAdapter()).notifyDataSetChanged();
            } else {
                Toast.makeText(ConversacionActivity.this, "Ocurrió un error de Parsing", Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<Mensajes> parsearJSON(String json) {
            JSONArray contactosJSON = null;
            JSONObject jsonObject = null;
            Mensajes mensaje;
            try {
                jsonObject = new JSONObject(json);
                contactosJSON = jsonObject.getJSONArray(TAG_CONTACTOS);
                for (int i = 0; i < contactosJSON.length(); i++) {
                    jsonObject = contactosJSON.getJSONObject(i);
                    System.out.println();
                    mensaje = new Mensajes(jsonObject.getString(TAG_MENSAJE), jsonObject.getInt(TAG_ENVIO), jsonObject.getInt(TAG_RECEPTOR), jsonObject.getInt(TAG_ENVIO));
                    mensajes.add(mensaje);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mensajes;
        }
    }
}
