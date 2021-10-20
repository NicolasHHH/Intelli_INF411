
class StopEvent extends Event {

	StopEvent(Billiard billard, double time) {
		super(billard, time);
	}

	@Override
	void process() {
		billiard.eventQueue.clear();
	}

}
