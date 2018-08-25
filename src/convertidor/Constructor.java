package convertidor;

import algoritmos.Thompson;
import java.util.Stack;
import representacion.AFN;
import representacion.Alfabeto;
import representacion.Token;

//Clase que construye el AFN para una expresion regular
public class Constructor {

    //El analizador lexico. Se utilizara para obtener tokens
    private final Lexer lexer;

    //Se utilizara para almacenar los AFN creados
    private final Stack<AFN> pila;

    //Variable para el token actual
    private Token token;

    //Constructor de la clase.
    public Constructor(Alfabeto alfabeto, String exprReg) {
        lexer = new Lexer(alfabeto, exprReg);
        pila = new Stack();
    }

    //Metodo que se encarga de construir el AFN final a partir de AFNs básicos
    public AFN construirAFN() throws Exception {
        boolean bandera = true;
        while (bandera) {
            token = obtenerToken();
            if (token != null) {
                AFN afn, afn1, afn2;
                String simbolo = token.getValor();
                switch (token.getTipo()) {
                    case ALFABETO:
                        afn = Thompson.basico(simbolo);
                        pila.push(afn);
                        break;
                    case CONCATENACION:
                        afn1 = pila.pop();
                        afn2 = pila.pop();
                        afn = Thompson.concatenacion(afn1, afn2);
                        pila.push(afn);
                        break;
                    case UNION:
                        afn1 = pila.pop();
                        afn2 = pila.pop();
                        afn = Thompson.union(afn1, afn2);
                        pila.push(afn);
                        break;
                    case OPCION:
                        afn1 = pila.pop();
                        afn = Thompson.opcion(afn1);
                        pila.push(afn);
                        break;
                    case CERRADURA_KLEENE:
                        afn1 = pila.pop();
                        afn = Thompson.cerraduraKleene(afn1);
                        pila.push(afn);
                        break;
                    case CERRADURA_POSITIVA:
                        afn1 = pila.pop();
                        afn = Thompson.cerraduraPositiva(afn1);
                        pila.push(afn);
                        break;
                    default:
                        //se llego al fin
                        bandera = false;
                }
            } else {
                error("Se obtuvo un token nulo");
            }
        }
        AFN afnResul = pila.pop();
        afnResul.setAlfabeto(lexer.getAlfabeto());
        afnResul.setExprReg(lexer.getExpresionRegular());
        return afnResul;
    }

    //Metodo que se encarga de lanzar excepciones
    private void error(String mensaje) throws Exception {
        String mensajeCompleto = "";
        mensajeCompleto += "Error de sintaxis\n";
        mensajeCompleto += "Caracter: " + token.getValor() + "\n";
        mensajeCompleto += "Mensaje : " + mensaje;

        throw new Exception(mensajeCompleto);
    }

    //Metodo que obtiene el siguiente token
    private Token obtenerToken() throws Exception {
        return lexer.sgteToken();
    }
}
