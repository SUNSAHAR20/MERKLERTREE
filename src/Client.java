import java.util.ArrayList;

public class Client extends MTree {

	public Client(String fileContent) {
		super(fileContent);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String fileContent = args[0];
		MTree txn = new MTree(fileContent);

		String hashed_String = getSHA(args[1]);
		System.out.println("Client: <Started> Encrypted the transaction got a given file");

		ArrayList<VO> checkObject = txn.checkVOPossible(hashed_String);

		System.out.println("Client: <In Porgress> Verification object retrived");
		System.out.println();

		System.out.println("Client: <In Porgress> Performing audit proof for a given verification object");
		System.out.println();

		if (checkObject.size() > 0) {

			String verifiedObject = hashed_String;
			String rootValue = checkObject.get(checkObject.size() - 1).trial_hash;

			checkObject.remove(checkObject.size() - 1);

			for (VO object : checkObject) {
				String objectHash = object.trial_hash;
				boolean objectFlag = object.flag;
				if (!objectFlag) {
					verifiedObject = getSHA(verifiedObject + objectHash);
					System.out.println("Client: <Complete> Building the verification object for the left node ");
				} else {
					verifiedObject = getSHA(objectHash + verifiedObject);
					System.out.println("Client: <Complete> Building the verification object for the right node ");
				}
				System.out.println("---------------------------------------------------------------------------------");
			}
			System.out.println();
			System.out.println("Client: <Complete> Verification object is: " + verifiedObject);
			System.out.println("Client: <Complete> Root node is: " + rootValue);
			System.out.println("---------------------------------------------------------------------------------");
			
			if (verifiedObject.equals(rootValue)) {
				System.out.println("Client: <Complete> The verification object matchs with root node");
			} else {
				System.out.println("Client: <Complete> Given verification object is not valid");
			}
		} else {
			System.out.println("Client: <Complete> Given verification object doest not exist in a tree");
		}
	}

}