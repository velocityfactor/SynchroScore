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
		install("SEGV");
		install("ILL");
		//install("FPE");
		install("ABRT");
		install("INT");
		install("TERM");
		//install("BREAK");		
	}
	
	// Static method to install the signal handler
	public static SignalHandler install(String signalName) {
		Signal diagSignal = new Signal(signalName);
		AppSignalHandler diagHandler = new AppSignalHandler();
		diagHandler.oldHandler = Signal.handle(diagSignal, diagHandler);
		return diagHandler;
	}

	// Signal handler method
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
	
	public void run() {
		logger.setLevel(Level.INFO);
		logger.info("Shutdown");
                EntityManager em = ScoreApp.getEntityManager();
                em.getTransaction().begin();
	        em.createNativeQuery("SHUTDOWN").executeUpdate();
	        em.getTransaction().commit();
	        em.close();
	}
}
