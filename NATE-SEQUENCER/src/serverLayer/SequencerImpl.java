package serverLayer;

import java.util.HashMap;

import transportLayer.MulticastServer;
import transportLayer.UDPSequencerClient;
import transportLayer.UDPSequencerServer;

public class SequencerImpl implements SequencerInterface {
	
	private int frontEndPort = 4545;
	private int replicaPort = 4544;
	private static int seqNum = 1;
	private static MulticastServer ms;
	private UDPSequencerServer FEserver;
	private UDPSequencerServer Rserver;
	private HashMap<Integer, String> sentMessages = new HashMap<Integer, String>();
	
	int clientRequest;
	int msgSequence;
	
	public SequencerImpl () {
		System.setProperty("java.net.preferIPv4Stack" , "true"); // needed on OSX in order to prevent it from blocking the ipv6 address
		this.ms = new MulticastServer();
		startServers();
	}
	
	public SequencerImpl (int port, String group) {
		System.setProperty("java.net.preferIPv4Stack" , "true"); // needed on OSX in order to prevent it from blocking the ipv6 address
		this.ms = new MulticastServer(port, group);
		startServers();
	}
	
	
	@Override
	public void receiveMessage(String msg) {
		createSequence(msg);
	}
	
	
	@Override
	public void createSequence(String msg) {
		msg = seqNum +"."+ msg;
		sentMessages.put(seqNum, msg);
		ms.setMessage(msg);
		seqNum++;
		broadcastSequence();
	}
	
	@Override
	public void resendMessage(int seqNumber) {
	String msg = seqNumber + sentMessages.get(seqNumber);
	ms.setMessage(msg);
	broadcastSequence();	
	}	
	
	@Override
	public void broadcastSequence() {
		ms.run();
	}
	
	
	@Override
	public void startServers() {
		System.out.println("Starting servers...");
		FEserver = new UDPSequencerServer(this, frontEndPort);
		FEserver.start();
		Rserver = new UDPSequencerServer(this, replicaPort);
		Rserver.start();
	}
}
