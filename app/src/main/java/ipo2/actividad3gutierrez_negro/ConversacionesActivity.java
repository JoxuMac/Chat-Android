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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
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

public class ConversacionesActivity extends AppCompatActivity {

    public ArrayList<Contacto> contactos = new ArrayList<>();
    public ListView lstConversaciones;
    public AdaptadorLista adaptador;
    private int contactoSeleccionado;

    public static final String TAG_CONTACTOS = "conversaciones";
    public static final String TAG_NOMBRE = "usuario";
    public static final String TAG_MENSAJE = "mensaje";
    public static final String TAG_ENVIO = "envio";
    public static final String TAG_RECEPTOR = "receptor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversaciones);
        this.setTitle("Conversaciones");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        lstConversaciones = (ListView) findViewById(R.id.lstConversaciones);
        registerForContextMenu(lstConversaciones);
        adaptador = new AdaptadorLista(this, contactos);
        lstConversaciones.setAdapter(adaptador);

        final Activity activityAmigos = this;

        lstConversaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(activityAmigos, ConversacionActivity.class);
                i.putExtra("id", ((Contacto) lstConversaciones.getItemAtPosition(position)).getID());
                i.putExtra("nombre", ((Contacto) lstConversaciones.getItemAtPosition(position)).getNombre());
                activityAmigos.startActivity(i);
            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab_amigos = (FloatingActionButton) findViewById(R.id.fab_amigos);
        FloatingActionButton fab_cerrarsesion = (FloatingActionButton) findViewById(R.id.fab_cerrarsesion);

        fab_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity host = (Activity) view.getContext();

                Intent i = new Intent(host, AmigosActivity.class);
                host.startActivity(i);
            }
        });

        fab_cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity host = (Activity) view.getContext();

                Intent i = new Intent(host, LoginActivity.class);
                host.startActivity(i);
            }
        });

        String sufURL = "listarconversaciones.php?usuario=" + Usuario.getUser();

        LeerConversacionesURL amigos = new LeerConversacionesURL();
        amigos.execute(URL_server + sufURL);

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual_conversaciones, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        contactoSeleccionado = info.position;
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verConversacion:
                Intent i = new Intent(this, ConversacionActivity.class);
                i.putExtra("id", ((Contacto) lstConversaciones.getItemAtPosition(contactoSeleccionado)).getID());
                i.putExtra("nombre", ((Contacto) lstConversaciones.getItemAtPosition(contactoSeleccionado)).getNombre());
                this.startActivity(i);

                break;
            case R.id.borrarConversacion:
                String sufURL = "borrarconversaciones.php?usuario1=" + ((Contacto) lstConversaciones.getItemAtPosition(contactoSeleccionado)).getID() +"&usuario2="+Usuario.getUser();

                BorrarConversacionesURL borrar = new BorrarConversacionesURL();
                borrar.execute(URL_server + sufURL);

                String suURL = "listarconversaciones.php?usuario=" + Usuario.getUser();

                LeerConversacionesURL amigos = new LeerConversacionesURL();
                amigos.execute(URL_server + suURL);

                ((AdaptadorLista)lstConversaciones.getAdapter()).notifyDataSetChanged();

        }
        return true;
    }

    class BorrarConversacionesURL extends AsyncTask<String, Void, String> {
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
        }
    }

    class LeerConversacionesURL extends AsyncTask<String, Void, String> {
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
                ((BaseAdapter) lstConversaciones.getAdapter()).notifyDataSetChanged();
            } else {
                Toast.makeText(ConversacionesActivity.this, "Ocurrió un error de Parsing", Toast.LENGTH_SHORT).show();
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
                    int foto = -1;

                    if (jsonObject.getInt(TAG_RECEPTOR) != Integer.parseInt(Usuario.getUser()))
                        foto = jsonObject.getInt(TAG_RECEPTOR);
                    else {
                        boolean exit = false;
                        for (int a = 0; a < contactos.size(); a++) {
                            if (contactos.get(a).getID() == jsonObject.getInt(TAG_RECEPTOR) && Integer.parseInt(Usuario.getUser()) == jsonObject.getInt(TAG_ENVIO)) {
                                exit = true;
                            }
                        }
                        if (!exit)
                            foto = jsonObject.getInt(TAG_ENVIO);
                    }

                    String mensaje = jsonObject.getString(TAG_MENSAJE);
                    if (mensaje.length() > 15)
                        mensaje = mensaje.substring(0, 15);

                    if (foto != -1) {
                        contacto = new Contacto(jsonObject.getString(TAG_NOMBRE), mensaje, foto, foto);
                        contactos.add(contacto);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return contactos;
        }
    }
}
