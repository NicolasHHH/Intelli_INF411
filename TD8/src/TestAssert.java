
public class TestAssert {

	public static void main(String[] args) {
		//Pour s'assurer que les assert's sont activés
		if (!TestAssert.class.desiredAssertionStatus()) {
	        System.err.println("Vous devez activer globalement l'option -ea de la JVM");
	        System.err.println("Voir la rubirque \"Activer Assert\" au debut du sujet");
	        System.exit(1);
	      }
		System.out.println("Les assert's sont actives.");
		
	}

}
