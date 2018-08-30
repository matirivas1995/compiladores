package convertidor;

import representacion.*;
import validaciones.*;
import algoritmos.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
            TablaDeSimbolos simbolo = null;
            TablaDeSimbolos imprimible = null;

            for (String part: parts){

                for (int i=0;i<listAfd.size();i++) {

                    // Verficiacion con el AFN
                    resultado = Validacion.validarAFN((AFN) listAfn.get(i), part);

                    Validacion.imprimirValidacion(resultado, "AFN");

                if (resultado.esValido()){
                    System.out.println("La cadena es aceptada y pertenece a la expresión regular : " + listExpReg.get(i));
                    simbolo.setToken(listExpReg.get(i).toString());
                    simbolo.setCadena(valor);
                    simbolo.setIdentificador(Integer.toString(i));
                    listaSimbolos.add(simbolo);
                    break;
                }

            }
            System.out.println("La tabla de simbolos es la siguiente\n\n");
            System.out.println("Token\tCadena\tIdentificador\n");
            for(int j=0 ; j< listaSimbolos.size() ; j++){
                imprimible = (TablaDeSimbolos) listaSimbolos.get(j);
                System.out.println(imprimible.getToken()+"\t"+imprimible.getCadena()+"\t"+imprimible.getIdentificador());
            }
            System.out.println("\n\nEnter para continuar. 'exit' para salir.");
            valor = scan.nextLine();
        }


    }
}


