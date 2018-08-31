package convertidor;

import representacion.*;
import validaciones.*;
import algoritmos.*;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;

public class Main {

    public static void main(String args[]) throws Exception {
        ArrayList listAfn;
        ArrayList listAfd;
        ArrayList listExpReg;
        ArrayList listaSimbolos;

        listAfn = new ArrayList<AFN>();
        listAfd = new ArrayList<AFD>();
        listaSimbolos = new ArrayList<TablaDeSimbolos>();
        listExpReg = new ArrayList<String>();
        Scanner scan = new Scanner(System.in);
        String entrada=null;
        String cantidad=null;


        System.out.println("Ingrese la cantidad de expresiones:");
        cantidad = scan.nextLine();


        for (int i=0; i<Integer.valueOf(cantidad);i++){

            System.out.println("Ingrese el alfabeto:");
            entrada = scan.nextLine();

            Alfabeto alfabeto = new Alfabeto(entrada);
            System.out.println("Ingrese la expresion regular:");
            String expReg = scan.nextLine();
            Constructor constructor = new Constructor(alfabeto, expReg);

            //Conversion Expresion Regular a AFN (Thompson)
            AFN afn = constructor.construirAFN();
            System.out.println("\nAFN\n" + afn);

            //Impresion tabla de transicion del AFN
            System.out.println();
            TablaDeTransicion tabla = afn.getTablaTransicion();

            tabla.imprimirTablaAFN();

            //Conversion AFN a AFD (Subconjuntos)
            System.out.println();
            AFD afd = Subconjuntos.construirAFD(afn);
            System.out.println(Subconjuntos.getRegistro());
            System.out.println("\nD-Estados AFD:\n" + afd.estadosDtoString());
            System.out.println("\nAFD:\n" + afd);

            //Impresion tabla de transicion del AFD
            TablaDeTransicion tabla2 = afd.getTablaTransicion();
            tabla2.imprimirTablaAFD();

            //Conversion AFD a AFD Minimizado (Minimizacion)
            AFDMin afdMin = Minimizacion.minimizarAFD(afd);
            System.out.println(Minimizacion.getRegistro());
            System.out.println("AFD Minimizado\n");
            System.out.println(afdMin.getDespIdentidades());

            listAfn.add(afn);
            listAfd.add(afd);
            listExpReg.add(expReg);
        }

        String valor = null;
        while (!Objects.equals(valor, "exit")){
            // Verificion de cadena con Automatas
            System.out.println("Ingrese cadena a validar:");
            valor = scan.nextLine();

            String[] parts = valor.split(" ");

            ResultadoValidacion resultado = null;
            String expre = null;
            int contador = 0;

            for (String part: parts) {

                for (int i = 0; i < listAfd.size(); i++) {

                    // Verficiacion con el AFN
                    resultado = Validacion.validarAFN((AFN) listAfn.get(i), part);

                    Validacion.imprimirValidacion(resultado, "AFN");

                    // Verficiacion con el AFD
                    resultado = Validacion.validarAFD((AFD) listAfd.get(i), part);
                    Validacion.imprimirValidacion(resultado, "AFD");

                    if (resultado.esValido()) {
                        System.out.println("La cadena es aceptada y pertenece a la expresión regular : " + listExpReg.get(i));
                        TablaDeSimbolos simbolo = new TablaDeSimbolos((String)listExpReg.get(i),part,Integer.toString(contador));
                        System.out.println("Se agrega --> " + simbolo.getCadena() + "  con id ---> " + simbolo.getIdentificador());
                        listaSimbolos.add(simbolo);
                        contador++;
                        break;
                    }

                }
            }

            Iterator it = listaSimbolos.iterator();

            System.out.println("\nLa tabla de simbolos es la siguiente\n\n");
            String format = "|%1$-10s|%2$-10s|%3$-20s|\n";
            System.out.format(format,"Token","Cadena","Identificador");
            while(it.hasNext()) {
                TablaDeSimbolos sim = (TablaDeSimbolos) it.next();
                System.out.format(format,sim.getToken(),sim.getCadena(),sim.getIdentificador());
            }
            System.out.println("\n\nEnter para continuar. 'exit' para salir.");
            valor = scan.nextLine();
        }


    }
}


