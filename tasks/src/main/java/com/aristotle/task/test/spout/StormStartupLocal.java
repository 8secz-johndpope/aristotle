package com.aristotle.task.test.spout;

import backtype.storm.Config;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.SpoutDeclarer;
import backtype.storm.topology.TopologyBuilder;

import com.aristotle.task.topology.TopologyRunner;

public class StormStartupLocal {

	public static void main(String[] args) throws Exception {
        String topologyName = "Tasks";
        System.out.println("Toplogy Name to Start " + topologyName);
        
		System.out.println("Creating Context");
		
		StormTopology stormTopology = buildTopology();
        startTopologyLocally(stormTopology);

    }

    public static void startTopologyLocally(StormTopology stormTopology) throws Exception {
        Config config = new Config();
        config.setNumWorkers(2);
        config.setMaxTaskParallelism(2);
        // TopologyRunner.runTopologyLocally(stormTopology, "eswaraj-topology", config);
        TopologyRunner.runTopologyRemotely(stormTopology, "tasks", config);
    }

    public static StormTopology buildTopology() {

        TopologyBuilder builder = new TopologyBuilder();
        // Create a Multiple Tree to print in logs
        System.out.println("Creating Spouts ");
        IRichSpout oneSpout = new OneSpout();
        SpoutDeclarer sd = builder.setSpout("oneSpout", oneSpout, 1);
        BoltDeclarer boltDeclarer;

        IRichBolt oneBolt = new OneBolt();
        boltDeclarer = builder.setBolt("OneBolt", oneBolt, 1);
        boltDeclarer.shuffleGrouping("oneSpout", "TestStream");
        return builder.createTopology();
    }

}
