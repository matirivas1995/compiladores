package validaciones;

import java.util.*;
import representacion.*;
import algoritmos.*;

//Clase que implementa la validacion para AFNs y AFDs.
public class Validacion {

    //Valida una cadena de entrada contra un AFD dado.
    public static ResultadoValidacion validarAFN(AFN afn, String entrada) {
        //Buffer para manipular la entrada
        Queue<String> buffer = new LinkedList<>();

        for (int i = 0; i < entrada.length(); i++) {
            buffer.add("" + entrada.charAt(i));
        }

        Conjunto<Par<Conjunto<Estado>, String>> camino = new Conjunto<>();
        Conjunto<Estado> estadosActuales = Subconjuntos
                .cerraduraEpsilon(afn.getEstadoInicial());
        camino.agregar(new Par<>(estadosActuales, ""));

        String simbolo;

        //Se recorre mientras hayan simbolos en la entrada
        while ((simbolo = buffer.poll()) != null) {
            estadosActuales = Subconjuntos
                    .cerraduraEpsilon(Subconjuntos.mover(estadosActuales, simbolo));

            if (estadosActuales.estaVacio()) {
                //Si no se alcanza ningún estado, terminama
                break;
            } else {
                //Se agrega el estado nuevo al camino
                camino.agregar(new Par<>(estadosActuales, simbolo));
            }
        }

        //Comprobacion de la condicion que provoco la finalizacion del while
        if (estadosActuales.estaVacio()) {

            //Se llego a un estado desde el cual no se pudo avanzar
            String entradaFaltante = simbolo;
            for (String s : buffer) {
                entradaFaltante += s;
            }

            return new ResultadoValidacionAFN(afn, entrada, camino, entradaFaltante);
        } else {
            return new ResultadoValidacionAFN(afn, entrada, camino, "");
        }
    }

    //Valida una cadena de entrada contra un AFD dado.
    public static ResultadoValidacion validarAFD(AFD afd, String entrada) {
        //Buffer para manipular la entrada
        Queue<String> buffer = new LinkedList<>();

        for (int i = 0; i < entrada.length(); i++) {
            buffer.add("" + entrada.charAt(i));
        }

        //Secuencia de estados recorridos
        Conjunto<Par<Estado, String>> camino = new Conjunto<>();

        //Se comienza por el estado inicial
        Estado estadoActual = afd.getEstadoInicial();
        camino.agregar(new Par<>(estadoActual, ""));

        String simbolo;

        //Se recorre mientras hayan simbolos en la entrada
        while ((simbolo = buffer.poll()) != null) {
            estadoActual = mover(estadoActual, simbolo);

            if (estadoActual == null) {
                //Si no se alcanza ningún estado, termina
                break;
            } else {
                //Se agrega el estado nuevo al camino
                camino.agregar(new Par<>(estadoActual, simbolo));
            }
        }

        //Comprobacion de la condicion que provoco la finalizacion del while
        if (estadoActual == null) {
            //Se llego a un estado desde el cual no se pudo avanzar
            String entradaFaltante = simbolo;
            for (String s : buffer) {
                entradaFaltante += s;
            }

            return new ResultadoValidacionAFD(afd, entrada, camino, entradaFaltante);
        } else {
            return new ResultadoValidacionAFD(afd, entrada, camino, "");
        }
    }

    //Metodo que obtiene el estado destino de una transicion por un simbolo dado.
    private static Estado mover(Estado origen, String simbolo) {
        for (Transicion t : origen.getTransiciones()) {
            if (t.getSimbolo().equals(simbolo)) {
                return t.getEstado();
            }
        }
        return null;
    }
    
    public static void imprimirValidacion(ResultadoValidacion resultado, String automata){
        String mensaje;
        if (!resultado.esValido()) {
            mensaje = "Cadena rechazada.\n\n";
            String Resto = resultado.getEntradaFaltante();
            if (!Resto.isEmpty()) {
                mensaje += "Se recorrio el " + resultado.getCamino() + " y no se pudo continuar" +
                        " Falta: \"" + Resto + "\".";
            } else {
                mensaje += "Se recorrio " + resultado.getCamino() + 
                        " y no se llego a un estado final. \n";
            }
        } else {
            mensaje = "Cadena aceptada.\nSe recorrio " + resultado.getCamino();
        }
        System.out.println("\nResultado del " + automata);
        System.out.println(mensaje);
    }
}
