
class Event implements Comparable<Event> {
	Billiard billiard;
	final double time;

	Event(Billiard billiard, double time) {
		this.billiard = billiard;
		this.time = time;
	}

	public int compareTo(Event e) {
		return Double.compare(this.time, e.time);
	}

	boolean isValid() {
		// Par défaut, un évènement n'est pas caduc
		return true;
	}

	void process() {
		// Ne fait rien
	}
}
