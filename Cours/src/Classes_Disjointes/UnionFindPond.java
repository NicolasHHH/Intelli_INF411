package Classes_Disjointes;

public class UnionFindPond {
    private int[] link;

    // pondéré
    private int[] rank; // uniquement pour les représentants

    UnionFindPond(int n){
        if (n < 0) throw new IllegalArgumentException();
        this.link = new int[n];
        for (int i = 0; i < n; i++)
            this.link[i] = i;

        // pondéré
        this.rank = new int[n];
    }
    int find(int i){
        int p = this.link[i];
        if (p == i) return i;

        int r = this.find(p);
        this.link[i] = r; // intérêt d'écrire récursivement
        return r;

        // return this.find(p); // pas de risque StackOverFlow
    }
    void union(int i, int j){
        int ri = this.find(i);
        int rj = this.find(j);
        if (ri == rj) return; // même classe déjà
        if (this.rank[ri] < this.rank[rj])
            this.link[ri] = rj;
        else {
            this.link[rj] = ri;
            if (this.rank[ri] == this.rank[rj])
                this.rank[ri]++;
        }
    }
}
