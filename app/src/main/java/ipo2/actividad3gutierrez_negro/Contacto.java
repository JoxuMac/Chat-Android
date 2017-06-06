/**
 * Nombre: Actividad 3 - CHAT
 * Autores: Josue Gutierrez & Miguel Negro
 * Asignatura: Interaccion Persona - Ordenador II
 **/
package ipo2.actividad3gutierrez_negro;

public class Contacto {
    private String nombre;
    private String telefono;
    private int tipo;
    private int id;

    public Contacto(String nom, String tel, int tip, int i) {
        nombre = nom;
        telefono = tel;
        tipo = tip;
        id = i;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getTipo() {
        return tipo;
    }

    public int getID() {
        return id;
    }
}
