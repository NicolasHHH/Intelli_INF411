import java.awt.Color;

class InvalidEvent extends Event {
	InvalidEvent(Billiard billiard, double time) {
		super(billiard, time);
	}

	@Override
	boolean isValid() {
		return false;
	}

	@Override
	void process() {
		assert false : "Un évènement caduc doit être ignoré";
	}
}

class CheckEvent extends Event {
	static int count = 0;
	static double last = 0.0;
	boolean processed = false;

	CheckEvent(Billiard billiard, double time) {
		super(billiard, time);
	}

	@Override
	boolean isValid() {
		return true;
	}

	@Override
	void process() {
		assert billiard.currentTime == time : "currentTime n'est pas égal à la date de l'évènement en cours de traitement";
		assert last <= time : "Les évènements sont mal ordonnés";
		assert !processed : "Un évènement ne doit être traité qu'une seule fois";
		processed = true;
		last = time;
		count++;
	}
}

class BallCheck extends Ball {
	BallCheck(double x, double y, double vx, double vy, double radius, double mass, Color color) {
		super(x, y, vx, vy, radius, mass, color);
	}

	@Override
	void forward(double t) {
		assert t < 1.5 : "Mauvaise utilisation de forward, "
				+ "la durée donnée doit être celle depuis le dernier évènement valide, pas celle depuis le début de la simulation.";
		super.forward(t);
	}
}

public class TestEventLoop {

	public static void main(String[] args) {
		CheckAssertions.check();
		
		System.out.print("Billiard.eventLoop...            ");
		System.out.flush();

		Billiard b = new Billiard();

		Ball ball = new BallCheck(0, 0, 0, 1, 0, 0, Color.BLACK);

		b.balls.add(ball);
		b.balls.add(new BallCheck(0, 2, 0, 0, 0, 0, Color.BLACK));

		for (int i = 0; i < 99; i++) {
			b.eventQueue.add(new CheckEvent(b, Math.random()));
			b.eventQueue.add(new InvalidEvent(b, Math.random()));
		}

		b.eventQueue.add(new CheckEvent(b, 2.0));
		b.eventQueue.add(new InvalidEvent(b, 3.0));

		b.eventLoop();
		assert CheckEvent.count == 100 : "Tous les évènements n'ont pas été traités";

		assert b.currentTime == 2.0 : "currentTime doit être mis à jour à la réception d'un évènement valide,"
				+ " et seulement dans ce cas.";

		assert ball.y != 3.0 : "Les billes ne doivent pas être déplacées à la réception d'un évènement invalide";

		assert ball.x == 0 && ball.y == 2.0 : "La boucle d'évènement doit gérer le mouvement des billes";

		System.out.println("OK");
	}
}
