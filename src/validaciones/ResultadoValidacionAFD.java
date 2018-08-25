package validaciones;

import representacion.*;


/*Clase que representa los datos obtenidos como resultado de un proceso de 
  validacion de una cadena de entrada contra un AFD*/
public class ResultadoValidacionAFD extends ResultadoValidacion {

    /* Camino producido */
    private final Conjunto<Par<Estado, String>> camino;

    public ResultadoValidacionAFD(AFD automata, String entrada,
            Conjunto<Par<Estado, String>> camino, String entradaFaltante) {

        this.automata = automata;
        this.entrada = entrada;
        this.camino = camino;
        this.entradaFaltante = (entradaFaltante == null) ? "" : entradaFaltante;
    }

    @Override
    public Conjunto<Par<Estado, String>> getCamino() {
        return camino;
    }

    @Override
    public boolean esValido() {
        if (!entradaFaltante.equals("")) {
            return false;
        }

        return camino.obtenerUltimo().getPrimero().getEsFinal();
    }
}

