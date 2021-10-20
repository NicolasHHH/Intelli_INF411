
import java.io.FileNotFoundException;
import java.util.HashMap;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Ocean;
import ocean.Traversal;
import reporters.TestReporter;

public class Test {
	protected static boolean test(
			String name, 
			TestReporter[] reporters, 
			Traversal traversal,
			HashMap<String, Object> data, 
			boolean canvas,
			boolean print
			) throws FileNotFoundException 
	{
		if (print)
			System.out.println("\n...sur le labyrinth '" + name + "'...");
		
		Ocean ocean = new Ocean(name, "data/" + name, BasicDirections.values()).reporters();
		if (canvas)
			ocean.add(new BasicOceanCanvas(name));
		
		for (TestReporter reporter : reporters)
			ocean.add(reporter);
		
		try {
			return ocean.exploreUsing(traversal);
		} 
		catch (StackOverflowError e) 
		{
			for (TestReporter reporter : reporters)
				reporter.finish();
			
			throw e;
		}
	}
}
