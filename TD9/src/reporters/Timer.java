package reporters;


import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

/**
 * Mesure le temps d'exploration
 */
public class Timer extends TestReporter implements OceanReporter {
	public Timer(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	private Instant start;
	private Instant end;
	private long duration;

	@Override
	public void initialise(Ocean ocean) {	
		super.initialise(ocean);
		start = Instant.now();
	}

	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
	}

	@Override
	public void finish() {
		end = Instant.now();
		duration = Duration.between(start, end).toMillis();
		sb.append("Durée de l'exploration : " + duration + " ms");		
		putData(new Long (duration));
		super.finish();
	}
	
	@Override
	public Long getData(String oceanName) {
		Object timer = super.getData(oceanName);
		return (timer instanceof Long) ? (Long) timer : null;
	}
}
