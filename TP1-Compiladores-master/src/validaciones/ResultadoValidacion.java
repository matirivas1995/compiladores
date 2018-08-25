package validaciones;

import representacion.*;

/*Clase que representa los datos obtenidos como resultado de un proceso de
  validacion de una cadena de entrada contra un Automata*/
public abstract class ResultadoValidacion {

    //Automata asociado a este resultado de validacion
    protected Automata automata;

    //Cadena de entrada asociada a este resultado de validacion
    protected String entrada;

    //Simbolos de entrada que no pudieron ser consumidos
    protected String entradaFaltante;

    public Automata getAutomata() {
        return automata;
    }

    public String getEntrada() {
        return entrada;
    }

    //Camiono de estados que resulta de validar la cadena de entrada
    public abstract Conjunto getCamino();

    //Obtiene los simbolos de entrada que no pudieron ser consumidos.
    public String getEntradaFaltante() {
        return entradaFaltante;
    }

    //Determina si el resultado es valido o no
    public abstract boolean esValido();
}
