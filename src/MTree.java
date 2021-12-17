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
		for (String con : contents) {
			nodeHash = getSHA(con);
			MNode merkleNode = new MNode(nodeHash);
			childNodes.add(merkleNode);
		}
		root = createTree(childNodes);
		System.out.println("Root: " + root.hashValue);
	}

	public MNode createTree(ArrayList<MNode> cNodes) {
		if (cNodes.size() == 1) {
			return cNodes.get(0);
		}

		ArrayList<MNode> parents = new ArrayList<MNode>();
		int nodeIndex = 0;
		MNode lNode;
		MNode rNode;

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

		System.out.println("Left Child: " + parentNode.lChild.hashValue);
		System.out.println("Right Child: " + parentNode.rChild.hashValue);
		System.out.println("Parent: " + lNode.parent.hashValue);
		System.out.println("---------------------------------");

		return parentNode;
	}

	public static String getSHA(String input) {
		try {

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
		ArrayList<VO> checker = new ArrayList<VO>();

		for (MNode cnode : childNodes) {
			if (cnode.hashValue.equals(checkNodeHash)) {
				System.out.println("Value Available in Server");
				checker = createVO(cnode, checker);
				return checker;
			}
		}

		return checker;
	}

	public ArrayList<VO> createVO(MNode node, ArrayList<VO> checker) {
		VO trialNode = new VO();

		if (node.hashValue == root.hashValue) {
			trialNode.trial_hash = node.hashValue;
			trialNode.flag = false;
			checker.add(trialNode);
			return checker;
		}

		if ((node.parent.lChild).equals(node)) {
			// int rIndex = checkNodes.indexOf(parentNode.rChild);
			trialNode.trial_hash = node.parent.rChild.hashValue;
			trialNode.flag = true;
			checker.add(trialNode);
		} else {
			// int lIndex = checkNodes.indexOf(parentNode.lChild);
			trialNode.trial_hash = node.parent.lChild.hashValue;
			trialNode.flag = false;
			checker.add(trialNode);
		}

		return createVO(node.parent, checker);
	}

	public static void main(String[] args) {
		String file = "sujinaa";
		new MTree(file);
//		String hashed_String = getSHA("a");
//		
//		ArrayList<VO> checkObject = txn.checkVOPossible(hashed_String);
//		
//		if(checkObject.size()>0){
//			//return the values
//			//verify function
//			String verifiedObject = hashed_String;
//			String rootValue = checkObject.get(checkObject.size()-1).trial_hash;
//			checkObject.remove(checkObject.size()-1);
//			for(VO object : checkObject){
//				String objectHash = object.trial_hash;
//				boolean objectFlag = object.flag;
//				if(!objectFlag){
//					verifiedObject = getSHA(verifiedObject + objectHash);
//				}
//				else{
//					verifiedObject = getSHA(objectHash + verifiedObject);
//				}
//			}
//			
//			System.out.println("VO: "+verifiedObject);
//			System.out.println("AR: "+rootValue);
//			
//			if(verifiedObject.equals(rootValue)){
//				System.out.println("Success");
//			}
//			else{
//			System.out.println("Failure");
//			}
//		}
//		else{
//			System.out.println("Object not found in tree");
//		}
	}
}