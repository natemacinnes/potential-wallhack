package libraryclient;

import replicaLayer.ReplicaServer;

public class Main {
	public static void main(String[] args) {
		String replicaName = "replica3";
		ReplicaServer replica = new ReplicaServer(replicaName);
		replica.start();
	}
}
