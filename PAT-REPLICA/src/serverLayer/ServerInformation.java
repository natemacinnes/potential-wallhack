package serverLayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import serverException.ItemNotFoundException;

public class ServerInformation {
	
	private HashMap<String,Integer> portList;
	
	public ServerInformation(){
		portList = new HashMap<String,Integer>();
		portList.put("concordia", 6789);
		portList.put("mcgill", 6790);
		portList.put("uqam", 6791);
	}
	
	//get the port of a specific port
	public int getServerPort(String serverName){
		return portList.get(serverName);
	}
	
	public boolean isServerNameValid(String serverName){
		return portList.containsKey(serverName);
	}
	
	//get a collection of all servers ports
	public Collection<Integer> getServersPorts(){
		return portList.values();
	}
	
	//get a collection of all servers ports
	public Collection<String> getServersNames(){
		return portList.keySet();
	}

}
