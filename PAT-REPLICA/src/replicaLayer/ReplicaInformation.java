package replicaLayer;

import java.util.Collection;
import java.util.HashMap;

public class ReplicaInformation {
	
	private HashMap<String,String> networkInfoList;
	private int sequencerPort;
	private int frontEndPort;
	private int replicaManagerPort;
	private String sequencerIp;
	private String frontEndIp;
	private String replicaManagerIp;
	
	
	public ReplicaInformation(){
		networkInfoList = new HashMap<String,String>();
		networkInfoList.put("replica1", "7777-127.0.0.1");
		networkInfoList.put("replica2", "7778-127.0.0.1");
		networkInfoList.put("replica3", "7779-127.0.0.1");
		sequencerPort = 7780;
		frontEndPort = 7781;
		replicaManagerPort = 7782;
		sequencerIp = "127.0.0.1";
		frontEndIp = "127.0.0.1";
		replicaManagerIp = "127.0.0.1";
	}
	
	//get the port of a specific port
	public int getReplicaPort(String replicaName){
		String networkInfo = networkInfoList.get(replicaName);
		String[] parts = networkInfo.split("-");
		int port = 0;
		try
		{
			port = Integer.parseInt(parts[0]);
			return port;
		}
		catch(NumberFormatException e) {}
		
		return port;
	}
	
	//get the port of a specific port
	public String getReplicaIp(String replicaName){
		String networkInfo = networkInfoList.get(replicaName);
		String[] parts = networkInfo.split("-");
		
		return parts[1];
	}
	
	public boolean isReplicaNameValid(String replicaName){
		return networkInfoList.containsKey(replicaName);
	}
	
	//get a collection of all servers ports
	public Collection<String> getReplicaName(){
		return networkInfoList.keySet();
	}
	
	public int getSequencerPort()
	{
		return sequencerPort;
	}
	
	public String getSequencerIp()
	{
		return sequencerIp;
	}

	public int getFrontEndPort() {
		return frontEndPort;
	}

	public int getReplicaManagerPort() {
		return replicaManagerPort;
	}

	public String getFrontEndIp() {
		return frontEndIp;
	}


	public String getReplicaManagerIp() {
		return replicaManagerIp;
	}
	

}
