package representacion;

public class Etiqueta {

    String nombre;
    Integer index;

    public Etiqueta(String nombre, Integer index) {
        this.nombre = nombre;
        this.index = index;
    }

    public Etiqueta() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
