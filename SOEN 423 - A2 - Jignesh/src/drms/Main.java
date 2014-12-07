package drms;

import replicaLayer.ReplicaServer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String replicaName = "replica2";
		ReplicaServer replica = new ReplicaServer(replicaName);
		replica.start();

	}

}
