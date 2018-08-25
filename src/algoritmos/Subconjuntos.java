package algoritmos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import representacion.*;

//Clase  que implementa el algoritmo de subconjuntos
public class Subconjuntos {

    //Registro para el algoritmo de subconjuntos
    private static final Registro REGISTRO = new Registro();

    public static AFD construirAFD(Automata afn) {
        Estado estadoOrigen, estadoDestino;

        REGISTRO.vaciar();
        REGISTRO.agregarLinea("Pasos para hallar el conjunto de estados"
                .toUpperCase()).nuevaLinea();
        REGISTRO.agregarLinea("________________________________________")
                .nuevaLinea().nuevaLinea();

        //AFD resultante
        AFD afd = new AFD(afn.getAlfabeto(), afn.getExprReg());

        //Conjunto de estados finales del AFD
        Conjunto<Conjunto<Estado>> estadosD = new Conjunto<>();

        //Cola utilizada para guardar temporalmente los conjuntos de estados
        Queue<Conjunto<Estado>> colaTemp = new LinkedList<>();

        //Contador de estados procesados del AFD
        int estadosProcesados = 0;

        //Calculo de la cerradura epsilon del estado inicial
        Conjunto<Estado> resultado = cerraduraEpsilon(afn.getEstadoInicial());

        REGISTRO.agregarLinea("cerradura(" + afn.getEstadoInicial() + ") = "
                + resultado).nuevaLinea().nuevaLinea();

        // Se agrega la cerradura epsilon del estado inicial a estadosD sin marcar
        estadosD.agregar(resultado);
        colaTemp.add(resultado);

        //Inicio del ciclo principal del algoritmo
        while (!colaTemp.isEmpty()) {
            //Marcar T
            Conjunto<Estado> T = colaTemp.remove();

            //Se agrega el estado correspondiente al AFD
            if (afd.cantidadEstados() < estadosD.cantidad()) {
                afd.agregarEstado(new Estado(afd.cantidadEstados()));
            }

            //Estado del AFD a procesar
            estadoOrigen = afd.getEstado(estadosProcesados++);

            //Se buscan transiciones por cada simbolo
            for (String simbolo : afn.getAlfabeto()) {

                //Se aplica cerraduraEpsilon(mueve(T, simbolo))
                Conjunto<Estado> M = mover(T, simbolo);
                Conjunto<Estado> U = cerraduraEpsilon(M);

                REGISTRO.agregarLinea("cerradura(mover(" + T + ", " + simbolo
                        + ")) = ")
                        .agregarLinea("cerradura(" + M + ") = " + U)
                        .nuevaLinea();

                if (estadosD.contiene(U)) {
                    int posicion = estadosD.obtenerPosicion(U);
                    estadoDestino = afd.getEstado(posicion);
                } else if (!U.estaVacio()) {
                    estadoDestino = new Estado(afd.cantidadEstados());
                    afd.agregarEstado(estadoDestino);

                    //Se agrega U a estadosD si no esta aún
                    estadosD.agregar(U);
                    colaTemp.add(U);
                } else {
                    //Conjunto vacio encontrado
                    continue;
                }

                // Agregamos la transicion al AFD
                Transicion trans = new Transicion(estadoDestino, simbolo);
                estadoOrigen.getTransiciones().agregar(trans);
            }

            REGISTRO.nuevaLinea();
        }

        // Se establecen los estados finales del AFD
        for (int i = 0; i < estadosD.cantidad(); i++) {
            Estado estadoAFD = afd.getEstado(i);

            for (Estado e : estadosD.obtener(i)) {
                if (e.getEsFinal()) {
                    estadoAFD.setEsFinal(true);
                    break;
                }
            }
        }

        afd.setdEstados(estadosD);
        return afd;
    }

    //Metodo que implementa la operacion cerradura epsilon sobre un Estado.
    public static Conjunto<Estado> cerraduraEpsilon(Estado estado) {
        Conjunto<Estado> resultado = new Conjunto();
        recorrido(estado, resultado, Alfabeto.VACIO);
        resultado.ordenar();
        return resultado;
    }

    //Metodo que implementa la operacion Cerradura Epsilon sobre un Conjunto
    public static Conjunto<Estado> cerraduraEpsilon(Conjunto<Estado> estados) {
        Conjunto<Estado> resultado = new Conjunto();
        recorrido(estados, resultado, Alfabeto.VACIO);
        resultado.ordenar();
        return resultado;
    }

    //Metodo que implementa la operacion Mueve.
    public static Conjunto<Estado> mover(Conjunto<Estado> estados,
            String simbolo) {
        Conjunto<Estado> resultado = new Conjunto();
        recorrido(estados, resultado, simbolo);
        resultado.ordenar();
        return resultado;
    }

    /*Se recorre el automata a partir de un estado inicial y pasa por todas 
       las transiciones que coincidan con el simbolo determinado*/
    private static void recorrido(Estado actual, Conjunto<Estado> alcanzados,
            String simboloBuscado) {

        /* Pila para almacenar los estados pendientes */
        Stack<Estado> pila = new Stack();

        //Si el simbolo buscado es igual a vacio, se incluye el estado original
        if (simboloBuscado.equals(Alfabeto.VACIO)) {
            alcanzados.agregar(actual);
        }

        //Se inserta el estado actual como estado inicial
        pila.push(actual);

        while (!pila.isEmpty()) {
            actual = pila.pop();
            for (Transicion t : actual.getTransiciones()) {
                Estado e = t.getEstado();
                String s = t.getSimbolo();

                if (s.equals(simboloBuscado) && !alcanzados.contiene(e)) {
                    alcanzados.agregar(e);

                    if (simboloBuscado.equals(Alfabeto.VACIO)) {
                        pila.push(e);
                    }
                }
            }
        }
    }

    /*Se recorre el automata a partir de un estado inicial y pasa por todas 
       las transiciones que coincidan con el simbolo determinado*/
    private static void recorrido(Conjunto<Estado> inicios, Conjunto<Estado> alcanzados, String simboloBuscado) {
        for (Estado e : inicios) {
            recorrido(e, alcanzados, simboloBuscado);
        }
    }

    public static Registro getRegistro() {
        return REGISTRO;
    }
}
