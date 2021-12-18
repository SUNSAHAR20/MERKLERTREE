import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MTree extends VO {
	private String nodeHash;
	private static MNode root;
	ArrayList<MNode> childNodes = new ArrayList<MNode>();
	ArrayList<MNode> checkNodes = new ArrayList<MNode>();

	public MTree(String fileContent) {

		String[] contents = fileContent.split("");
		System.out.println("Server: <In Progress> Spliting the given file content based on the charecter");
		System.out.println();

		for (String con : contents) {
			nodeHash = getSHA(con);
			System.out.println("Server: <In Progress> Generate unique SHA key for a charecter" + " - " + con);
			
			MNode merkleNode = new MNode(nodeHash);
			System.out.println("Server: <In Progress> Creating a node for a given SHA key");
			System.out.println("------------------------------------------------------------------------------");
			
			childNodes.add(merkleNode);
		}

		System.out.println("Server: <Complete> Created leaf nodes with hash value");
		System.out.println();
		
		System.out.println("Server: <In Progress> Creating a Merkle Tree");

		root = createTree(childNodes);
		
		System.out.println("Server: <Complete> Created root node");
		System.out.println("Server: <Complete> Hash value: " + root.hashValue);
		System.out.println();
		System.out.println("Server: <Complete> Merkle tree created successfully");
		System.out.println();
		
	}

	public MNode createTree(ArrayList<MNode> cNodes) {
		if (cNodes.size() == 1) {
			return cNodes.get(0);
		}

		ArrayList<MNode> parents = new ArrayList<MNode>();
		int nodeIndex = 0;
		MNode lNode;
		MNode rNode;

		System.out.println("Server: <In Progress> Adding child nodes to a Merkle Tree");
		System.out.println();
		
		do {
			lNode = cNodes.get(nodeIndex);
			if ((nodeIndex + 1) < cNodes.size())
				rNode = cNodes.get(nodeIndex + 1);

			else
				rNode = lNode;

			MNode checkNode = createParent(lNode, rNode);

			parents.add(checkNode);
			checkNodes.add(checkNode);
			
			nodeIndex += 2;
		} while (nodeIndex < cNodes.size());

		return createTree(parents);
	}

	public MNode createParent(MNode lNode, MNode rNode) {
		// MNode parentNode = new MNode();
		String nodeHash = rNode.hashValue + lNode.hashValue;
		String parentNodeHash = getSHA(nodeHash);
		MNode parentNode = new MNode(parentNodeHash);
		parentNode.lChild = lNode;
		parentNode.rChild = rNode;
		lNode.parent = rNode.parent = parentNode;

		System.out.println("Server: <Complete> Added left child node");
		System.out.println("Server: <Complete> Hash value: " + parentNode.lChild.hashValue);
		System.out.println();
		
		System.out.println("Server: <Complete> Added right  child node");
		System.out.println("Server: <Complete> Hash value: " + parentNode.rChild.hashValue);
		System.out.println();
		
		System.out.println("Server: <Complete> Created parent got the above child");
		System.out.println("Server: <Complete> Hash value of Parent: " + lNode.parent.hashValue);
		System.out.println("------------------------------------------------------------------------------");

		return parentNode;
	}

	public static String getSHA(String input) {
		try {
			System.out.println("Server: <In Progress> Encrypted the transaction");
			// Static getInstance method is called with hashing SHA
			MessageDigest hashedToken = MessageDigest.getInstance("SHA-1");
			byte[] hashedTokenArr = new byte[20];
			// digest() method called to calculate message digest of an input and return
			// array of byte
			hashedTokenArr = hashedToken.digest(input.getBytes());
			// Convert byte array into Numerical representation
			BigInteger hashedTokenNumeric = new BigInteger(1, hashedTokenArr);
			// Convert message digest into hex value
			String hashedTokenValue = hashedTokenNumeric.toString(16);

			while (hashedTokenValue.length() < 40) {
				hashedTokenValue = "0" + hashedTokenValue;
			}

			return hashedTokenValue;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown" + " for incorrect algorithm: " + e);
			return null;
		}
	}

	public ArrayList<VO> checkVOPossible(String checkNodeHash) {
		
		System.out.println("Server: <In Progress> Checking if leave exists and return the audit trail if it does");
		System.out.println();
		
		ArrayList<VO> checker = new ArrayList<VO>();

		for (MNode cnode : childNodes) {
			if (cnode.hashValue.equals(checkNodeHash)) {
				System.out.println("Server: <Complete> Leaf exists");
				System.out.println();
				
				System.out.println("Server: <In Progress> Create verification object");
				System.out.println();
				
				checker = createVO(cnode, checker);
				return checker;
			}
		}

		return checker;
	}

	public ArrayList<VO> createVO(MNode node, ArrayList<VO> checker) {
		VO trialNode = new VO();

		System.out.println("Server: <In Progress> Creating verification object in a bottom-up fashion");
		
		System.out.println("Server: <In Progress> Check if merkle node is root node");
		System.out.println();
		
		if (node.hashValue == root.hashValue) {
			trialNode.trial_hash = node.hashValue;
			trialNode.flag = false;
			System.out.println("Server: <Complete> Given Merkle node is root node");
			checker.add(trialNode);
			return checker;
		}
		
		System.out.println("Server: <Complete> Merkle node is not root node");
		System.out.println();
		
		System.out.println("Server: <In Progress> Check if merkle node is the left child or the right child");
		
		if ((node.parent.lChild).equals(node)) {
			trialNode.trial_hash = node.parent.rChild.hashValue;
			trialNode.flag = true;
			System.out.println("Server: <Complete> Since the current node is left node, right node is required for audit trail");
			System.out.println();
			
			checker.add(trialNode);
		} else {
			trialNode.trial_hash = node.parent.lChild.hashValue;
			trialNode.flag = false;
			System.out.println("Server: <Complete> Since the current node is right node, left node is required for audit trail");
			System.out.println();
			
			checker.add(trialNode);
		}
		
		System.out.println("------------------------------------------------------------------------------------------------");
		System.out.println("Server: <In Progress> Sending parent node as a child node");
		
		return createVO(node.parent, checker);
	}
}