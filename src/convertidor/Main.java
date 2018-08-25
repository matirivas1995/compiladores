package convertidor;

import representacion.*;
import validaciones.*;
import algoritmos.*;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws Exception {

        Scanner scan = new Scanner(System.in);
        System.out.println("Ingrese el alfabeto:");
        String entrada = scan.nextLine();
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

        // Verificion de cadena con Automatas
        System.out.println("Ingrese cadena a validar:");
        String valor = scan.nextLine();
        ResultadoValidacion resultado = null;

        // Verficiacion con el AFN
        resultado = Validacion.validarAFN((AFN) afn, valor);

        Validacion.imprimirValidacion(resultado, "AFN");

        // Verficiacion con el AFD
        resultado = Validacion.validarAFD((AFD) afd, valor);
        Validacion.imprimirValidacion(resultado, "AFD");
    }
}



