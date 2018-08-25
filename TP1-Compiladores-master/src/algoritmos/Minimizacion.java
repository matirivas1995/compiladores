package algoritmos;

import java.util.*;

import representacion.*;
//Clase que implementa el algoritmo de minimizacion de estados de un AFD

public class Minimizacion {

    //Registro para el algoritmo de minimizacion
    private static final Registro REGISTRO = new Registro();

    //Obtiene un AFD minimo a partir de un AFD
    public static AFDMin minimizarAFD(AFD afdOriginal) {

        REGISTRO.vaciar();
        REGISTRO.agregarLinea("Proceso de minimizacion"
                .toUpperCase()).nuevaLinea();
        REGISTRO.agregarLinea("_______________________")
                .nuevaLinea().nuevaLinea();

        //Se eliminan los estados inalcanzables
        AFD afdPostInalcanzables = new AFD();
        copiarAutomata(afdOriginal, afdPostInalcanzables);
        eliminarInalcanzables(afdPostInalcanzables);

        //Proceso de minimizacion
        AFD afdPostMinimizacion = minimizar(afdPostInalcanzables);

        // Se eliminan los estados identidades no finales */
        AFD afdPostIdentidades = new AFD();
        copiarAutomata(afdPostMinimizacion, afdPostIdentidades);
        eliminarIdentidades(afdPostIdentidades);

        return new AFDMin(afdOriginal, afdPostInalcanzables,
                afdPostMinimizacion, afdPostIdentidades);
    }

    //Metodo que elimina los estados inalcanzables de un AFD. 
    private static void eliminarInalcanzables(AFD afd) {

        REGISTRO.agregarLinea("Antes de eliminar inalcanzables:   "
                + afd.getEstados()).nuevaLinea();

        //Conjunto de estados alcanzados desde el estado inicial
        Conjunto<Estado> alcanzados = obtenerAlcanzados(afd);

        //Se eliminan los estados no alcanzados */
        afd.getEstados().retener(alcanzados);

        REGISTRO.agregarLinea("Despues de eliminar inalcanzables: "
                + afd.getEstados()).nuevaLinea();
        REGISTRO.nuevaLinea();
    }

    //Metodo que obtiene los estados alcanzados a partir del estado inicial
    private static Conjunto<Estado> obtenerAlcanzados(AFD afd) {
        //Estado inicial
        Estado actual = afd.getEstadoInicial();

        //Estados alcanzados
        Conjunto<Estado> alcanzados = new Conjunto();

        //Se agrega el estado actual
        alcanzados.agregar(actual);

        //Pila para almacenar los estados pendientes 
        Stack<Estado> pila = new Stack();

        //Se inserta el estado actual como el estado inicial
        pila.push(actual);

        while (!pila.isEmpty()) {
            actual = pila.pop();
            for (Transicion t : actual.getTransiciones()) {
                Estado e = t.getEstado();
                if (!alcanzados.contiene(e)) {
                    alcanzados.agregar(e);
                    pila.push(e);
                }
            }
        }
        return alcanzados;
    }

    //Implementacion del algoritmo de minización
    private static AFD minimizar(AFD afd) {
        HashMap<Estado, Conjunto<Integer>> tabla1;
        HashMap<Conjunto<Integer>, Conjunto<Estado>> tabla2;

        //Particiones del AFD
        Conjunto<Conjunto<Estado>> particion = new Conjunto<>();

        /*Paso 1: separar el AFD en dos grupos, los estados finales y
		  los estados no finales*/
        particion.agregar(afd.getEstadosFinales());
        particion.agregar(afd.getEstadosNoFinales());

        REGISTRO.agregarLinea("Particion: " + particion).nuevaLinea();

        //Paso 2: construir nuevas particiones
        Conjunto<Conjunto<Estado>> nuevaParticion;

        while (true) {
            //Nuevas particiones en cada pasada
            nuevaParticion = new Conjunto<>();

            for (Conjunto<Estado> grupo : particion) {
                switch (grupo.cantidad()) {
                    case 0:
                        //Los grupos vacios no se tienen en cuenta
                        continue;
                    case 1:
                        //Los grupos unitarios se agregan directamente
                        nuevaParticion.agregar(grupo);
                        break;
                    default:
                        /*Paso 2.1: encontrar los grupos alcanzados por
                                      cada estado del grupo actual*/

                        tabla1 = new HashMap<>();
                        for (Estado e : grupo) {
                            tabla1.put(e, getGruposAlcanzados(e, particion,
                                            afd.getAlfabeto()));
                        }
                        //Paso 2.2: calcular las nuevas particiones
                        tabla2 = new HashMap<>();
                        for (Estado e : grupo) {
                            Conjunto<Integer> alcanzados = tabla1.get(e);
                            if (tabla2.containsKey(alcanzados)) {
                                tabla2.get(alcanzados).agregar(e);
                            } else {
                                Conjunto<Estado> tmp = new Conjunto<>();
                                tmp.agregar(e);
                                tabla2.put(alcanzados, tmp);
                            }
                        }
                        /*Paso 2.3: copiar las nuevas particiones al
                                    * conjunto de nuevas particiones*/
                        for (Conjunto<Estado> c : tabla2.values()) {
                            nuevaParticion.agregar(c);
                        }
                        break;
                }
            }
            //Se ordena la neuva particion
            nuevaParticion.ordenar();

            REGISTRO.agregarLinea("Particion: " + nuevaParticion).nuevaLinea();

            /*Paso 2.4: si las particiones son iguales, significa que
			  no hubo cambios y debemos terminar*/
            if (nuevaParticion.equals(particion)) {
                break;
            } else {
                particion = nuevaParticion;
            }
        }

        REGISTRO.nuevaLinea();

        //Paso 3: crear el nuevo AFD, con los nuevos estados producidos.
        AFD afdPostMinimizacion = new AFD(afd.getAlfabeto(), afd.getExprReg());

        //Paso 3.1: agregar los estados al nuevo AFD.
        for (int i = 0; i < particion.cantidad(); i++) {
            Conjunto<Estado> grupo = particion.obtener(i);
            boolean esFinal = false;

            /*El grupo actual tiene un estado final, el estado correspondiente
			  en el nuevo AFD tambien debe ser final*/
            if (tieneEstadoFinal(grupo)) {
                esFinal = true;
            }

            /*Si el estado es resultado de la union de dos o mas estados, su
	      etiqueta sera del tipo e1.e2.e3, donde e1, e2 y e3 son los
              estados agrupados (aparecen separados por un punto)*/
            String etiqueta = obtenerEtiqueta(grupo);

            //Se agrega el estado al nuevo AFD
            Estado estado = new Estado(i, esFinal);
            estado.setEtiqueta(etiqueta);
            afdPostMinimizacion.agregarEstado(estado);
        }

        //Paso 3.2:  mapear estados del nuevo AFD a estados del AFD original
        HashMap<Estado, Estado> mapeo = new HashMap<Estado, Estado>();
        for (int i = 0; i < particion.cantidad(); i++) {
            //Grupo a procesar
            Conjunto<Estado> grupo = particion.obtener(i);

            //Estado del nuevo AFD
            Estado valor = afdPostMinimizacion.getEstado(i);

            //Guardar mapeo
            for (Estado clave : grupo) {
                mapeo.put(clave, valor);
            }
        }

        //Paso 3.3: agregar las transiciones al nuevo AFD
        for (int i = 0; i < particion.cantidad(); i++) {
            Estado representante = particion.obtener(i).obtenerPrimero();
            Estado origen = afdPostMinimizacion.getEstado(i);
            for (Transicion trans : representante.getTransiciones()) {
                Estado destino = mapeo.get(trans.getEstado());
                origen.getTransiciones().agregar(
                        new Transicion(destino, trans.getSimbolo()));
            }
        }
        return afdPostMinimizacion;
    }

    /*Para un estado dado, busca los grupos en los que caen las transiciones
	  del mismo*/
    private static Conjunto<Integer> getGruposAlcanzados(Estado origen,
            Conjunto<Conjunto<Estado>> particion, Alfabeto alfabeto) {
        //Grupos alcanzados por el estado
        Conjunto<Integer> gruposAlcanzados = new Conjunto();
        HashMap<String, Estado> transiciones = origen
                .getTransicionesSegunAlfabeto(alfabeto);

        for (String s : alfabeto) {
            Estado destino = transiciones.get(s);
            if (destino == null) {
                gruposAlcanzados.agregar(-1);
            } else {
                for (int pos = 0; pos < particion.cantidad(); pos++) {
                    Conjunto<Estado> grupo = particion.obtener(pos);

                    if (grupo.contiene(destino)) {
                        gruposAlcanzados.agregar(pos);
                        break;
                    }
                }
            }
        }
        return gruposAlcanzados;
    }

    /*Elimina los estados identidad, aquellos que para todos los simbolos del
      alfabeto tienen transiciones a si mismos, solo se eliminan si no son estados
      de aceptacion*/
    private static void eliminarIdentidades(AFD afd) {
        REGISTRO.agregarLinea("Antes de eliminar identidades:   "
                + afd.getEstados()).nuevaLinea();

        //Conjunto de estados a eliminar
        Conjunto<Estado> estadosEliminados = new Conjunto<Estado>();

        for (Estado e : afd.getEstados()) {
            if (e.getEsIdentidad() && !e.getEsFinal()) {
                estadosEliminados.agregar(e);
            }
        }

        if (estadosEliminados.estaVacio()) {
            REGISTRO.agregarLinea("Despues de eliminar identidades: "
                    + afd.getEstados()).nuevaLinea();
            REGISTRO.nuevaLinea();
            return;
        }

        //Eliminacion de los estados identidad no finales
        for (Estado e : estadosEliminados) {
            afd.getEstados().eliminar(e);
        }

        //Transiciones a eliminar
        ArrayList<List> transEliminadas = new ArrayList();

        for (Estado e : afd.getEstados()) {
            for (Transicion t : e.getTransiciones()) {
                if (estadosEliminados.contiene(t.getEstado())) {
                    transEliminadas.add(Arrays.asList(t, e.getTransiciones()));
                }
            }
        }

        transEliminadas.forEach((a) -> {
            Transicion t = (Transicion) a.get(0);
            Conjunto<Transicion> c = (Conjunto<Transicion>) a.get(1);
            c.eliminar(t);
        });

        REGISTRO.agregarLinea("Despues de eliminar identidades: "
                + afd.getEstados()).nuevaLinea();
        REGISTRO.nuevaLinea();
    }

    //Metodo que copia un automata origen a otro de destino.
    private static void copiarAutomata(Automata origen, Automata destino) {
        Automata.copiarEstados(origen, destino, 0);
        for (int i = 0; i < origen.cantidadEstados(); i++) {
            Estado tmp = origen.getEstado(i);

            destino.getEstado(i).setEsFinal(tmp.getEsFinal());
            destino.getEstado(i).setEtiqueta(tmp.getEtiqueta());
        }
        destino.setAlfabeto(origen.getAlfabeto());
        destino.setExprReg(origen.getExprReg());
    }

    //Metodo que determina si un grupo de estados tiene un estado final.
    private static boolean tieneEstadoFinal(Conjunto<Estado> grupo) {
        for (Estado e : grupo) {
            if (e.getEsFinal()) {
                return true;
            }
        }
        return false;
    }

    /*Calcula la nueva etiqueta para un estado del nuevo AFD, segun los estados
      agrupados*/
    private static String obtenerEtiqueta(Conjunto<Estado> grupo) {
        String etiqueta = "";
        String pedazo;

        for (Estado e : grupo) {
            if (e.toString().endsWith("if")) {
                pedazo = e.toString().substring(0, e.toString().length() - 2);
            } else if (e.toString().endsWith("i") || e.toString().endsWith("f")) {
                pedazo = e.toString().substring(0, e.toString().length() - 1);
            } else {
                pedazo = e.toString();
            }

            etiqueta += pedazo + " ";
        }

        if (etiqueta.endsWith(" ")) {
            etiqueta = etiqueta.substring(0, etiqueta.length() - 1);
        }

        return "(" + etiqueta + ")";
    }

    public static Registro getRegistro() {
        return REGISTRO;
    }
}
