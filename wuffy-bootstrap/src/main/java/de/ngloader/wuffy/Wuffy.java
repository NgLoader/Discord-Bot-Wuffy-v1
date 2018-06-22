package de.ngloader.wuffy;

public class Wuffy {

	public static final Long START_TIME_IN_MILLIS = System.currentTimeMillis();

	public static void main(String[] args) {
		var debug = false;
		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				debug = true;
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));
	}
}