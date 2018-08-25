package convertidor;

import algoritmos.ConvertidorER;

import java.util.HashMap;
import representacion.Alfabeto;
import representacion.Token;

/*Esta clase implementa el analizador lexico y el metodo para que el 
   constructor de AFNs pueda consumir tokens*/
public class Lexer {

    //Variable que representa al alfabeto
    private final Alfabeto alfabeto;

    //Expresion regular ingresada
    private final String exprReg;

    /*Variable que se utiliza como buffer de entrada, en donde se almacena
    la expresion regular en notacion postfijo*/
    private final StringBuffer buffer;

    //Tabla de simbolos validos esperados por el analizador
    private HashMap<String, Token.Tipos> tablaSimbolos;

    //Constructor de la clase
    public Lexer(Alfabeto alfabeto, String exprReg) {
        this.alfabeto = alfabeto;
        this.exprReg = exprReg;
        this.buffer = new StringBuffer(ConvertidorER.infijoAPostfijo(exprReg));
        crearTablaSimbolos();
    }

    /*Metodo que se encarga de consumir caracteres del buffer de entrada,
      convertirlos a tokens y retornarlos*/
    public Token sgteToken() throws Exception {
        String lexema = sgteLexema();

        Token.Tipos tipoToken = tablaSimbolos.get(lexema);

        if (null == tipoToken) {
            return new Token(Token.Tipos.DESCONOCIDO, lexema);
        } else {
            switch (tipoToken) {
                case ALFABETO:
                    return new Token(Token.Tipos.ALFABETO, lexema);
                default:
                    return new Token(tipoToken);
            }
        }
    }

    public Alfabeto getAlfabeto() {
        return alfabeto;
    }

    public String getExpresionRegular() {
        return exprReg;
    }

    //Metodo que consumelos caracteres del buffer, hasta agotarlos.
    private String sgteLexema() {
        String salida = "";

        if (buffer.length() > 0) {
            salida += buffer.charAt(0);
            buffer.deleteCharAt(0);
        }
        return salida;
    }

    //Tabla que se utiliza para validar los caracteres ingresados
    private void crearTablaSimbolos() {
        tablaSimbolos = new HashMap();

        tablaSimbolos.put("|", Token.Tipos.UNION);
        tablaSimbolos.put(".", Token.Tipos.CONCATENACION);
        tablaSimbolos.put("*", Token.Tipos.CERRADURA_KLEENE);
        tablaSimbolos.put("+", Token.Tipos.CERRADURA_POSITIVA);
        tablaSimbolos.put("?", Token.Tipos.OPCION);
        tablaSimbolos.put("", Token.Tipos.FINAL);

        for (int i = 0; i < alfabeto.getCantidad(); i++) {
            String simbolo = alfabeto.getSimbolo(i);
            if (tablaSimbolos.containsKey(simbolo)) {
                System.out.println("\nError, el simbolo " + simbolo 
                        + " del alfabeto ingresado es un operador");
                System.exit(1);
            } else {
                tablaSimbolos.put(simbolo, Token.Tipos.ALFABETO);
            }
        }
    }
}
