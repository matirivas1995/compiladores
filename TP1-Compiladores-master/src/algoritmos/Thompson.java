package algoritmos;

import representacion.Transicion;
import representacion.Estado;
import representacion.AFN;
import representacion.Automata;
import representacion.*;

/*Esta clase implementa los algoritmos de Thompson para cada  para cada
uno de los operadores*/
public class Thompson {

    //Construye un AFN a partir de un simbolo
    public static AFN basico(String simbolo) {
        AFN afn = new AFN();

        //Los estados iniciales y finales
        Estado ini = new Estado(0);
        Estado fin = new Estado(1, true);

        //Transicion del estado inicial al final
        Transicion tran = new Transicion(fin, simbolo);
        ini.getTransiciones().agregar(tran);

        //Se agregan los estados al AFN
        afn.agregarEstado(ini);
        afn.agregarEstado(fin);

        return afn;
    }

    //Aplica el operador de union a dos AFNs dados.
    public static AFN union(AFN afn1, AFN afn2) {
        Transicion trans;
        AFN afn = new AFN();

        //Se agrega nuevo estado inicial
        Estado nuevoInicio = new Estado(afn.cantidadEstados());
        afn.agregarEstado(nuevoInicio);

        //Se agregan los estados de afn1
        Automata.copiarEstados(afn1, afn, afn.cantidadEstados());

        //Se agregan los estados de afn2
        Automata.copiarEstados(afn2, afn, afn.cantidadEstados());

        //Se agrega el nuevo estado final
        Estado nuevoFin = new Estado(afn.cantidadEstados(), true);
        afn.agregarEstado(nuevoFin);

        /*Se agrega la transicion vacia desde el estado inicial
          al estado inicial de afn1*/
        trans = new Transicion();
        trans.setEstado(afn.getEstado(1));
        trans.setSimbolo(Alfabeto.VACIO);
        nuevoInicio.getTransiciones().agregar(trans);

        /*Se agrega la transicion vacia desde el estado inicial
          al estado inicial de afn2*/
        trans = new Transicion();
        trans.setEstado(afn.getEstado(afn1.cantidadEstados() + 1));
        trans.setSimbolo(Alfabeto.VACIO);
        nuevoInicio.getTransiciones().agregar(trans);

        /*Se agrega la transicion vacia desde el estado final
          de afn1 hasta el estado final*/
        trans = new Transicion();
        trans.setEstado(afn.getEstado(afn.cantidadEstados() - 1));
        trans.setSimbolo(Alfabeto.VACIO);
        afn.getEstado(afn1.cantidadEstados()).getTransiciones().agregar(trans);

        /*Se agrega la transicion vacia desde el estado final
          de afn2 hasta el estado final*/
        trans = new Transicion();
        trans.setEstado(afn.getEstado(afn.cantidadEstados() - 1));
        trans.setSimbolo(Alfabeto.VACIO);
        afn.getEstado(afn.cantidadEstados() - 2).getTransiciones().agregar(trans);

        return afn;
    }

    //Aplica el operador de concatenacion a dos AFNs dados.
    public static AFN concatenacion(AFN afn1, AFN afn2) {
        AFN afn = new AFN();

        //Se agregan los estados de afn1
        Automata.copiarEstados(afn1, afn, afn.cantidadEstados());

        //Estado final de afn2
        Estado finAfn1 = afn.getEstado(afn.cantidadEstados() - 1);

        //Se agregan los estados de afn2, excepto el primero
        Automata.copiarEstados(afn2, afn, afn.cantidadEstados() - 1, 1);

        //Estado inicial de afn2
        Estado inicioAfn2 = afn2.getEstadoInicial();

        /*Se agregan las transiciones del estado inicial afn2 al 
        estado final de afn1*/
        Automata.copiarTransiciones(afn, inicioAfn2.getTransiciones(),
                finAfn1, finAfn1.getIdentificador());

        //Se establece el estado final
        afn.getEstado(afn.cantidadEstados() - 1).setEsFinal(true);

        return afn;
    }

    //Aplica el operador de opcion (?) a un AFN dado.
    public static AFN opcion(AFN afn) {
        return union(afn, basico(Alfabeto.VACIO));
    }

    //Aplica la cerradura de Kleene (*) a un AFN dado.
    public static AFN cerraduraKleene(AFN afn) {
        AFN afnResultante = new AFN();

        //Se agrega el nuevo estado inicial
        Estado nuevoInicio = new Estado(afnResultante.cantidadEstados());
        afnResultante.agregarEstado(nuevoInicio);

        //Se agregan los demas estados
        Automata.copiarEstados(afn, afnResultante, afnResultante.cantidadEstados());

        //Se agrega el estado final
        Estado nuevoFin = new Estado(afnResultante.cantidadEstados(), true);
        afnResultante.agregarEstado(nuevoFin);

        //Se agregan las transiciones desde el nuevo estado inicial
        nuevoInicio.getTransiciones().agregar(
                new Transicion(afnResultante.getEstado(1), Alfabeto.VACIO));
        nuevoInicio.getTransiciones().agregar(
                new Transicion(nuevoFin, Alfabeto.VACIO));

        //Anterior estado final
        Estado antesDeFinal = afnResultante.getEstado(afnResultante.cantidadEstados() - 2);

        //Se agrega las transiciones desde el anterior estado final
        antesDeFinal.getTransiciones().agregar(
                new Transicion(afnResultante.getEstado(1), Alfabeto.VACIO));
        antesDeFinal.getTransiciones().agregar(
                new Transicion(nuevoFin, Alfabeto.VACIO));

        return afnResultante;
    }

    // Aplica la cerradura positiva (+) a un AFN dado.
    public static AFN cerraduraPositiva(AFN afn) {
        return concatenacion(afn, cerraduraKleene(afn));
    }
}
