public class Test1 {

	// Test length: itératif, pas récursif
	public static boolean TestLength() {
		Singly<Integer> chain;
		// test lenght(null) == 0
		chain = null;
		assert (Singly.length(chain) == 0) : "\nerreur : length(null) vaut "+Singly.length(chain)+" or la réponse attendue est 0\n";		
		// test lenght(ptr -> null) == 1
		chain = new Singly<Integer>(1, null);
		assert (Singly.length(chain) == 1) : "\nerreur : length(l) vaut "+Singly.length(chain)+" or la réponse attendue est 1\n";
		Integer[] data = new Integer[] {1,3};
		chain = new Singly<Integer>(data);
		assert (Singly.length(chain) == 2) : "\nerreur : length(l) vaut "+Singly.length(chain)+" or la réponse attendue est 2\n";
		
		// test avec 200000 valeurs
		int size_r = 200000;
		Integer[] rndm = new Integer[size_r];
		for (int i=0; i < size_r; i++)
		    rndm[i] = (int) Math.random()*size_r*2;
		
		chain = new Singly<Integer>(rndm);
		// timing

		
		//long startTime = System.nanoTime();
		int size_s = Singly.length(chain);
		//long endTime = System.nanoTime();

		//System.out.print("(longueur "+size_s +" calculée en " + (endTime-startTime)/1000000.0 + " ms) : ");
		assert (size_s == size_r) : "\nerreur : length(l) vaut "+size_s+" or la réponse attendue est "+ size_r +"\n";

		return true;
	}
	
	
	// Test split
	public static <E> void TestSplit(E[] data, E[] first, E[] second) {
		Singly<E> origin, chain1, chain2, answer1, answer2;
		chain1 = data!=null?new Singly<E>(data):null;
		origin = data!=null?new Singly<E>(data):null;
		answer1 = first!=null?new Singly<E>(first):null;
		answer2 = second!=null?new Singly<E>(second):null;
		chain2 = Singly.split(chain1);
		assert (Singly.areEqual(chain1, answer1)) : "\nLa chaîne originale est\n"
				+ origin
				+ "\nLe segment initial devrait être\n"
				+ answer1 + "\nalors qu'il est\n"
				+ chain1;
		assert (Singly.areEqual(chain2, answer2)) : "\nLa chaîne originale est\n"
				+ origin + "\nLe segment final devrait être\n"
				+ answer2 + "\nalors qu'il est\n"
				+ chain2;
	}

	
	public static void main(String[] args) {
    
		//Pour s'assurer que les assert's sont activés
		if (!Test1.class.desiredAssertionStatus()) {
	        System.err.println("Vous devez activer globalement l'option -ea de la JVM");
	        System.err.println("Voir la rubirque \"Activer Assert\" au début du sujet");
	        System.exit(1);
	      }
    
		System.out.println("Question 1");
		System.out.println("Si vous avez programmé la méthode «length» récursivement, le test qui suit va déclencher java.lang.StackOverflowError.");
		System.out.print("Test de la méthode «length» ");
		TestLength();
		System.out.println("[OK]");

		
		System.out.print("Test de la méthode «split» : ");
		//TestSplit(null, null, null);
		TestSplit(new String[] { "one" }, new String[] { "one" },
				null);
		TestSplit(new String[] { "one", "two" }, new String[] { "one" },
				new String[] { "two" });
		TestSplit(new String[] { "one", "two", "three" }, new String[] {
				"one", "two" }, new String[] { "three" });
		TestSplit(new String[] { "one", "two", "three", "four" },
				new String[] { "one", "two" }, new String[] { "three", "four" });
		TestSplit(new String[] { "one", "two", "three", "four","five" },
				new String[] { "one", "two","three" }, new String[] {"four","five"});
		TestSplit(new String[] { "one", "two", "three", "four","five","six" },
				new String[] { "one", "two","three" }, new String[] {"four","five","six" });
		System.out.println("[OK]");
	}

}

