package representacion;

public class TablaDeSimbolos {
    String token;
    String expr;
    String cadena;
    String identificador;

    public TablaDeSimbolos() {
    }

    public TablaDeSimbolos(String token, String expr, String cadena, String identificador) {
        this.token = token;
        this.expr = expr;
        this.cadena = cadena;
        this.identificador = identificador;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }
}
