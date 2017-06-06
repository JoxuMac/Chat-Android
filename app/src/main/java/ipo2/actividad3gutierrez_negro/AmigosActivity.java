/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/

package ipo2.actividad3gutierrez_negro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

public class AmigosActivity extends AppCompatActivity {

    public ArrayList<Contacto> contactos = new ArrayList<>();
    public ListView lstAmigos;
    public AdaptadorLista adaptador;

    public static final String TAG_CONTACTOS = "contactos";
    public static final String TAG_NOMBRE = "usuario";
    public static final String TAG_TELEFONO = "telefono";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        this.setTitle("Amigos");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        lstAmigos = (ListView) findViewById(R.id.lstAmigos);
        adaptador = new AdaptadorLista(this, contactos);
        lstAmigos.setAdapter(adaptador);

        final Activity activityAmigos = this;

        lstAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(activityAmigos, ConversacionActivity.class);
                i.putExtra("id", ((Contacto) lstAmigos.getItemAtPosition(position)).getID());
                i.putExtra("nombre", ((Contacto) lstAmigos.getItemAtPosition(position)).getNombre());
                activityAmigos.startActivity(i);
            }
        });

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String sufURL = "listaramigos.php?usuario=" + Usuario.getUser();

        LeerContactosURL amigos = new LeerContactosURL();
        amigos.execute(URL_server + sufURL);

        ((BaseAdapter) lstAmigos.getAdapter()).notifyDataSetChanged();

    }

    public void click_VolverAmigos(View v) {
        Intent i = new Intent(this, ConversacionesActivity.class);
        this.startActivity(i);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class LeerContactosURL extends AsyncTask<String, Void, String> {
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

            contactos.removeAll(contactos);
            contactos = parsearJSON(s);

            if (contactos != null) {
                ((BaseAdapter) lstAmigos.getAdapter()).notifyDataSetChanged();
            } else {
                Toast.makeText(AmigosActivity.this, "Ocurrió un error de Parsing", Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<Contacto> parsearJSON(String json) {
            JSONArray contactosJSON = null;
            JSONObject jsonObject = null;
            Contacto contacto;
            try {
                jsonObject = new JSONObject(json);
                contactosJSON = jsonObject.getJSONArray(TAG_CONTACTOS);
                for (int i = 0; i < contactosJSON.length(); i++) {
                    jsonObject = contactosJSON.getJSONObject(i);
                    contacto = new Contacto(jsonObject.getString(TAG_NOMBRE), jsonObject.getString(TAG_TELEFONO), jsonObject.getInt(TAG_FOTO), jsonObject.getInt(TAG_ID));
                    contactos.add(contacto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return contactos;
        }
    }
}