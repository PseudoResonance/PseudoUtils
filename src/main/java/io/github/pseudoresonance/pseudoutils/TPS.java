package io.github.pseudoresonance.pseudoutils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class TPS {

	private static long lastPoll = System.nanoTime();
	private final static LinkedList<Double> history = new LinkedList<Double>();
	private static long tickInterval = 20;
	private static long historyMaxSize = 15;

	private static int taskID;

	private static final DecimalFormat df = new DecimalFormat("#.##");

	public static void startTps() {
		df.setRoundingMode(RoundingMode.HALF_UP);
		tickInterval = Config.tpsUpdateFrequency;
		historyMaxSize = Config.tpsHistorySize;
		taskID = PseudoUtils.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(PseudoUtils.plugin, new Runnable() {
			@Override
			public void run() {
				final long startTime = System.nanoTime();
				long timeSpent = (startTime - lastPoll);
				if (timeSpent == 0) {
					timeSpent = 1;
				}
				double tps = tickInterval * 1000000000.0 / timeSpent;
				if (tps <= 21) {
					history.add(tps);
				}
				if (history.size() > historyMaxSize) {
					history.remove();
				}
				lastPoll = startTime;
			}
		}, tickInterval, tickInterval);
	}

	public static void stopTps() {
		PseudoUtils.plugin.getServer().getScheduler().cancelTask(taskID);
	}

	public static double getTps() {
		if (history.size() > 0) {
			double avg = 0;
			for (Double d : history) {
				if (d != null) {
					avg += d;
				}
			}
			double round = avg / history.size();
			double end = Double.valueOf(df.format(round));
			if (end > 20)
				end = Double.valueOf(df.format(20d));
			return end;
		} else {
			return Double.valueOf(df.format(20d));
		}
	}

}
