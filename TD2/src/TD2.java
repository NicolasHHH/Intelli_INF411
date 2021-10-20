
/* TD2. Fruits et tables de hachage
 * Ce fichier contient 7 classes:
 * 		- Row représente une ligne de fruits,
 * 		- CountConfigurationsNaive compte naïvement les configurations stables,
 * 		- Quadruple manipule des quadruplets,
 * 		- HashTable construit une table de hachage,
 * 		- CountConfigurationsHashTable compte les configurations stables en utilisant notre table de hachage,
 * 		- Triple manipule des triplets,
 * 		- CountConfigurationsHashMap compte les configurations stables en utilisant la HashMap de java.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // représente une ligne de fruits
	private final int[] fruits;

	// constructeur d'une ligne vide
	Row() {
		this.fruits = new int[0];
	}

	// constructeur à partir du champ fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// méthode equals pour comparer la ligne à un objet o
	@Override
	public boolean equals(Object o) {
		// on commence par transformer l'objet o en un objet de la classe Row
		// ici on suppose que o sera toujours de la classe Row
		Row that = (Row) o;
		// on vérifie que les deux lignes ont la meme longueur
		if (this.fruits.length != that.fruits.length)
			return false;
		// on vérifie que les ièmes fruits des deux lignes coincident
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// on a alors bien l'égalité des lignes
		return true;
	}

	// code de hachage de la ligne
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// chaîne de caracteres représentant la ligne
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// renvoie une nouvelle ligne en ajoutant fruit à la fin de la ligne
	Row addFruit(int fruit) {
		int len = this.fruits.length;
		int[] Case = new int[len+1];
		for (int i = 0; i< len;i++) {
			Case[i] = this.fruits[i];
		}
		Case[len] = fruit;
		Row newRow = new Row(Case);
		return newRow;
	}

	// renvoie la liste de toutes les lignes stables de largeur width
	static LinkedList<Row> allStableRows(int width) {
		LinkedList<Row> ls = new LinkedList<Row>();
		if (width == 0) {
			ls.add(new Row());
			return ls;
		}
		
		LinkedList<Row> last = allStableRows(width-1);
		for (Row row: last) {
			if (width < 3 || row.fruits[width - 2] != row.fruits[width - 3])
			{
				ls.add(row.addFruit(0));
				ls.add(row.addFruit(1));
			}
			else
			{
				int fruit = row.fruits[width - 2] == 0 ? 1 : 0;
				ls.add(row.addFruit(fruit));
			}
		}
		return ls;
		
	}

	// vérifie si la ligne peut s'empiler avec les lignes r1 et r2
	// sans avoir trois fruits du même type adjacents
	boolean areStackable(Row r1, Row r2) {
		int len = this.fruits.length;
		if (r1.fruits.length != len || r2.fruits.length != len) return false;
		for(int i=0; i<len;i++) {
			if (this.fruits[i] == r1.fruits[i] && r1.fruits[i]==r2.fruits[i] ) {
				return false;
			}
		}
		return true;
	}
}

// COMPTAGE NAIF
class CountConfigurationsNaive { // comptage des configurations stables

	// Question 2

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// récursive
		if (height<=1) return 0;
		else if (height==2) return 1;
		else {
			int somme = 0;
			for (Row r3 : rows) {
				if(r3.areStackable(r1, r2)) {
					somme += count(r2, r3, rows, height-1);
				}
			}
			return somme;
		}
		
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
			LinkedList<Row> all = Row.allStableRows(n);
			long allAmount = 0;
			if (n == 0)
				return 1;
			else if (n == 1)
				return 2;
			else
				for (Row i : all)
					for (Row j : all)
						allAmount += count(i, j, all, n);
			return allAmount;
	}
}

// CONSTRUCTION ET UTILISATION D'UNE TABLE DE HACHAGE

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // table de hachage
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructeur
	HashTable() {
		this.buckets = new Vector<LinkedList<Quadruple>>(M);
		for (int i = 0; i<M;i++) {
			this.buckets.add(new LinkedList<Quadruple>());
		}
	}

	// Question 3.2

	// renvoie le code de hachage du triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		return r1.hashCode()*r2.hashCode() + 
				r2.hashCode()*17*17+
				height*height*31*17+
				r1.hashCode()*29+
				r1.hashCode()*r1.hashCode()*r1.hashCode();
	}

	// renvoie le seau du triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		int res = hashCode(r1,r2,height)%M;
		if (res <0 ) {
			res += M;
		}
		else if (res >M) {
			res -= M;
		}
		return res;
	}

	// Question 3.3

	// ajoute le quadruplet (r1, r2, height, result) dans le seau indiqué par la
	// methode bucket
	void add(Row r1, Row r2, int height, long result) {
		int hash = bucket(r1,r2,height);
		buckets.get(hash).add(new Quadruple(r1, r2, height,result));
	}

	// Question 3.4

	// recherche dans la table une entrée pour le triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		int hash = bucket(r1,r2,height);
		LinkedList<Quadruple> Ls = buckets.get(hash);
		if (Ls.size()==0) return null;
		for ( Quadruple ad : Ls) {
			if (ad.r1.equals(r1) && ad.r2.equals(r2) && height == ad.height)
				return Long.valueOf(ad.result);
		}
		return null;
	}

}

class CountConfigurationsHashTable { // comptage des configurations stables en utilisant notre table de hachage
	static HashTable memo = new HashTable();

	// Question 4

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant notre table de hachage
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		
		if (height<=1) return 0;
		else if (height==2) return 1;
		else {
			if (memo.find(r1, r2, height)!=null) return memo.find(r1, r2, height);
			else {
				long somme = 0;
				long temp = 0;
				for (Row r3 : rows) {
					if(r3.areStackable(r1, r2)) {
						temp = count(r2, r3, rows, height-1);
						somme += temp;
						memo.add(r2, r3, height-1, temp);
					}
				}
				return somme; 
			}
			
		}
		
	}

	// renvoie le nombre de grilles a n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> all = Row.allStableRows(n);
		long allAmount = 0;
		if (n == 0)
			return 1;
		else if (n == 1)
			return 2;
		else
			for (Row i : all)
				for (Row j : all)
					allAmount += count(i, j, all, n);
		return allAmount;
	}
}

//UTILISATION DE HASHMAP

class Triple { // triplet (r1, r2, height)
	// à compléter
	Row r1;
	Row r2;
	int height;

	Triple(Row r1, Row r2, int height) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}
	@Override
	public boolean equals(Object o) {
		Triple tp = (Triple)o;
		return this.r1.equals(tp.r1) &&
		this.r2.equals(tp.r2) &&
		this.height == tp.height; 
	}
	@Override
	public int hashCode() {
		return HashTable.hashCode(r1, r2, height);
	}
}

class CountConfigurationsHashMap { // comptage des configurations stables en utilisant la HashMap de java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant la HashMap de java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if (height<=1) return 0;
		else if (height==2) return 1;
		else {
			Triple tp = new Triple(r1, r2, height);
			if (memo.get(tp)!=null) return memo.get(tp);
			else {
				long somme = 0;
				long temp = 0;
				for (Row r3 : rows) {
					if(r3.areStackable(r1, r2)) {
						temp = count(r2, r3, rows, height-1);
						somme += temp;
						memo.put(new Triple(r2, r3, height-1), temp);
					}
				}
				return somme; 
			}
		}
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> all = Row.allStableRows(n);
		long allAmount = 0;
		if (n == 0)
			return 1;
		else if (n == 1)
			return 2;
		else
			for (Row i : all)
				for (Row j : all)
					allAmount += count(i, j, all, n);
		return allAmount;
	}
}
