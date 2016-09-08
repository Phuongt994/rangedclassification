import tools.Scanners;

public class Starter {
	 
    public static void main(String[] args) {
    	// get start time
    	final long startTime = System.nanoTime();
    	
    	new Scanners();
    	
    	// get end time
    	// calculate duration
    	final long duration = System.nanoTime() - startTime;
    	final long durationInMillisecond = duration / 1000000;
    	final long durationInSecond = duration / 1000000000;
    	System.out.println("Execution time : " + durationInMillisecond + " milliseconds.");
    	System.out.println("Execution time : " + durationInSecond + " seconds.");
    	
    	// Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is " + memory + " in bytes");
        long kiloByte = 1024L;
        long memoryInKB = memory / kiloByte;
        System.out.println("Used memory is " + memoryInKB + " in kilobytes");
      }
    
}

