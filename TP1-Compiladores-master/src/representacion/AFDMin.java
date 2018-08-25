package representacion;

//Clase que representa un AFN minimizado
public class AFDMin {

    //AFD original
    private final AFD original;

    //AFD resultante luego de aplicar la eliminacion de estados inalcanzables
    private final AFD despInalcanzables;

    //AFD resultante luego de aplicar el algoritmo de minimizacion
    private final AFD despMinimizacion;

    //AFD resultante luego de aplicar la eliminacion de estados identidades no finales.
    private final AFD despIdentidades;

    public AFDMin(AFD afdOriginal, AFD afdPostInalcanzables, AFD afdPostMinimizacion, AFD afdPostIdentidades) {
        this.original = afdOriginal;
        this.despInalcanzables = afdPostInalcanzables;
        this.despMinimizacion = afdPostMinimizacion;
        this.despIdentidades = afdPostIdentidades;
    }

    public AFD getOriginal() {
        return original;
    }

    public AFD getDespInalcanzables() {
        return despInalcanzables;
    }

    public AFD getDespMinimizacion() {
        return despMinimizacion;
    }

    public AFD getDespIdentidades() {
        return despIdentidades;
    }

    public boolean inalcanzablesEliminados() {
        return !original.toString().equals(despInalcanzables.toString());
    }

    public boolean identidadesEliminados() {
        return !despMinimizacion.toString().equals(despIdentidades.toString());
    }
}
