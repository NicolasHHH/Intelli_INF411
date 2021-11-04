package File_de_Priorite;


import java.util.NoSuchElementException;

public class PQ  {
    private int[] elts; // ABR par tas
    private int   size;

    PQ(int capacity) {
        this.elts = new int[capacity];
        this.size = 0;         // initialement vide
    }

    //complexité O(1)
    int getMin() {
        if (size == 0) throw new NoSuchElementException();
        return this.elts[0];
    }

    private void moveUp(int x, int i) {
        while (i > 0) {
            int fi = (i - 1) / 2;
            int y = this.elts[fi];
            if (y <= x) break;
            this.elts[i] = y;
            i = fi; }
        this.elts[i] = x;
    }

    void add(int x) {
        moveUp(x, this.size++);
    }
    //cout <= logN = hauteur de l'arbre

    private void moveDown(int x, int i) {
        int n = this.size;
        while (true) {
            int j = 2 * i + 1;
            if (j >= n) break;
            if (j + 1 < n && this.elts[j] > this.elts[j + 1]) j++;
            if (x <= this.elts[j]) break;
            this.elts[i] = this.elts[j];
            i = j;
        }
        this.elts[i] = x;
    }

    int removeMin() {
        if (size == 0) throw new NoSuchElementException();
        int n = --this.size;
        int r = this.elts[0];
        if (n > 0)
            moveDown(this.elts[n], 0);
        return r;
    }

}
