
public class Test31 {

	static String toString (int [] t) {
		String x = "[";
		for(int i:t) {x = x + " " + i;};
		return x+" ]";
	}
	
	static void test_initialize(UnionFind uf) {
		//System.out.print("Test de l'initialisation du champ size : ");
		int output;
		for (int i = 0; i < uf.cardinal(); i++) {
			output = uf.find(i);
			assert (output == i) : "erreur: on doit avoir find(" + i + ") = " + i + " mais find(" + i + ") = " + output;
			output = uf.getSize(i);
			assert (output == 1) : "\nerreur: l'initialisation de size dans UnionFind a produit size[" + i + "] = " + output
					+ "\n    ce qui veut dire que la classe contenant l'element " + i + " a " + output + " element(s)\n"
					+ "    etes-vous sur de vouloir initialiser le tableau size avec " + output + " ?";
		}
		//System.out.println("[OK]");
	}
	
	// a et b : les deux entiers que l'on connecte
	// expected_size : le tableau des tailles attendues
	static void connect_and_test_getSize(UnionFind uf, int a, int b,int[] expected_size) {
		int length = uf.cardinal();
		int output, expected;
		System.out.print("connexion "+a+" ~ "+b+" puis test getSize : ");
		uf.union(a,b);
		for (int i = 0; i < length; i++) {
			output = uf.getSize(i);
			expected = expected_size[i];
			assert(output==expected):"\nD'après votre implémentation, getSize("+i+") = "+output+"\nalors que la valeur attendue est "+expected+"\n"+"classes = "+uf;
		}
		//System.out.println(" [OK]");
		
	}
	
	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
	    }

		System.out.print("Test du champ size de UnionFind... ");
		
		// Déclaration de la variable qui contiendra l'instance de la classe UnionFind à tester
		UnionFind uf;

		//System.out.println("On crée une instance de la classe UnionFind à 8 éléments,\npuis on effectue successivement plusieurs connexions,\net on vérifie après chacune que la méthode getSize est correcte.");
		
		// Création d'une instance de la classe UnionFind que l'on va tester
		uf = new UnionFind(8);
		test_initialize(uf);
		connect_and_test_getSize(uf,1,3,new int[] { 1, 2, 1, 2, 1, 1, 1, 1 });
		connect_and_test_getSize(uf,5,1,new int[] { 1, 3, 1, 3, 1, 3, 1, 1 });
		connect_and_test_getSize(uf,3,7,new int[] { 1, 4, 1, 4, 1, 4, 1, 4 });
		connect_and_test_getSize(uf,0,1,new int[] { 5, 5, 1, 5, 1, 5, 1, 5 });
		connect_and_test_getSize(uf,4,6,new int[] { 5, 5, 1, 5, 2, 5, 2, 5 });
		connect_and_test_getSize(uf,3,6,new int[] { 7, 7, 1, 7, 7, 7, 7, 7 });

		//System.out.println("On crée une instance de la classe UnionFind à 10 éléments,\npuis on effectue successivement plusieurs connexions,\net on vérifie après chacune que la méthode getSize est correcte.");
		// Création d'une autre instance de la classe UnionFind que l'on va tester
		uf = new UnionFind(10);
		test_initialize(uf);
		connect_and_test_getSize(uf,9,0,new int[] {2,1,1,1,1,1,1,1,1,2});
		connect_and_test_getSize(uf,1,8,new int[] {2,2,1,1,1,1,1,1,2,2});
		connect_and_test_getSize(uf,7,2,new int[] {2,2,2,1,1,1,1,2,2,2});
		connect_and_test_getSize(uf,3,6,new int[] {2,2,2,2,1,1,2,2,2,2});
		connect_and_test_getSize(uf,5,4,new int[] {2,2,2,2,2,2,2,2,2,2});
		connect_and_test_getSize(uf,0,1,new int[] {4,4,2,2,2,2,2,2,4,4});
		connect_and_test_getSize(uf,6,7,new int[] {4,4,4,4,2,2,4,4,4,4});
		connect_and_test_getSize(uf,1,2,new int[] {8,8,8,8,2,2,8,8,8,8});
		connect_and_test_getSize(uf,0,6,new int[] {8,8,8,8,2,2,8,8,8,8});
		connect_and_test_getSize(uf,0,5,new int[] {10,10,10,10,10,10,10,10,10,10});
		System.out.println("[OK]");
	}
}
