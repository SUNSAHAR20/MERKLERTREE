import java.util.ArrayList;

public class Client extends MTree {

	public Client(String fileContent) {
		super(fileContent);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String file = args[0];
		MTree txn = new MTree(file);

		String hashed_String = getSHA(args[1]);

		ArrayList<VO> checkObject = txn.checkVOPossible(hashed_String);

		if (checkObject.size() > 0) {
			// return the values
			// verify function
			String verifiedObject = hashed_String;
			String rootValue = checkObject.get(checkObject.size() - 1).trial_hash;
			checkObject.remove(checkObject.size() - 1);
			for (VO object : checkObject) {
				String objectHash = object.trial_hash;
				boolean objectFlag = object.flag;
				if (!objectFlag) {
					verifiedObject = getSHA(verifiedObject + objectHash);
				} else {
					verifiedObject = getSHA(objectHash + verifiedObject);
				}
			}

			System.out.println("VO: " + verifiedObject);
			System.out.println("AR: " + rootValue);

			if (verifiedObject.equals(rootValue)) {
				System.out.println("Success");
			} else {
				System.out.println("Failure");
			}
		} else {
			System.out.println("Object not found in tree");
		}
	}

}