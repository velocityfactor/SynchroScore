package org.aquastarz.score;

import javax.persistence.EntityManager;

import org.apache.log4j.Level;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class AppSignalHandler extends Thread implements SignalHandler {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(SignalHandler.class);

	private SignalHandler oldHandler;

	public static void installAll() {
		try {
			install("SEGV");
		} catch (Exception e) {
			//nothing
		}
		try {
			install("ILL");
		} catch (Exception e) {
			//nothing
		}
		try {
			install("ABRT");
		} catch (Exception e) {
			//nothing
		}
		try {
			install("INT");
		} catch (Exception e) {
			//nothing
		}
		try {
			install("TERM");
		} catch (Exception e) {
			//nothing
		}
		// install("BREAK");
		// install("FPE");
	}

	// Static method to install the signal handler
	public static SignalHandler install(String signalName) {
		Signal diagSignal = new Signal(signalName);
		AppSignalHandler diagHandler = new AppSignalHandler();
		diagHandler.oldHandler = Signal.handle(diagSignal, diagHandler);
		return diagHandler;
	}

	// Signal handler method
	@Override
	public void handle(Signal sig) {
		logger.error("Signal handler received " + sig);
		try {
			// // Output information for each thread
			// Thread[] threadArray = new Thread[Thread.activeCount()];
			// int numThreads = Thread.enumerate(threadArray);
			// System.out.println("Current threads:");
			// for (int i=0; i < numThreads; i++) {
			// System.out.println(" "+threadArray[i]);
			// }

			// Chain back to previous handler, if one exists
			if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
				oldHandler.handle(sig);
			}

		} catch (Exception e) {
			logger.error("Signal handler failed, reason ", e);
		}
	}

	@Override
	public void run() {
		logger.setLevel(Level.INFO);
		ScoreApp.shutdownDb();
	}
}
