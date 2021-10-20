import java.util.Random;

public class Test41 {

	static void testSize(KDTree inputTree, int output) {
		assert (output == KDTree.size(inputTree)) : " le nombre de noeuds doit etre " + output;

	}

	static void testSum(KDTree inputTree, double[] inputAcc, double[] outputAcc) {

		KDTree.sum(inputTree, inputAcc);
		
		for (int i = 0; i < inputTree.point.length; i++) {
			assert (Math.abs(outputAcc[i]-inputAcc[i])<1e-9): "Erreur dans le calcul de sum";
		}
		
	}

	static void testAverage(KDTree inputTree, double[] output) {

		double[] acc = new double[inputTree.point.length];

		KDTree.sum(inputTree, acc);
		int size1=KDTree.size(inputTree);
		for (int i = 0; i < inputTree.point.length; i++) {
			assert (Math.abs( output[i]- acc[i] / size1) < 1e-9): "Erreur pour le calcul de la moyenne au" + " coefficient" + i;
		}

	}

	public static void main(String[] args) {

		if (!Test41.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		Picture pic = new Picture("photo.jpg");
		// pic.show();

		Random r = new Random();

		KDTree tree = null;
		int iter = 20000;
		int size = KDTree.size(tree);

		assert (size == 0) : "le nombre de noeud de tree est 0";

		for (int i = 0; i < iter; i++) {
			int row = r.nextInt(pic.height()), col = r.nextInt(pic.width());
			int c = pic.getRGB(col, row);

			// construction du noeud labelle par un point qui contient les 3 coord. couleurs
			double[] point = new double[3];
			point[0] = (c >> 16) & 255;
			point[1] = (c >> 8) & 255;
			point[2] = c & 255;
			tree = KDTree.insert(tree, point);
		}
		System.out.println("--Test de la methode size ... ");

		testSize(tree, 20000);
		double[] p0 = { -2.0, 0.0, 0.0 };
		KDTree treeTest = KDTree.insert(null, p0);
		testSize(treeTest, 1);
		double[] p2 = { -2.0, 0.0, 0.0 };
		double[] p1 = { -1.0, -1.0, 0.0 };

		treeTest = KDTree.insert(treeTest, p1);

		testSize(treeTest, 2);

		System.out.println("--Test de la methode sum ... ");

		treeTest = KDTree.insert(treeTest, p2);

		double[] inacc1 = new double[treeTest.point.length];

		double[] outacc1 = new double[] {-5.0, -1.0, 0.0 };
		testSum(treeTest, inacc1, outacc1);

		System.out.println("--Test de la methode average ... ");

		double[] pointmoyenne = KDTree.average(treeTest);
		testAverage(treeTest, pointmoyenne);
		
		double p3[] = { 0.0, 0.5, 0.0 };

		KDTree treeTest1 = new KDTree(p3, 1);
		double p4[] = { -1.0, -1.0, -1.0 };
		double p5[] = { -1.0, 6.0, 0.5 };
		
		
		treeTest1 = KDTree.insert(treeTest1, p3);
		treeTest1 = KDTree.insert(treeTest1, p4);
		treeTest1 = KDTree.insert(treeTest1, p5);
		
		double[] inacc2 = new double[treeTest1.point.length];

		double[] outacc2 = new double[] {-2.0, 6.0, -0.5 };
		
		testSum(treeTest1,inacc2,outacc2);
		
		pointmoyenne = KDTree.average(treeTest1);
		testAverage(treeTest1,pointmoyenne);
		
		System.out.println("[OK]");
	}

}
