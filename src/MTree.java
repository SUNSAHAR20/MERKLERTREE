import java.util.ArrayList;
//import MNode;

public class MTree {
	//private MNode merkleNode;
	private String nodeHash;
	private ArrayList<MNode> root;
	public MTree(String fileContent){
		String[] contents = fileContent.split("");
		ArrayList<MNode> childNodes = new ArrayList<MNode>();	
		for(String con : contents){
			MNode temp = new MNode();
			nodeHash = temp.getSHA(con);
			temp.hashValue=nodeHash;	
			childNodes.add(temp);
		}
		root = createTree(childNodes);
		System.out.println(root);
	}

    public ArrayList <MNode> createTree(ArrayList<MNode> cNodes) {
        if(cNodes.size() == 1){
        	return cNodes;
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
        	nodeIndex +=2;
        }while(nodeIndex<cNodes.size());
        return createTree(parents);
    }

    public MNode createParent(MNode rNode, MNode lNode){
    	MNode parentNode = new MNode();
    	String nodeHash = rNode.hashValue + lNode.hashValue;
    	String parentNodeHash = parentNode.getSHA(nodeHash);
    	parentNode.hashValue = parentNodeHash;
    	lNode.parent = parentNode;
    	rNode.parent = parentNode;
    	
    	System.out.printf("Left Child: ", lNode.hashValue);
    	System.out.printf(" Right Child: ", rNode.hashValue);
    	System.out.printf(" Parent: ", parentNode.hashValue);
    	return parentNode;
    }

 /*   private ArrayList<String> merkleTree(ArrayList<String> hashList){
        //Return the Merkle Root
        if(hashList.size() == 1){
            return hashList;
        }
        ArrayList<String> parentHashList=new ArrayList<>();
        //Hash the leaf transaction pair to get parent transaction
        int i=0;
        do{
            String hashedString = getSHA(hashList.get(i).concat(hashList.get(i+1)));
            parentHashList.add(hashedString);
            i+=2;
        }while(i<(hashList.size()-1));
        // If odd number of transactions , add the last transaction again
        if(hashList.size() % 2 == 1){
            String lastHash=hashList.get(hashList.size()-1);
            String hashedString = getSHA(lastHash.concat(lastHash));
            parentHashList.add(hashedString);
        }
        System.out.println(parentHashList);
        return merkleTree(parentHashList);
    }*/



	public static void main(String[] args) {
		String file = "SujinaSaddival";
		new MTree(file);
	}
}