import java.util.LinkedList;
import java.util.ListIterator;

public class Test22 {

	// teste si les listes l1 et l2 peuvent être melangées par le riffle pour donner l3 (sans modifier les deux listes)
	static boolean fromRiffle(LinkedList<Integer> l1, LinkedList<Integer> l2, LinkedList<Integer> l3) {
		// si l3 est vide, alors l2 et l3 doivent être vides
		if (l3.isEmpty()) return l1.isEmpty() && l2.isEmpty();
		// sinon, on va comparer le premier élément de l3 à ceux de l1 et l2
		// on retire donc le premier élément de l3, et on le rajoutera ensuite pour s'assurer de ne pas avoir modifié l3
		int x3 = l3.removeFirst();
		if (!l1.isEmpty()) {
			// on retire le premier élément de l1, et on le rajoutera ensuite pour s'assurer de ne pas avoir modifié l1
			int x1 = l1.removeFirst();
			// on teste si les premiers éléments de l1 et l3 coincident, et si le reste de l1 peut être mélangé avec l2 pour donner l3
			if (x1 == x3 && fromRiffle(l1, l2, l3)) {
				l1.addFirst(x1);		
				l3.addFirst(x3);
				return true;
			}
			l1.addFirst(x1);
		}
		if (!l2.isEmpty()) {
			// on retire le premier élément de l2, et on le rajoutera ensuite pour s'assurer de ne pas avoir modifié l2
			int x2 = l2.removeFirst();
			// on teste si les premiers éléments de l2 et l3 coincident, et si l1 peut être mélangée avec le reste de l2 pour donner l3
			if (x2 == x3 && fromRiffle(l1, l2, l3)) {
				l2.addFirst(x2);
				l3.addFirst(x3);
				return true;
			}
			l2.addFirst(x2);
		}
		l3.addFirst(x3);
		return false;
	}
	
	// teste si la liste l3 est formée de la liste l1 suivie de la liste l2
	static boolean prefix(LinkedList<Integer> l1, LinkedList<Integer> l2, LinkedList<Integer> l3) {
		ListIterator<Integer> i1 = l1.listIterator();
		ListIterator<Integer> i2 = l2.listIterator();
		ListIterator<Integer> i3 = l3.listIterator();
		// on commence par la liste l1
		// tant qu'on n'a pas lu toute la liste l1
		while(i1.hasNext())
			// on vérifie qu'on n'a pas lu toute la liste l3 et que les éléments suivants de l1 et l3 coincident
			if (!i3.hasNext() || i1.next() != i3.next()) return false;
		// on a maintenant lu toute la liste l1
		// on continue donc avec la liste l2
		// tant qu'on n'a pas lu toute la liste l2
		while(i2.hasNext())
			// on vérifie qu'on n'a pas lu toute la liste l3 et que les éléments suivants de l2 et l3 coincident
			if (!i3.hasNext() || i2.next() != i3.next()) return false;
		// on a maintenant lu toute la liste l2
		// on vérifie donc qu'on a aussi lu toute la liste l3
		return !i3.hasNext();
	}
	
	// teste la méthode riffleWith
	static void testRiffleWith(Deck d) {
		// effectue un split et riffle le résultat
		Deck e = d.split();
		Deck d1 = e.copy();
		Deck d2 = d.copy();
		String s1 = d1.toString();
		String s2 = d2.toString();
		d.riffleWith(e);
		assert(fromRiffle(d1.cards, d2.cards, d.cards)) : "\nPour les paquets d1 = " + s1 + " et d2 = " + s2 + ", l'appel de d1.riffleWith(d2) ne devrait pas donner " + d + ".";
		assert(!prefix(d1.cards, d2.cards, d.cards)) : "\nPour les paquets d1 = " + s1 + " et d2 = " + s2 + ", l'appel de d1.riffleWith(d2) a renvoyé " + d + ", en placant d1 devant d2. C'est possible mais peu probable. Recommencez le test.";
		assert(!prefix(d2.cards, d1.cards, d.cards)) : "\nPour les paquets d1 = " + s1 + " et d2 = " + s2 + ", l'appel de d1.riffleWith(d2) a renvoyé " + d + ", en placant d2 devant d1. C'est possible mais peu probable. Recommencez le test.";
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test22.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// tests de la méthode riffleWith
		System.out.print("Test de la méthode riffleWith ... ");
		for (int i = 0; i < 100; i++)
			testRiffleWith(new Deck(13));
		System.out.println("[OK]");
	}
}
