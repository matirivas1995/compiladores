package validaciones;

import representacion.*;

/*Clase que representa los datos obtenidos como resultado de un proceso de 
  validacion de una cadena de entrada contra un AFN*/
public class ResultadoValidacionAFN extends ResultadoValidacion {

    private final Conjunto<Par<Conjunto<Estado>, String>> camino;

    public ResultadoValidacionAFN(AFN automata, String entrada,
            Conjunto<Par<Conjunto<Estado>, String>> camino, String entradaFaltante) {

        this.automata = automata;
        this.entrada = entrada;
        this.camino = camino;
        this.entradaFaltante = (entradaFaltante == null) ? "" : entradaFaltante;
    }

    @Override
    public Conjunto<Par<Conjunto<Estado>, String>> getCamino() {
        return camino;
    }

    @Override
    public boolean esValido() {
        if (!entradaFaltante.equals("")) {
            return false;
        }

        for (Estado e : camino.obtenerUltimo().getPrimero()) {
            if (e.getEsFinal()) {
                return true;
            }
        }

        return false;
    }
}
