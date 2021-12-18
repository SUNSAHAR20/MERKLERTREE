
public class Server extends MTree {
	
	public Server(String fileContent) {
		super(fileContent);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String fileContent = args[0];
		
		System.out.println("Server:<Started> Creating Merkle Tree for a given file content");
		
		new MTree(fileContent);
	}

}
