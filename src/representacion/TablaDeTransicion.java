package representacion;

//Clase que representa la tabla de transiciones
public class TablaDeTransicion {

    //Valores de la tabla de transiciones
    private final Object[][] datos;

    //Cabecera de la tabla de transiciones
    private final String[] cabecera;

    //Constructor que utiliza una cabecera y datos especificados
    public TablaDeTransicion(String[] cabecera, Object[][] datos) {
        this.cabecera = cabecera;
        this.datos = datos;
    }

    public int getCantidadFilas() {
        return datos.length;
    }

    public int getCantidadColumnas() {
        return cabecera.length;
    }

    public String getNombreColumna(int indiceColumna) {
        return cabecera[indiceColumna];
    }

    public Object getvalorEn(int indiceFila, int indiceColumna) {
        return datos[indiceFila][indiceColumna];
    }

    public void setValorEn(Object valor, int indiceFila, int indiceColumna) {
        datos[indiceFila][indiceColumna] = valor;
    }

    //Modifica el valor de la cabecera de una columna.
    public void setCabeceraEn(String valor, int indiceColumna) {
        cabecera[indiceColumna] = valor;
    }

    public void imprimirTablaAFN() {
        for (int i = 0; i < getCantidadColumnas(); i++) {
            System.out.printf("|%10s\t|", getNombreColumna(i));
        }
        System.out.println();
        for (int i = 0; i < getCantidadFilas(); i++) {
            for (int j = 0; j < getCantidadColumnas(); j++) {
                System.out.printf("|%10s\t|", getvalorEn(i, j));
            }
            System.out.println();
        }
        System.out.println("\n");
    }
    
    public void imprimirTablaAFD() {
        for (int i = 0; i < getCantidadColumnas(); i++) {
            System.out.printf("|%15s\t\t|", getNombreColumna(i));
            
        }
        System.out.println();
        for (int i = 0; i < getCantidadFilas(); i++) {
            for (int j = 0; j < getCantidadColumnas(); j++) {
                System.out.printf("|%15s\t\t|", getvalorEn(i, j));
            }
            System.out.println();
        }
        System.out.println("\n");
    }
}
