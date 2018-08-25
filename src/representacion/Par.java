package representacion;

//Clase que representa un par de objetos de cualquier clase.
public final class Par<A extends Comparable<A>, B extends Comparable<B>>
                        implements Comparable<Par<A, B>> {
    private A primero;
  
    private B segundo;
   
    public Par(A primero) {
        this(primero, null);
    }

    public Par(A primero, B segundo) {
        setPrimero(primero);
        setSegundo(segundo);
    }

    public A getPrimero() {
        return primero;
    }

    public void setPrimero(A primero) {
        this.primero = primero;
    }

    public B getSegundo() {
        return segundo;
    }

    public void setSegundo(B segundo) {
        this.segundo = segundo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final Par<A, B> other = (Par<A, B>) obj;
        if (this.primero.equals(other.primero) && this.segundo.equals(other.segundo))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.primero != null ? this.primero.hashCode() : 0);
        hash = 79 * hash + (this.segundo != null ? this.segundo.hashCode() : 0);
        return hash;
    }
   
    @Override
    public String toString() {
        if (this.primero != null && this.segundo != null)
            return "(" + this.primero + ", " + this.segundo + ")";
        else if (this.primero != null)
            return "(" + this.primero + ")";
        else if (this.segundo != null)
            return "(null, " + this.segundo + ")";
        else
            return "()";
    }

    public int compareTo(Par<A, B> obj) {
        int diferencia = this.primero.compareTo(obj.primero);
       
        if (diferencia == 0)
            return this.segundo.compareTo(obj.segundo);
        else
            return diferencia;
    }
}

