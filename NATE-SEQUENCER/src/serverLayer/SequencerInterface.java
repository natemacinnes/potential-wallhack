package serverLayer;

public interface SequencerInterface {

	public abstract void receiveMessage(String msg);

	public abstract void createSequence(String msg);

	public abstract void broadcastSequence();

	public abstract void startServers();

}