
public class Test42 {

	// teste la méthode stats
	static private void testStats(int nbVals){
		System.out.println("Jeu de cartes avec "+nbVals+" valeurs");
		Battle.stats(nbVals, 1000);
		System.out.println("");
	}
	
	public static void main(String[] args) {
		testStats(11);
		testStats(12);
		testStats(13);
	}

}
