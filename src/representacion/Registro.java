package representacion;

import java.util.*;

public class Registro {

    //Lista de cadenas agregadas al registro
    private final ArrayList<String> cadenas;

    //Constructor de un registro nuevo
    public Registro() {
        cadenas = new ArrayList();
    }

    //Agrega una nueva linea
    public Registro agregarLinea(String linea) {
        cadenas.add(linea);
        return this;
    }

    //Agrega un caracter de fin de linea.
    public Registro nuevaLinea() {
        cadenas.add("\n");
        return this;
    }

    //Vacia las cadenas del registro
    public Registro vaciar() {
        cadenas.clear();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        cadenas.forEach((s) -> {
            str.append(s);
        });

        return str.toString();
    }
}
