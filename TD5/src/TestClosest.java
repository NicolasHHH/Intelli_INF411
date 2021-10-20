import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;

public abstract class TestClosest {

	String name;

	abstract double[] closest(KDTree tree, double[] point);

	static String pointToString(KDTree tree, double[] point) {
		String res = String.format(Locale.ROOT, "[%f, %f, %f]", point[0], point[1], point[2]);
		if (tree != null) {
			if (Arrays.equals(point, tree.point))
				res += " (= racine de l'arbre)";
			else if (tree.left != null && Arrays.equals(point, tree.left.point))
				res += " (= tree.left.point)";
			else if (tree.right != null && Arrays.equals(point, tree.right.point))
				res += " (= tree.right.point)";
		}
		return res;
	}

	public void testClosest(int size, int runs) {

		if (!TestClosest.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// on remplit un arbre 3d avec les couleurs de pixels d'une image,
		// et on recupere un sous-ensemble des memes points pour s'en servir
		// dans les tests
		Picture pic = new Picture("photo.jpg");
		int height = pic.height();
		int width = pic.width();
		int stride = height * width / size;
		assert size >= 0 && stride > 0 : "Parametre size incorrect";
		assert runs <= size : "runs doit etre <= size";
		int testStride = (size - 3) / (runs - 3);

		KDTree tree = null;
		Vector<double[]> testPoints = new Vector<double[]>();
		for (int i = 0; i < size; i++) {
			int c = pic.getRGB((i * stride) % width, i * stride / width);
			double[] point = new double[3];
			point[0] = (c >> 16) & 255;
			point[1] = (c >> 8) & 255;
			point[2] = c & 255;
			tree = KDTree.insert(tree, point);
			if (i < 3 || (i - 3) % testStride == 0 && testPoints.size() < runs)
				testPoints.add(point);
		}

		System.out.printf("--Test de la methode %s (%d points, 16*%d tests)...\n", name, size, runs);

		long startTime = System.currentTimeMillis();

		for (double[] a : testPoints) {
			// on utilise ici le fait que par construction, les points de
			// notre arbre test sont a coordonnees entieres
			for (int i = 0; i < 8; i++) {
				double[] b = a.clone();
				for (int j = 0; j < 3; j++)
					if ((i & (1 << j)) != 0)
						b[j] += .1;
				double[] c = closest(tree, b);
				assert (Arrays.equals(a, c)) : String.format("Le point le plus proche de %s devrait etre %s et non %s",
						pointToString(tree, b), pointToString(tree, a), pointToString(tree, c));
			}
		}
		
		// Second test avec des perturbations un peu plus grandes
		Random rnd = new Random(0);
		for (double[] a : testPoints) {
			for (int i = 0; i < 8; i++) {
				double[] b = a.clone();
				for (int j = 0; j < 3; j++)
					b[j] += rnd.nextGaussian()/2;
				double[] c = closest(tree, b);
				assert (KDTree.sqDist(c, b) <= KDTree.sqDist(a, b)) : String.format("Le point le plus proche de %s n'est pas %s (%s est plus proche)",
						pointToString(tree, b), pointToString(tree, c), pointToString(tree, a));
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.printf("Temps total : %f sec.\n", (endTime - startTime) / 1000.);
		System.out.println("[OK]");

	}
}
