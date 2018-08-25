package representacion;

//Clase utilizada para representar un token de la expresión regular
public class Token {
   
    //Tipo del token
    private Tipos tipo;
   
    //Valor del token
    private String valor;
   
    //Constructor por defecto del token
    public Token(Tipos token) throws Exception {
        switch (token) {
            case CERRADURA_KLEENE:
                tipo = Tipos.CERRADURA_KLEENE;
                valor = "*";
                break;
            case CERRADURA_POSITIVA:
                tipo = Tipos.CERRADURA_POSITIVA;
                valor = "+";
                break;
            case OPCION:
                tipo = Tipos.OPCION;
                valor = "?";
                break;
            case CONCATENACION:
                tipo = Tipos.CONCATENACION;
                valor = ".";
                break;
            case UNION:
                tipo = Tipos.UNION;
                valor = "|";
                break;
            case FINAL:
                tipo = Tipos.FINAL;
                valor = "";
                break;
            default:
                throw new Exception("Token invalido");
        }
    }
   
    //Constructor para simbolos del alfabeto y para simbolos desconocidos.
    public Token(Tipos token, String simbolo) throws Exception {
        switch (token) {
            case ALFABETO:
                tipo = Tipos.ALFABETO;
                valor = simbolo;
                break;
            case DESCONOCIDO:
                tipo = Tipos.DESCONOCIDO;
                valor = simbolo;
                break;
            default:
                throw new Exception("Token invalido");
        }
    }
   
    public Tipos getTipo() {
        return tipo;
    }
   
    public String getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return valor;
    }
    
    //Enumacion con los tipos de un token
    public enum Tipos {
    
        UNION, //Operador de union, "|" 
        
        CONCATENACION, //Operador de concatenacion "."
        
        OPCION, //Operador de opcion, "?"

        CERRADURA_KLEENE, //Operador de cerradura de Kleene, "*"

        CERRADURA_POSITIVA, //Operador de cerradura positiva, "+"

        ALFABETO, //Cualquier simbolo del alfabeto.

        FINAL, //Finalizador de una expresion regular (EOF).

        DESCONOCIDO //Token desconocido (invalido) 
    }
}


