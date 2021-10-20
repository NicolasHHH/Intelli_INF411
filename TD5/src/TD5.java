/* TD5.  Arbres k-dimensionnels */

import java.util.Vector;

class KDTree {
    int depth;
    double[] point;
    KDTree left;
    KDTree right;
    static int maxp = 0;

    // Constructeur pour une feuille
    KDTree(double[] point, int depth) {
        this.point = point;
        this.depth = depth;
    }

    public String pointToString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        if (this.point.length > 0)
            sb.append(this.point[0]);
        for (int i = 1; i < this.point.length; i++)
            sb.append("," + this.point[i]);
        sb.append("]");
        return sb.toString();
    }

    // Question 1

    // <0 si a doit etre range dans le sous-arbre gauche, >=0 si a doit etre
    // range dans le sous-arbre droit
    double difference(double[] a) {
        int k = a.length;
        return a[this.depth % k] - this.point[this.depth % k];
    }

    // true si a doit etre range dans le sous-arbre droit, false sinon
    boolean compare(double[] a) {
        if (difference(a) >= 0) return true;
        else return false;
    }

    // Question 2

    // insere le point a dans l'arbre tree de telle  sorte que la propriete
    // d'arbre kd soit preservee, renvoie la racine du nouvel arbre

    static KDTree insert(KDTree tree, double[] a) {
        if (tree == null) {
            return new KDTree(a, 0);
        }

        return insert(tree, a, tree.depth);
    }

    static KDTree insert(KDTree tree, double[] a, int depth) {
        if (tree == null) {
            return new KDTree(a, depth);
        }
        if (!tree.compare(a)) {
            tree.left = insert(tree.left, a, depth + 1);
        } else {
            tree.right = insert(tree.right, a, depth + 1);
        }
        return tree;
    }


    // Question 3

    // calcule le carre de la distance euclidienne entre deux points
    static double sqDist(double[] a, double[] b) {
        double somme = 0;
        for (int i = 0; i < a.length; i++) {
            somme += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return somme;
    }


    // renvoie le point le plus proche du point a parmi les points de tree et champion

    static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
        if (tree == null) return champion;

        double[] cleft = closestNaive(tree.left, a, champion);
        double[] cright = closestNaive(tree.right, a, champion);

        double dis = sqDist(a, champion);
        if (sqDist(a, tree.point) < dis) {
            champion = tree.point;
            dis = sqDist(a, champion);
        }
        //System.out.println(tree.point[0]);
        if (sqDist(a, cleft) < dis) {
            champion = cleft;
            dis = sqDist(a, champion);
        }
        if (sqDist(a, cright) < dis) {
            champion = cright;
        }
        return champion;
    }

    // renvoie le point de tree le plus proche du point a
    static double[] closestNaive(KDTree tree, double[] a) {
        if (tree == null) {
            return null;
        }
        double[] champion = tree.point;
        return closestNaive(tree, a, champion);
    }


    // renvoie le point le plus proche du point a parmi les points de tree

    static double[] closest(KDTree tree, double[] a, double[] champion) {

        if (tree == null)
            return champion;

        // sert a InteractiveClosest pour afficher la progression
        InteractiveClosest.trace(tree.point, champion);

        // throw new Error("Methode double[] closest(KDTree tree, double[] a, double[] champion) a completer (Question 3.3)");

        double c = tree.difference(a);

        // déterminer où se situe a par rapport à tree (dans sous-arbre gauche ou droite)

        KDTree first, second;
        if (c < 0) { // a est range dans le sous-arbre gauche
            first = tree.left;
            second = tree.right;
        } else { // a est range dans le sous-arbre droit
            first = tree.right;
            second = tree.left;
        }

        champion = closest(first, a, champion);

        double d = sqDist(a, champion);

        if (d >= c * c) {
            if (d > sqDist(a, tree.point))
                champion = tree.point;
            champion = closest(second, a, champion);
        }

        return champion;

    }

    static double[] closest(KDTree tree, double[] a) {
        if (tree == null)
            return null;
        return closest(tree, a, tree.point);
    }


    // Question 4

    // calcule le nombre de noeuds de l'arbre
    static int size(KDTree tree) {
        if (tree == null) {
            return 0;
        } else return size(tree.left) + size(tree.right) + 1;
    }

    // calcule la somme des points de l'arbre
    static void sum(KDTree tree, double[] acc) {
        if (tree != null) {
            sum(tree.left, acc);
            sum(tree.right, acc);
            for (int i = 0; i < acc.length; i++) {
                acc[i] += tree.point[i];
            }
        }
    }

    // calcule l'isobarycentre des points de l'arbre
    static double[] average(KDTree tree) {
        if (tree == null) return null;
        else {
            double[] acc = new double[tree.point.length];
            sum(tree, acc);
            int size = size(tree);
            for (int i = 0; i < acc.length; i++) {
                acc[i] /= size;
            }
            return acc;
        }
    }

    // elague tree de maniere a ne conserver que
    // maxpoints, renvoie le vecteur de points qui en resulte
    static int palette(KDTree tree, int maxpoints, Vector<double[]> acc) {
        if (tree == null || maxpoints <= 0)
            return 0;
        if (maxpoints == 1) {
            acc.add(average(tree));
            return 1;
        }
        System.out.println(size(tree) + " " + size(tree.left) + " ");
        int mid = (int) Math.round((double) maxpoints * size(tree.left) / size(tree));
        int total = palette(tree.left, mid, acc);
        total += palette(tree.right, maxpoints - mid, acc);
        if (total < maxpoints) {
            acc.add(average(tree));
            total++;
        }
        return total;
    }

    // elague tree de maniere a ne conserver que
    // maxpoints, renvoie le vecteur de points qui en resulte
    static Vector<double[]> palette(KDTree tree, int maxpoints) {
        // throw new Error("Methode Vector<double[]> palette(KDTree, int) a completer (Question 4.2)");
        Vector<double[]> acc = new Vector<double[]>();
        palette(tree, maxpoints, acc);
        System.out.println(acc.size());
        return acc;
    }

}
