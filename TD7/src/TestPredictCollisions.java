import java.awt.Color;

public class TestPredictCollisions {
	public static void main(String[] args) {
		CheckAssertions.check();

		System.out.print("Billiard.predictCollisions...    ");
		System.out.flush();

		Billiard b = new Billiard();

		// Sert à vérifier que les temps relatifs et absolus ne sont pas confondus.
		b.currentTime = 1.0;

		{
			// UNE BILLE
			Ball ball = new Ball(.5, .5, 0, 0, .01, 1, Color.BLACK);
			b.balls.add(ball);

			b.predictCollisions(ball);
			assert b.eventQueue
					.size() == 0 : "Avec une seule bille de vitesse nulle, il ne devrait pas y avoir de collision prévue";

			ball.vx = 1.0;
			b.predictCollisions(ball);
			assert b.eventQueue.size() == 1 : "Avec une seule bille se déplaçant horizontalement,"
					+ "on prévoit une seule collision.";

			Event e = b.eventQueue.poll();
			assert e instanceof WallCollisionEvent
					&& ((WallCollisionEvent) e).direction == Direction.Vertical : "Une collision avec un mur vertical devrait être prévue";

			ball.vy = 0.0001;
			b.predictCollisions(ball);
			assert b.eventQueue.size() == 2 : "Avec une seule bille se déplaçant en diagonale,"
					+ "on prévoit deux collisions (mur vertical et horizontal).";

			Event e1 = b.eventQueue.poll();
			assert e1 instanceof WallCollisionEvent
					&& ((WallCollisionEvent) e1).direction == Direction.Vertical : "Une collision avec un mur vertical devrait être prévue";

			Event e2 = b.eventQueue.poll();
			assert e2 instanceof WallCollisionEvent
					&& ((WallCollisionEvent) e2).direction == Direction.Horizontal : "Une collision avec un mur horizontal devrait être prévue";

			double etime = b.currentTime + ball.nextWallCollision(Direction.Vertical);
			assert e1.time != etime
					- b.currentTime : "Ne pas oublier d'ajouter currentTime dans la date prévue de l'évènement";
			assert e1.time == etime : "Calcul incorrect de l'heure de la collision";

			etime = b.currentTime + ball.nextWallCollision(Direction.Horizontal);
			assert e2.time != etime
					- b.currentTime : "Ne pas oublier d'ajouter currentTime dans la date prévue de l'évènement";
			assert e2.time == etime : "Calcul incorrect de l'heure de la collision";
		}

		{
			// TROIS BILLES (dont deux immobiles)
			b.balls.clear();

			Ball ball = new Ball(.5, .5, 1, 0.0001, .01, 1, Color.BLACK);
			b.balls.add(ball);
			b.balls.add(new Ball(.6, .5, 0, 0, .01, 1, Color.BLACK));
			b.balls.add(new Ball(.7, .5, 0, 0, .01, 1, Color.BLACK));

			b.predictCollisions(ball);
			assert b.eventQueue.size() == 4 : "Il faut prévoir les collisions avec toutes les autres"
					+ "billes et les quatres bandes, pas seulement la première collision";

			Event e = b.eventQueue.poll();
			assert e instanceof BallCollisionEvent : "La première collision devrait être avec une bille";

			assert e.time == b.currentTime
					+ ball.nextBallCollision(b.balls.get(1)) : "Mauvais calcul du moment de la collision";
		}

		System.out.println("OK");
		System.out.print("Billiard.initialize...           ");
		System.out.flush();
		
		// INITIALIZE

		{
			b.balls.clear();
			b.eventQueue.clear();

			Ball ball = new Ball(.5, .5, 1, 0.0001, .01, 1, Color.BLACK);
			b.balls.add(ball);
			b.balls.add(new Ball(.6, .5, 0, 0, .01, 1, Color.BLACK));
			b.balls.add(new Ball(.7, .5, -1, 0, .01, 1, Color.BLACK));
			b.initialize();

			int size = (2 + 2) // bille 1 : 2 collisions bille-bille, 2 collisions bille-bande
					+ 2 // bille 2 : 2 collisions bille-bille, 0 collisions bille-bande (immobile)
					+ (2 + 1); // bille 3 : 2 collisions bille-bille, 1 collision bille-bande (déplacement
								// horizontal)
			int sizewithunwantedoptimization = 6; // L'étudiant a peut-être essayé de ne pas dupliquer les collisions
													// bille-bille.

			assert b.eventQueue
					.size() != sizewithunwantedoptimization : "Avez-vous tenté de ne pas enregistrer une collisions A-B si B-A a déjà été enregistrée ?";

			assert b.eventQueue.size() == size : "Le nombre d'évènement prévu n'est pas correct";

		}

		System.out.println("OK");

	}
}
