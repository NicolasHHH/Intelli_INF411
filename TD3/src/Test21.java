
public class Test21 {

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
	    }
		
		// tab_calls contient les premières valeurs des appels
		int[] tab_calls = new int[] { 200007, 100053, 600183, 500439, 600863, 701497, 602383, 103563, 5079, 106973,
				209287, 112063, 615343, 519169, 623583, 728627, 634343, 140773, 47959, 155943, 264767, 174473, 685103,
				596699, 709303, 822957, 737703, 253583, 170639, 288913, 408447, 329283, 851463, 775029, 900023, 26487,
				954463, 483993, 415119, 547883, 682327, 618493, 156423, 96159, 237743, 381217, 326623, 874003, 823399,
				974853, 128407, 84103, 641983, 602089, 764463, 529290, 951516, 375212, 400462 };

		System.out.print("Test de la méthode nextCall() ... ");
		Network network = new Network(1000000);
		for (int i = 0; i < tab_calls.length / 2; i++) {
			network.nextCall();
			int a = network.caller;
			int b = network.callee;
			// test numeros appels
			assert (a == tab_calls[2 * i] && b == tab_calls[2 * i + 1]) : "\nLe " + i + "ème appel devrait être entre "
					+ tab_calls[2 * i] + " et " + tab_calls[2 * i + 1] + " mais est entre " + a + " et " + b + ".";
			assert (network.relations.sameClass(a, b)) : "\nLes numéros N" + (2*i) + " = " + a + " et N" + (2*i+1) + " = " + b + " se sont appelés" 
					+ " et devraient être dans la même classe maintenant,\n" + "Avez-vous utilisé la méthode union ?";
		}
		System.out.println("[OK]");
	}
}
