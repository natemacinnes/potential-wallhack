package replicaStub;

public class TestClient {
	public static void main(String ars[]) {
		(new Thread(new SequencerClient())).start();
	}
}
