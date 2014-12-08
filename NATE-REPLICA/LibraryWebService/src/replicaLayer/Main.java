package replicaLayer;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String replicaName = "replica3";
		ReplicaServer replica = new ReplicaServer(replicaName);
		replica.start();
	}

}
