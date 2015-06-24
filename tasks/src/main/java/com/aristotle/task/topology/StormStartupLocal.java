package com.aristotle.task.topology;

import backtype.storm.generated.TopologySummary;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;
import org.apache.thrift7.TException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StormStartupLocal {

	public static void main(String[] args) throws Exception {
        String topologyName = "Local";
        System.out.println("Toplogy Name to Start " + topologyName);
        
		System.out.println("Creating Context");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("storm-topology.xml");
        
        killToplogyIfAlreadyRunning(topologyName);

        startToplogy(topologyName, applicationContext);

    }

    private static void startToplogy(String topologyName, ClassPathXmlApplicationContext applicationContext) throws Exception {
        Map<String, SpringTopology> allToplogies = applicationContext.getBeansOfType(SpringTopology.class);

        SpringTopology springEswarajTopologyToStart = null;
        for (Entry<String, SpringTopology> oneEntry : allToplogies.entrySet()) {
            if (oneEntry.getValue().getName().equals(topologyName)) {
                springEswarajTopologyToStart = oneEntry.getValue();
            }
        }
        if (springEswarajTopologyToStart == null) {
            throw new Exception("No Such topology [" + topologyName + "] defined in Spring context files");
        }
        springEswarajTopologyToStart.startTopologyLocally();
    }
    private static void killToplogyIfAlreadyRunning(String topologyName) throws TException {
        System.out.println("Utils.readStormConfig()=" + Utils.readStormConfig());
        NimbusClient nimbusClient = NimbusClient.getConfiguredClient(Utils.readStormConfig());
        List<TopologySummary> topologySummaries = nimbusClient.getClient().getClusterInfo().get_topologies();
        System.out.println("Already Running Toplogies");
        TopologySummary topologySummaryToKill = null;
        for (TopologySummary oneTopologySummary : topologySummaries) {
            System.out.println("Name : " + oneTopologySummary.get_name());
            System.out.println("     Id     : " + oneTopologySummary.get_id());
            System.out.println("     Status : " + oneTopologySummary.get_status());
            if (oneTopologySummary.get_name().equals(topologyName)) {
                topologySummaryToKill = oneTopologySummary;
            }

        }
        if (topologySummaryToKill == null) {
            System.out.println("No topology with name " + topologyName + " is running so no need to kill it");
            return;
        }
        if ("ACTIVE".equals(topologySummaryToKill.get_status())) {
            try {
                nimbusClient.getClient().killTopology(topologyName);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                // Sleep for 35 seconds
                try {
                    System.out.println("Wait for 35 seconds to topology with name " + topologyName + " get killed");
                    Thread.sleep(35000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
