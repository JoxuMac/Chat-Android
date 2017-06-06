/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/

package ipo2.actividad3gutierrez_negro;

class Mensajes {

    private String mensaje;
    private int envio;
    private int receptor;
    private int foto;

    public Mensajes(String msg, int env, int recep, int fot) {
        mensaje = msg;
        envio = env;
        receptor = recep;
        foto = fot;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getEnvio(){
        return envio;
    }
}
