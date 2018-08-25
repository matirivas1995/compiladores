package representacion;

//Clase para representar un Automata Finito Deterministico (AFD). 
public class AFD extends Automata {
   
    //Conjunto de D-Estados
    private Conjunto<Conjunto<Estado>> dEstados;
   
    public AFD() {
       this(null, "");
    }

    public AFD(Alfabeto alfabeto, String exprReg) {
        super(alfabeto, exprReg);
        dEstados = null;
    }
   
    public Conjunto<Conjunto<Estado>> getdEstados() {
        return dEstados;
    }


    public void setdEstados(Conjunto<Conjunto<Estado>> dEstados) {
        this.dEstados = dEstados;
    }
   
    public String estadosDtoString() {
        String str = "";
       
        for (int i=0; i < dEstados.cantidad(); i++) {
            Conjunto<Estado> conj = dEstados.obtener(i);
            Estado actual = getEstado(i);
           
            str += actual + " --> " + conj + "\n";
        }
       
        return str;
    }
   
    //Retorna la tabla de transicion de estados.
    @Override
    public TablaDeTransicion getTablaTransicion() {
        TablaDeTransicion tabla;
       
        if (getdEstados() != null) {
            int cantFil = getEstados().cantidad();
            int cantCol = getAlfabeto().getCantidad() + 2;

            tabla = cargarTablaTransicion(cantFil, cantCol, 1);
            tabla.setCabeceraEn("Estados del AFD", 0);

            for (int i=0; i < dEstados.cantidad(); i++)
                tabla.setValorEn(dEstados.obtener(i), i, 0);
        }
        else {
            int cantFil = getEstados().cantidad();
            int cantCol = getAlfabeto().getCantidad() + 1;

            tabla = cargarTablaTransicion(cantFil, cantCol, 0);
        }
       
        return tabla;
    }
}

