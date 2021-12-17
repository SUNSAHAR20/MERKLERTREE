public class MNode {
	String hashValue;
	MNode parent;
	MNode lChild;
	MNode rChild;
	public MNode(String hash) {
		hashValue=hash;
		parent=null;
		lChild=null;
		rChild=null;
	}
}