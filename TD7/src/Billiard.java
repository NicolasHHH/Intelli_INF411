import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

//2 PROGRAMMATION DU BILLARD

public class Billiard {

	Vector<Ball> balls;
	double currentTime;
	PriorityQueue<Event> eventQueue;

	Billiard() {
		eventQueue = new PriorityQueue<Event>();
		balls = new Vector<Ball>();
	}

	// 2.1 LA BOUCLE DES ÉVÈNEMENTS
	
	void eventLoop() {
		// TODO (7-10 lignes)
		Event current;
		while(!eventQueue.isEmpty()) {
			current = eventQueue.poll();
			if (current.time >= currentTime && current.isValid()) {
				for(Ball b : balls) {
					b.forward(current.time - currentTime);
				}
				currentTime = current.time;
				current.process();
			}
		}
	}
	
	// 2.2 PRÉDICTION DES COLLISIONS

	void predictCollisions(Ball b) {
		// TODO (~10 lignes)
		double time; 
		for (Ball c : balls) {
			time = currentTime + b.nextBallCollision(c); 
			if(time > currentTime && !b.equals(c)) 
				eventQueue.add(new BallCollisionEvent(this, time, b, c));
		}
		time = currentTime + b.nextWallCollision(Direction.Horizontal); 
		if(time > currentTime)
			eventQueue.add(new WallCollisionEvent(this, time, b, Direction.Horizontal));
		
		time = currentTime + b.nextWallCollision(Direction.Vertical); 
		if(time > currentTime) 
			eventQueue.add(new WallCollisionEvent(this, time, b, Direction.Vertical));
		
	}

	void initialize() {
		// TODO 2 lignes
		for(Ball x : balls) {
			predictCollisions(x);
		}
	}

	void run() {
		initialize();
		eventLoop();
	}
	
	// Lit une configuration initiale depuis un fichier
	Billiard(String path) throws FileNotFoundException {
		this();

		Scanner input = new Scanner(new File(path));
		input.useLocale(Locale.ENGLISH);  // pour que java reconnaisse "1.2" plutôt que "1,2"

		input.nextInt();	// Le premier entier contient le nombre de lignes.
							// Mais comme on utilise un tableau redimensionnable, on l'ignore.
		while (input.hasNext()) {
			double x = input.nextDouble();
			double y = input.nextDouble();
			double vx = input.nextDouble();
			double vy = input.nextDouble();
			double radius = input.nextDouble();
			double mass = input.nextDouble();
			int r = input.nextInt();
			int g = input.nextInt();
			int b = input.nextInt();

			balls.add(new Ball(x, y, vx, vy, radius, mass, new Color(r, g, b)));
		}
	}
}
