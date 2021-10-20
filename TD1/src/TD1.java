
/* TD1. Bataille
 * Ce fichier contient deux classes :
 * 		- Deck représente un paquet de cartes,
 * 		- Battle représente un jeu de bataille.
 */

import java.util.LinkedList;

class Deck { // représente un paquet de cartes

	LinkedList<Integer> cards;

	// constructeur d'un paquet vide
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructeur à partir du champ
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructeur d'un paquet de cartes complet trié avec nbVals valeurs
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// copie le paquet de cartes
	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// chaîne de caractères représentant le paquet
	@Override
	public String toString() {
		return cards.toString();
	}

	// Question 1

	// prend une carte du paquet d pour la mettre à la fin du paquet courant
	int pick(Deck d) {
		if (d.cards.isEmpty()) {
			return -1;
		}
		else{
			int first = d.cards.pop();
			this.cards.addLast(first);
			return first;
		}
		
	}

	// prend toutes les cartes du paquet d pour les mettre à la fin du paquet courant
	void pickAll(Deck d) {
		while(!d.cards.isEmpty()) {
			this.cards.addLast(d.cards.removeFirst());
		}
	}

	// vérifie si le paquet courant est valide
	boolean isValid(int nbVals) {
		int[] count = new int[nbVals];
		for (int x : cards) {
			if ( x <1 || x>nbVals) {
				return false;
			}
			else {
				count[x-1] += 1;
				if (count[x-1]>4) { 
					return false;
				}
			}
		}
		return true;
	}

	// Question 2.1

	// choisit une position pour la coupe
	int cut() {
		int count = 0;
		for(int i = 0; i<this.cards.size();i++) {
			if (Math.random()>0.5)
				count ++;
		}
		return count; 
	}


	// coupe le paquet courant en deux au niveau de la position donnée par cut()
	Deck split() {
		int c = cut();
		Deck premier = new Deck();
		for(int i = 0;i<c;i++) {
			premier.cards.add(this.cards.pop());
		}
		return premier;
	}

	// Question 2.2

	// mélange le paquet courant et le paquet d
	void riffleWith(Deck d) {
		Deck f = new Deck();
		int size_d = d.cards.size();
		int size_c = this.cards.size();
		while (size_d > 0 || size_c > 0) {
			double ratio = (double)size_d/(double)(size_d+size_c);
			if (Math.random()>ratio) {
				f.pick(this);
				size_c --;
			}
			else{
				f.pick(d);
				size_d --;
			}
		}
		this.cards = f.cards;
 	}

	// Question 2.3

	// mélange le paquet courant au moyen du riffle shuffle
	void riffleShuffle(int m) {
		for(int i = 0; i<m;i++) {
			Deck premier = split();
			riffleWith(premier);
		}
	}
}

class Battle { // représente un jeu de bataille

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn;

	// constructeur d'une bataille sans cartes
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		this.turn = true;
	}
	
	// constructeur à partir des champs
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
		this.turn = true;
	}

	// copie la bataille
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		r.turn = this.turn;
		return r;
	}

	// chaîne de caractères représentant la bataille
	@Override
	public String toString() {
		return "Joueur 1 : " + player1.toString() + "\n" + "Joueur 2 : " + player2.toString() + "\nPli " + trick.toString();
	}

	// Question 3.1

	// constructeur d'une bataille avec un jeu de cartes de nbVals valeurs
	Battle(int nbVals) {
		Deck deck = new Deck(nbVals);
		deck.riffleShuffle(7);
		player1 = new Deck();
		for (int i = 0; i< nbVals*2 ; i++) {
			player1.pick(deck);
		}
		player2 = deck;
		trick = new Deck();
		this.turn = true;
		//System.out.print(player1.cards.size()+" "+player2.cards.size());
	}

	// Question 3.2

	// teste si la partie est terminée
	boolean isOver() {
		if (player1.cards.isEmpty() || player2.cards.isEmpty()) {
			return true;
		}
		return false;
	}

	// effectue un tour de jeu
	
	boolean oneRound() {
		if (isOver()) return false;
		int i = player1.cards.pop();
		int j = player2.cards.pop();
		
		if (i>j) {
			int k;
			while(!trick.cards.isEmpty()) {
				k = trick.cards.pop();
				player1.cards.add(k);
			}
			if (this.turn == false) {
				int t = i;
				i = j;
				j= t;
			}
			player1.cards.addLast(i);
			player1.cards.addLast(j);
			this.turn = !this.turn;
			return true;
		}
		else if (i<j) {
			int k;
			while(!trick.cards.isEmpty()) {
				k = trick.cards.pop();
				player2.cards.add(k);
			}
			if (this.turn == false) {
				int t = i;
				i = j;
				j= t;
			}
			player2.cards.addLast(i);
			player2.cards.addLast(j);
			this.turn = !this.turn;
			return true;
		}
		else {
			if (this.turn == false) {
				int t = i;
				i = j;
				j= t;
			}
			trick.cards.addLast(i);
			trick.cards.addLast(j);
			if (isOver()) return false;
			if (this.turn == false) {
				trick.cards.addLast(player1.cards.pop());
				trick.cards.addLast(player2.cards.pop());
			}
			else {
				trick.cards.addLast(player2.cards.pop());
				trick.cards.addLast(player1.cards.pop());
			}
			this.turn = !this.turn;
			return oneRound();
		}
	}
	

	// Question 3.3

	// renvoie le gagnant
	int winner() {
		int premier = player1.cards.size();
		int seconde = player2.cards.size();
		if (premier>seconde) {
			return 1;
		}
		else if (premier<seconde) {
			return 2;
		}
		else {
			return 0;
		}
	}

	// effectue une partie avec un nombre maximum de coups fixé
	int game(int turns) {
		int round = 0;
		while(round<turns && !isOver()) {
			round ++;
			if (oneRound()==false) {break;}
		}
		return winner();
	}

	// Question 4.1

	// effectue une partie sans limite de coups, mais avec détection des parties infinies
	int game() {
		Battle b2 = this.copy();
		while(!isOver()) {
			oneRound();
			b2.oneRound();
			if (isOver()) break;
			b2.oneRound();
			
			if( this.toString().equals(b2.toString())) return 3;
		}
		return winner();
		
	}

	// Question 4.2

	// effectue des statistiques sur le nombre de parties infinies
	static void stats(int nbVals, int nbGames) {
		for (int i =0; i<nbGames;i++) {
			Battle b = new Battle(nbVals);
			System.out.println(b.game());
		}
	}
}
