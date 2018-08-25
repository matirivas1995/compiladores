package representacion;

import java.util.*;

//Clase que representa al alfabeto
public class Alfabeto implements Iterable<String> {

    // Representa el simbolo vacio
    public static final String VACIO = "ε";

    // Conjunto de simbolos del alfabeto
    private final ArrayList<String> simbolos;

    //Contructor de la clase.
    public Alfabeto(String caracteres) {
        String[] arregloTemp = caracteres.split("(?!^)");
        Arrays.sort(arregloTemp);

        //Se eliminan duplicados
        simbolos = new ArrayList(arregloTemp.length);
        for (String temp : arregloTemp) {
            if (!simbolos.contains(temp)) {
                simbolos.add(temp);
            }
        }
    }

    //Retorna la cantidad de simbolos que contiene.
    public int getCantidad() {
        return simbolos.size();
    }

    //Retorna un determinado simbolo del alfabeto.
    public String getSimbolo(int pos) {
        if (pos == getCantidad()) {
            return Alfabeto.VACIO;
        } else {
            return simbolos.get(pos);
        }
    }

    //Permite conocer si un caracter dado pertenece al alfabeto
    public boolean contiene(String caracter) {
        return simbolos.contains(caracter);
    }

    public int obtenerPosicion(String caracter) {
        if (caracter.equals(Alfabeto.VACIO)) {
            return getCantidad();
        } else {
            return simbolos.indexOf(caracter);
        }
    }

    @Override
    public String toString() {
        String salida = "{";

        for (int i = 0; i < this.getCantidad(); i++) {
            salida += simbolos.get(i);

            if (i < this.getCantidad() - 1) {
                salida += ", ";
            }
        }

        return salida;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Alfabeto other = (Alfabeto) obj;

        // Si los tamaños son distintos, no pueden ser iguales.
        if (other.getCantidad() != this.getCantidad()) {
            return false;
        }

        // Verificamos cada uno de los simbolos
        for (int i = 0; i < this.getCantidad(); i++) {
            String tmp1 = this.getSimbolo(i);
            String tmp2 = other.getSimbolo(i);

            if (!tmp1.equals(tmp2)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iterator<String> iterator() {
        return simbolos.iterator();
    }
}
