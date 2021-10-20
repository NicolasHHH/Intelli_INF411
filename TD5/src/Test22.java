
public class Test22 {

	public static void main(String[] args) {

		if (!Test22.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// test de KDtree.insert
		System.out.println("--Test de la methode insert");

		double p0[] = { 0.0, 0.0, 0.0 };
		KDTree kd0 = new KDTree(p0, 0);

		double p1[] = { -1.0, 0.0, 0.0 };
		KDTree kd1 = new KDTree(p1, 1);

		double p2[] = { 1.0, 0.0, 0.0 };
		KDTree kd2 = new KDTree(p2, 1);

		double p3[] = { 0.0, 2.0, 0.0 };

		double p4[] = { -1.0, -1.0, -1.0 };

		System.out.println("Test d'insertion a gauche ... ");
		kd0 = KDTree.insert(kd0, p1);

		assert (kd0 != null) : "Erreur kd0=insert(null, p0) doit retourner un nouveau KDTree contenant le point p0 a profondeur 0 mais a retourne null";

		assert (kd0.left!=null): "Erreur kd0=insert(kd0, p1) retourne un KDTree tel que kd0.left!=null"; 
		
		assert (kd0.left.point
				.equals(p1)) : "Erreur kd0=insert(kd0,p3) doit contenir le point p1 dans le sous-arbre gauche";

		kd0 = KDTree.insert(kd0, p2);

		assert (kd0.left != null) : "Avec kd0 = insert(kd0, p2), kd0.left est non null";

		kd0 = KDTree.insert(kd0, p4);

		assert (kd0.left.left != null) : "Avec kd0 = insert(kd0, p4), kd0.left.left est non null";
		assert (kd0.left.left.point.equals(p4)
				&& kd0.left.left.depth == 2) : "kd0.left.left.point est p4 a profondeur 2";

		System.out.println("Test d'insertion a droite ... ");


		assert (kd0.right != null): "kd0 =insert(kd0, p2), kd0.right est non null";
				
		assert(kd0.right.point.equals(p2)
				&& kd0.right.depth == 1) : "Avec kd0=insert(kd0,p2), kd0.right est p2 a profondeur 1";

		kd1 = KDTree.insert(kd0, p3);

		assert (kd1.right != null) : "Avec kd1=insert(kd0,p3), kd1.right est non null";
		assert(kd1.right.point.equals(p2) 
				&& kd1.right.depth == 1) : "Avec kd1 = insert(kd0, p3), kd1.right est p2 a profondeur 1";
						
		kd2 = KDTree.insert(kd1, p4);
	
		assert (kd2.right.right != null) : "Avec kd2=insert(kd1,p4), kd2.right.right est non null";
		assert(kd2.right.right.point.equals(p3)
				&& kd2.right.right.depth == 2) : "Avec kd2 = insert(kd1, p4), kd2.right.right est egal a p3 a profondeur 2"
						+ " on a kd2.right.right.depth=" + kd2.right.right.depth;

		
		System.out.println("[OK]");
	}

}
