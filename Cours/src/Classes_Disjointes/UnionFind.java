package Classes_Disjointes;

public class UnionFind {
    private int[] link;

    UnionFind(int n){
        if (n < 0) throw new IllegalArgumentException();
        this.link = new int[n];
        for (int i = 0; i < n; i++)
            this.link[i] = i;
    }
    int find(int i){
        int p = this.link[i];
        if (p == i) return i;
        return this.find(p); // pas de risque StackOverFlow
    }
    void union(int i, int j){
        int ri = this.find(i);
        int rj = this.find(j);
        this.link[ri] = rj;  // réunir les représentants dans une union
    }
}
