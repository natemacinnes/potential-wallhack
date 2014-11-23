package replicaLayer;

import java.util.Collection;
import java.util.HashMap;

public class ReplicaInformation {
	
	private HashMap<String,Integer> portList;
	
	public ReplicaInformation(){
		portList = new HashMap<String,Integer>();
		portList.put("replica1", 7777);
		portList.put("replica2", 7778);
		portList.put("replica3", 7779);
	}
	
	//get the port of a specific port
	public int getReplicaPort(String replicaName){
		return portList.get(replicaName);
	}
	
	public boolean isReplicaNameValid(String replicaName){
		return portList.containsKey(replicaName);
	}
	
	//get a collection of all servers ports
	public Collection<Integer> getReplicaPorts(){
		return portList.values();
	}

}
