import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MTree {
	private String nodeHash;
	private MNode root;
	public MTree(String fileContent){
		String[] contents = fileContent.split("");
		ArrayList<MNode> childNodes = new ArrayList<MNode>();	
		for(String con : contents){
			nodeHash =  getSHA(con);
			MNode merkleNode = new MNode(nodeHash);	
			childNodes.add(merkleNode);
		}
		root = createTree(childNodes);
		System.out.println("Root: " +root.hashValue);
	}

    public MNode createTree(ArrayList<MNode> cNodes) {
        if(cNodes.size() == 1){
        	return cNodes.get(0);
        }
        ArrayList <MNode> parents = new ArrayList<MNode>();
        int nodeIndex=0;
        MNode lNode;
        MNode rNode;
        do{
        	lNode = cNodes.get(nodeIndex);
        	if((nodeIndex+1)<cNodes.size())
        		rNode = cNodes.get(nodeIndex+1);
        	else
        		rNode=lNode;
        	parents.add(createParent(lNode, rNode));       
        	nodeIndex += 2;
        }while(nodeIndex<cNodes.size());
        return createTree(parents);
    }

    public MNode createParent(MNode lNode, MNode rNode){
    	//MNode parentNode = new MNode();
    	String nodeHash = rNode.hashValue + lNode.hashValue;
    	String parentNodeHash = getSHA(nodeHash);
    	MNode parentNode = new MNode(parentNodeHash);
    	parentNode.lChild=lNode;
    	parentNode.rChild=rNode;
    	lNode.parent = rNode.parent = parentNode;
    	
    	System.out.println("Left Child: " + lNode.hashValue);
    	System.out.println("Right Child: " + rNode.hashValue);
    	System.out.println("Parent: " + parentNode.hashValue);
    	System.out.println("---------------------------------");
    	return parentNode;
    }
    public static String getSHA(String input)
    {
        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest hashedToken = MessageDigest.getInstance("SHA-1");
            byte[] hashedTokenArr = new byte[20];
            // digest() method called to calculate message digest of an input and return array of byte
            hashedTokenArr = hashedToken.digest(input.getBytes());
            // Convert byte array into Numerical representation
            BigInteger hashedTokenNumeric = new BigInteger(1, hashedTokenArr);
            // Convert message digest into hex value
            String hashedTokenValue = hashedTokenNumeric.toString(20);

	        while(hashedTokenValue.length() < 20) {
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

	public static void main(String[] args) {
		String file = "Sujina";
		new MTree(file);
	}
}