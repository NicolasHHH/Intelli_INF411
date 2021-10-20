/* TD8. Tri fusion en place et généricité 
 * Ce fichier contient 5 classes:
 * 	- Singly<E> : listes chaînées génériques,
 * 	- MergeSortString : algorithme merge-sort pour les listes (chaînées) de chaînes de caractères,
 * 	- Occurrence : comptage des mots d'un texte,
 *  - MergeSort : algorithme merge-sort générique (on remplace le type «String» par le type générique «E»),
 *  - Median : calcul de la médiane d'un ensemble de valeurs numériques
 */

/* Remarque : ne sont declarées «public» que les constructeurs, ainsi que les méthodes dont la 
 * visibilité ne peut pas être réduite, ici toString et compareTo.
 */

// SINGLY 
 
class Singly<E> {
	E element;
	Singly<E> next;

	// On choisit de représenter la liste vide par null, les constructeurs ne
	// peuvent donc construire que des listes non vides.

	public Singly(E element, Singly<E> next) {
		this.element = element;
		this.next = next;
	}

	public Singly(E[] data) {
		assert (data.length > 0) : "\nLe constructeur Singly(E[] data) ne peut être utilisé avec un tableau vide"
				+ "\ncar on ne peut pas construire une liste non vide sans données.";
		this.element = data[0];
		this.next = null;
		Singly<E> cursor = this;
		for (int i = 1; i < data.length; i++) {
			cursor.next = new Singly<E>(data[i], null);
			cursor = cursor.next;
		}
		;
	}

	static <E> Singly<E> copy(Singly<E> l) {
		if (l == null)
			return null;
		Singly<E> res = new Singly<E>(l.element, l.next);
		Singly<E> cursor = res;
		while (l.next != null) {
			l = l.next;
			cursor.next = new Singly<E>(l.element, l.next);
			cursor = cursor.next;
		}
		return res;
	}

	public static <E> boolean areEqual(Singly<E> chain1, Singly<E> chain2) {
		while (chain1 != null && chain2 != null) {
			if (!chain1.element.equals(chain2.element))
				return false;
			chain1 = chain1.next;
			chain2 = chain2.next;
		}
		return chain1 == chain2;
	}
	
	public String toString() {
		Singly<E> cursor = this;
		String answer = "[ ";
		while (cursor != null) {
			answer = answer + (cursor.element).toString() + " ";
			cursor = cursor.next;
		}
		answer = answer + "]";
		return answer;
	}

	static<E> int length(Singly<E> l) {
		int res = 0;
		while(l != null) {
			l = l.next;
			res++;
		}
		return res;
	}
	
	static<E> Singly<E> split(Singly<E> l) {
		int n = length(l);
		if (n <= 1) return null;
		Singly<E> current = l;
		for (int i = 0; i < (n-1)/2; i++) {
			current = current.next;
		}
		Singly<E> res = current.next;
		current.next = null;
		return res;
	}
}

/* MERGE_SORT_STRING */

class MergeSortString {

	static Singly<String> merge(Singly<String> l1, Singly<String> l2) {
		if (l1 == null) return l2;
		if (l2 == null) return l1;
		Singly<String> accu;
		if (l1.element.compareTo(l2.element) <= 0) {
			accu = l1; 
			l1 = l1.next; //supprime le premier élément de l1
		} else {
			accu = l2; 
			l2 = l2.next; //supprime le premier élément de l2
		}
		accu.next = null; //accu ne contient plus qu'un seul élément
		Singly<String> last = accu;
		while (l1 != null && l2 != null) {
			if (l1.element.compareTo(l2.element) <= 0) {
				last.next = l1; //ajoute le premier éléménet de l1 à la fin de l'accumulateur
				last = last.next; //pointe le dernier élément de l'accumulateur
				l1 = l1.next; //supprime le premier élément de l1
				 
			} else {
				last.next = l2; l2 = l2.next; last = last.next;
			}
		}
		if (l1 != null) last.next = l1;
		if (l2 != null) last.next = l2;
		return accu;
	}

	static Singly<String> sort(Singly<String> l) {
		if (l == null || l.next == null) return l;
		return merge(sort(Singly.split(l)), sort(l));
	}

}

/* OCCURRENCE */

class Occurrence implements Comparable<Occurrence> {
	String word;
	int count;

	Occurrence(String word, int count) {
		this.word = word;
		this.count = count;
	}

	public String toString() {
		return word;
	}
	
	static Singly<Occurrence> count(Singly<String> l) {
		l = MergeSortString.sort(l);
		if (l == null) return null;
		Singly<Occurrence> res = null;
		while(l != null) {
			Occurrence occ = new Occurrence(l.element, 1);
			while (l.next != null && l.next.element.equals(occ.word)) {
				occ.count++;
				l = l.next;
			}
			res = new Singly<Occurrence>(occ, res);
			l = l.next;
		}
		return res;
	}
	
	public int compareTo(Occurrence that) {
		int diff = that.count - this.count;
		if (diff != 0)
			return diff;
		return this.word.compareTo(that.word);
	}
	
	static Singly<Occurrence> sortedCount(Singly<String> l) {
		return MergeSort.sort(count(l));
	}
}

/* MERGE_SORT */

// Version générique de MergeSortString
// On remplace le type «String» par le type générique «E» dans l'implémentation de MergeSort


class MergeSort {
	
	static<E extends Comparable<E>> Singly<E> merge(Singly<E> l1, Singly<E> l2) {
		if (l1 == null) return l2;
		if (l2 == null) return l1;
		Singly<E> accu;
		if (l1.element.compareTo(l2.element) <= 0) {
			accu = l1; l1 = l1.next;
		} else {
			accu = l2; l2 = l2.next;
		}
		accu.next = null;
		Singly<E> last = accu;
		while (l1 != null && l2 != null) {
			if (l1.element.compareTo(l2.element) <= 0) {
				last.next = l1; l1 = l1.next; last = last.next; 
			} else {
				last.next = l2; l2 = l2.next; last = last.next; 
			}
		}
		if (l1 != null) last.next = l1;
		if (l2 != null) last.next = l2;
		return accu;
	}
	
	static<E extends Comparable<E>> Singly<E> sort(Singly<E> l) {
		if (l == null || l.next == null) return l;
		return merge(sort(Singly.split(l)), sort(l));
	}

}

/* MEDIAN */


class Median {

	static Pair<Double> median (Singly<Double> data) {
		int n = Singly.length(data);
		if (n == 0) return new Pair<Double>(Double.NaN, Double.NaN);
		if (n == 1) return new Pair<Double>(data.element, data.element);
		data = MergeSort.sort(data);
		for (int i = 1; i < n / 2; i++) data = data.next;
		if (n % 2 == 0) return new Pair<Double>(data.element, data.next.element);
		return new Pair<Double>(data.next.element, data.next.element);
	}
}
