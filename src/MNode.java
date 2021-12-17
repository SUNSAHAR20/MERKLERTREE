public class MNode {
	String hashValue;
	Object parent;
	Object lChild;
	Object rChild;
	public MNode(String hash) {
		hashValue=hash;
		parent=null;
		lChild=null;
		rChild=null;
	}
}