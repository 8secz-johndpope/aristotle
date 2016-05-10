package com.aristotle.task.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;

/**
 * This is a Storm util class to choose how to run the topology
 * 
 * @author Ravi
 * @data Jun 14, 2015
 */
public class TopologyRunner {

	private static final int MILLIS_IN_SEC = 1000;

	private TopologyRunner() {}

	/**
	 * Run the topology in a local mock cluster until user interruption
	 * @param topology
	 * @param topologyName
	 * @param conf
	 */
	public static void runTopologyLocally(StormTopology topology, String topologyName, Config conf) {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(topologyName, conf, topology);
	}

	/**
	 * Run the topology in a local mock cluster until user interruption
	 * @param topology
	 * @param topologyName
	 * @param conf
	 * @param runtimeInSeconds
	 * @throws InterruptedException
	 */
	public static void runTopologyLocallyWithShutdown(StormTopology topology, String topologyName, Config conf, int runtimeInSeconds) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(topologyName, conf, topology);
		Thread.sleep((long) runtimeInSeconds * MILLIS_IN_SEC);
		cluster.killTopology(topologyName);
		cluster.shutdown();
	}

	/**
	 * Run the toplogy in a remote cluster
	 * @param topology
	 * @param topologyName
	 * @param conf
	 * @throws AlreadyAliveException
	 * @throws InvalidTopologyException
	 */
	public static void runTopologyRemotely(StormTopology topology, String topologyName, Config conf) throws AlreadyAliveException, InvalidTopologyException {
		StormSubmitter.submitTopology(topologyName, conf, topology);
	}
}
