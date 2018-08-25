package representacion;

//Clase que representa un AFN
public class AFN extends Automata {

    //Constructor por defecto
    public AFN() {
        super();
    }

    //Constructor para un determinado alfabeto y expresion regular
    public AFN(Alfabeto alfabeto, String exprReg) {
        super(alfabeto, exprReg);
    }

    //Retorna la tabla de transicion de estados.
    @Override
    public TablaDeTransicion getTablaTransicion() {
        int cantFil = getEstados().cantidad();
        int cantCol = getAlfabeto().getCantidad() + 2;

        return cargarTablaTransicion(cantFil, cantCol, 0);
    }
}
