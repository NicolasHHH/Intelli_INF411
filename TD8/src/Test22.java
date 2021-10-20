
public class Test22 {
	
	public static void merge(String[] input1, String[] input2, String[] expected) {
		Singly<String> chain1 = input1!=null?new Singly<String>(input1):null;
		Singly<String> chain2 = input2!=null?new Singly<String>(input2):null;
		Singly<String> expandable_chain1 = input1!=null?new Singly<String>(input1):null;
		Singly<String> expandable_chain2 = input2!=null?new Singly<String>(input2):null;
		Singly<String> answer = expected!=null?new Singly<String>(expected):null;
		Singly<String> output = MergeSortString.merge(expandable_chain1, expandable_chain2);
		assert (Singly.areEqual(output, answer)) : "\n"
				+ "chain1 =\n"
				+ chain1
				+ "\n"
				+ "chain2 =\n"
				+ chain2
				+ "\n"
				+ "la fusion des deux chaînes est\n"
				+ answer
				+ "\nalors que votre implémentation de merge renvoie\n"
				+ output
				;
	}

	public static void sort(String[] input, String[] expected) {
		Singly<String> chain = input!=null? new Singly<String>(input):null;
		Singly<String> expandable_chain = input!=null? new Singly<String>(input):null;
		Singly<String> answer = expected!=null? new Singly<String>(expected):null;
		Singly<String> output = MergeSortString.sort(expandable_chain);
		assert (Singly.areEqual(output, answer)) : "\n" + "La chaîne entrée est\n"
				+ chain + "\n" 
				+ "la chaîne triée est\n"
				+ answer + "\n"
				+ "alors que votre implémentation de sort renvoie\n" + output + "\n";
	}
	
	public static void main(String[] args) {

		//Pour s'assurer que les assert's sont actives
		if (!Test22.class.desiredAssertionStatus()) {
	        System.err.println("Vous devez activer l'option -ea de la JVM");
	        System.err.println("Pour une modification globale, voir la rubirque \"Activer Assert\" au début du sujet");
	        System.err.println("Pour une modification locale : Run As -> Run configurations -> Arguments -> VM Arguments");
	        System.exit(1);
	      }
		
		System.out.println("Question 2.2");
		
		// Test de la méthode merge
		
		System.out.print("Test de la méthode merge: ");
		merge(null, null, null);
		merge(new String[] { "Zoé" }, new String[] { "Albert" },
				new String[] { "Albert", "Zoé" });
		merge(null, new String[] { "Albert", "Zoé" },
				new String[] { "Albert", "Zoé" });
		merge(new String[] { "Albert", "Zoé" }, null,
				new String[] { "Albert", "Zoé" });
		merge(new String[] { "banane", "cerise", "citron", "datte",
				"figue", "grenade", "pamplemousse", "prune" }, new String[] {
				"abricot", "cerise", "grenade", "pomme" },
				new String[] { "abricot", "banane", "cerise", "cerise",
						"citron", "datte", "figue", "grenade", "grenade",
						"pamplemousse", "pomme", "prune" });
		System.out.println("[OK]");
		
		// Test de la méthode sort
		
		System.out.print("Test de la méthode sort: ");
		sort(null, null);
		String[] input = new String[] { "OK", "KO" };
		String[] output = new String[] { "KO", "OK" };
		sort(input, output);
		input = new String[] { "ga", "bu", "zo", "ga", "zo", "ga", "bu", "ga",
				"meu", "bu" };
		output = new String[] { "bu", "bu", "bu", "ga", "ga", "ga", "ga",
				"meu", "zo", "zo" };
		sort(input, output);
		System.out.println("[OK]");

		
	}


}
