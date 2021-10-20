
public class Test21 {

	public static void main(String[] args) {

		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// test de KDtree.compare
		System.out.println("--Test de la methode difference et compare ...");
		double p0[] = { 0.0, 0.0, 0.0 };
		KDTree t0 = new KDTree(p0, 0);
		double p1[] = { -1.0, 0.0, 0.0 };

		KDTree t1 = new KDTree(p1, 2);

		double p2[] = { 1.0, 0.0, 0.0 };
		KDTree t2 = new KDTree(p2, 1);

		t0.left = t1;
		t0.right = t2;

		assert (t0.compare(p0)) : "p0 se situe a la racine de t0";
		assert (t0.compare(p2)) : "p2 est dans le sous-arbre droit de t0";

		double p01[] = { -1.0, 3.0 };
		double p02[] = { 4.0, 1.0 };

		KDTree t01 = new KDTree(p01, 1);
		KDTree t02 = new KDTree(p02, 1);
		t01.left = t02;

		assert (!t01.compare(p02)) : "p02 est dans le sous-arbre gauche de t01";
	
		System.out.println("[OK]");

	}
}

