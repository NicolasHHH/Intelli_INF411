import java.awt.Color;

class RedrawEvent extends Event {

	static int frame = 0;

	RedrawEvent(Billiard billiard, double time) {
		super(billiard, time);
	}

	static Color clearColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);

	@Override
	void process() {
		frame++;
		System.out.printf("t=%f\t(%d pending events)\n", billiard.currentTime, billiard.eventQueue.size());

		// StdDraw.clear();

		// Plutôt que d'effacer la frame précédente, on peut appliquer un calque
		// semi-opaque, cela permet de mieux voir le mouvement des billes.
		StdDraw.setPenColor(clearColor);
		StdDraw.filledSquare(0.5, 0.5, 0.5);

		for (Ball b : billiard.balls)
			b.draw();

		StdDraw.pause(10);

		StdDraw.show();

		billiard.eventQueue.add(new RedrawEvent(billiard, time + 2.));
	}
}
