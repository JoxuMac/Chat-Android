/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/

package ipo2.actividad3gutierrez_negro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static ipo2.actividad3gutierrez_negro.Constantes.URL_server;

public class Identificarse_BBDD extends AsyncTask<String, Void, String> {

    private ProgressDialog dialogoCargando;

    Activity LoginActivity;

    public Identificarse_BBDD(Activity Login) {
        LoginActivity = Login;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoCargando = ProgressDialog.show(LoginActivity, "Comprobando credenciales ...", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        String sufijo = params[0];
        String cadenaDevueltaPHP;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(URL_server + sufijo);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            cadenaDevueltaPHP = bufferedReader.readLine();

            if (!cadenaDevueltaPHP.equals("0")) {
                Intent i = new Intent(LoginActivity, ConversacionesActivity.class);
                LoginActivity.startActivity(i);
                Usuario.setUser(cadenaDevueltaPHP);
                return "Identificado Correctamente";
            } else
                return "Usuario/Contraseña erroneo";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String sufijo) {
        super.onPostExecute(sufijo);

        dialogoCargando.dismiss();

        Toast.makeText(LoginActivity, sufijo, Toast.LENGTH_LONG).show();
    }
}