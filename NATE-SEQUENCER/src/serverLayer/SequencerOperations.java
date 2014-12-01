package serverLayer;

// interface for between sequencer and clients
public interface SequencerOperations {
	boolean sendSequenceMulticast(String msg);
	String receiveMessage(String msg);
}
