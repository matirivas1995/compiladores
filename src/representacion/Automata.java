package representacion;

//Clase que representa un Automata Finito
public abstract class Automata {

    //Estados del automata
    protected Conjunto<Estado> estados;

    //Expresion regular del automata
    protected String exprReg;

    //Alfabeto del automata
    protected Alfabeto alfabeto;

    //Registro del automata
    protected String registro;

    protected Automata() {
        this(null, "");
    }

    protected Automata(Alfabeto alfabeto, String exprReg) {
        estados = new Conjunto<>();
        setAlfabeto(alfabeto);
        setExprReg(exprReg);
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Alfabeto getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(Alfabeto alfabeto) {
        this.alfabeto = alfabeto;
    }

    public String getExprReg() {
        return exprReg;
    }

    public void setExprReg(String exprReg) {
        this.exprReg = exprReg;
    }

    public Estado getEstadoInicial() {
        return estados.obtenerPrimero();
    }

    public Conjunto<Estado> getEstadosFinales() {
        Conjunto<Estado> finales = new Conjunto<>();

        for (Estado tmp : estados) {
            if (tmp.getEsFinal()) {
                finales.agregar(tmp);
            }
        }
        return finales;
    }

    public Conjunto<Estado> getEstadosNoFinales() {
        Conjunto<Estado> noFinales = new Conjunto<>();

        for (Estado tmp : estados) {
            if (!tmp.getEsFinal()) {
                noFinales.agregar(tmp);
            }
        }
        return noFinales;
    }

    //Metodo que agrega un estdo al automata
    public void agregarEstado(Estado estado) {
        estados.agregar(estado);
    }

    public Estado getEstado(int pos) {
        return estados.obtener(pos);
    }

    public Conjunto<Estado> getEstados() {
        return estados;
    }

    public int cantidadEstados() {
        return estados.cantidad();
    }

    public void iniciarRecorrido() {
        for (Estado tmp : estados) {
            tmp.setVisitado(false);
        }
    }

    //Metodo que retorna la tabla de transiciones de estados
    public abstract TablaDeTransicion getTablaTransicion();

    //Metodo que genera y carga la tabla de transicion de estados del Automata.
    protected TablaDeTransicion cargarTablaTransicion(int cantFil, int cantCol, int colDesde) {
        //Cabeceras de las columnas de la tabla
        String[] cabecera = new String[cantCol];

        //Datos de la tabla
        Object[][] datos = new Object[cantFil][cantCol];

        //Titulo para los estados
        cabecera[colDesde] = "Estados";

        //Se carga la cabecera con simbolos del alfabeto
        for (int i = colDesde + 1; i < cantCol; i++) {
            cabecera[i] = getAlfabeto().getSimbolo(i - colDesde - 1);
        }

        //Se carga la primera columna de datos
        for (int i = 0; i < cantFil; i++) {
            datos[i][colDesde] = getEstado(i);
        }

        //Se cargan las transiciones
        for (Estado e : getEstados()) {
            int fil = e.getIdentificador();

            for (Transicion t : e.getTransiciones()) {
                int col = getAlfabeto().obtenerPosicion(t.getSimbolo());

                if (datos[fil][col + colDesde + 1] == null) {
                    datos[fil][col + colDesde + 1] = new Conjunto();
                }

                int id = t.getEstado().getIdentificador();
                ((Conjunto<Integer>) datos[fil][col + colDesde + 1]).agregar(id);
            }
        }
        String vacio = "";
        for (int i = 0; i < cantFil; i++) {
            for (int j = colDesde + 1; j < cantCol; j++) {
                if (datos[i][j] == null) {
                    datos[i][j] = vacio;
                } else {
                    @SuppressWarnings("rawtypes")
                    Conjunto c = (Conjunto) datos[i][j];
                    if (c.cantidad() == 1) {
                        datos[i][j] = c.obtenerPrimero();
                    }
                }
            }
        }
        return new TablaDeTransicion(cabecera, datos);
    }

    @Override
    public String toString() {
        String str = "";

        for (Estado tmp : getEstados()) {
            str += tmp.toString();

            for (Transicion trans : tmp.getTransiciones()) {
                str += " --> " + trans.getEstado() + "{" + trans.getSimbolo() + "}";
            }

            str += "\n";
        }
        return str;
    }

    public static void copiarEstados(Automata afOrigen, Automata afDestino, int incremento) {
        copiarEstados(afOrigen, afDestino, incremento, 0);
    }

    public static void copiarEstados(Automata afOrigen, Automata afDestino,
            int incrementoTrans, int omitidos) {

        /*Cantidad a incrementar el identificador de un estado de afnOrigen 
        para convertirlo en el estado de afnDestino*/
        int incrementoEst = incrementoTrans;

        for (int i = omitidos; i < afOrigen.cantidadEstados(); i++) {
            afDestino.agregarEstado(new Estado(afDestino.cantidadEstados()));
        }

        int contador = 0;

        for (Estado tmp : afOrigen.getEstados()) {

            if (omitidos > contador++) {
                continue;
            }
            Estado objetivo = afDestino.getEstado(tmp.getIdentificador() + incrementoEst);
            copiarTransiciones(afDestino, tmp.getTransiciones(), objetivo, incrementoTrans);
        }
    }

    public static void copiarTransiciones(Automata afDestino, Conjunto<Transicion> transiciones,
            Estado objetivo, int incrementoTrans) {

        for (Transicion trans : transiciones) {
            int idDestino = trans.getEstado().getIdentificador();
            String simbolo = trans.getSimbolo();

            Estado estadoDestino = afDestino.getEstado(idDestino + incrementoTrans);
            Transicion nuevaTrans = new Transicion(estadoDestino, simbolo);

            objetivo.getTransiciones().agregar(nuevaTrans);
        }
    }
}
