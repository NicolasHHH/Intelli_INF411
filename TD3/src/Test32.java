
public class Test32 {

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test32.class.desiredAssertionStatus()) {
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

		System.out.print("Test de size et de la méthode getSize() ... ");
		Network network = new Network(1000000);
		for (int i = 0; i < tab_calls.length / 2; i++) {
			network.nextCall();
			int a = network.caller;
			int b = network.callee;
			// test de la mise à jour de union-find avec le champ size
			int sa = network.getSize(a);
			int sb = network.getSize(b);
			assert (sa == 2) : "\nAprès le " + i + "ème appel, " + a + " et " + b
					+ " sont en relation et getSize(" + a + ") devrait renvoyer 2, mais a renvoyé " + sa + "\n";
			assert (sb == 2) : i + "ème appel, " + a + " et " + b
					+ " sont en relation et getSize(" + b + ") devrait renvoyer 2, mais a renvoyé " + sb + "\n";
		}
		System.out.println("[OK]");
	}
}
