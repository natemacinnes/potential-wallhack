package testReplicaLayer;

import replicaLayer.ReplicaServer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String replicaName = "replica1";
		ReplicaServer replica = new ReplicaServer(replicaName);
		replica.start();

	}

}
