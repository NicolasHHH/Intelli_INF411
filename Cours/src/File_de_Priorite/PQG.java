package File_de_Priorite;

import java.util.NoSuchElementException;
import java.util.Vector;

public class PQG<E extends Comparable<E>> {
    private Vector<E> elts;
    private int   size;

    PQG() {
        this.elts = new Vector<E>();
    }


    E getMin() {
        if (size == 0) throw new NoSuchElementException();
        return this.elts.get(0);
    }

    private void moveUp(E x, int i) {
        while (i > 0) {
            int fi = (i - 1) / 2;
            E y = this.elts.get(fi);
            if (y.compareTo(x) <=0 ) break;
            this.elts.setElementAt(y,i);
            i = fi;
        }
        this.elts.setElementAt(x,i);
    }

    void add(E x) {
        moveUp(x, this.size++);
    }
    //cout <= logN = hauteur de l'arbre

    private void moveDown(E x, int i) {
        int n = this.size;
        while (true) {
            int j = 2 * i + 1;
            if (j >= n) break;
            if (j + 1 < n && this.elts.get(j).compareTo( this.elts.get(j+1))>0) j++;
            if (x.compareTo(this.elts.get(j))<=0) break;
            this.elts.setElementAt(this.elts.get(j),i);
            i = j;
        }
        this.elts.setElementAt(x,i);
    }

    E removeMin() {
        if (size == 0) throw new NoSuchElementException();
        int n = --this.size;
        E r = this.elts.get(0);
        if (n > 0)
            moveDown(this.elts.get(n), 0);
        return r;
    }

}
