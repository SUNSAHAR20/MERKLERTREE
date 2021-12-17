
public class Server extends MTree {
	public Server(String fileContent) {
		super(fileContent);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String file = args[0];
		new MTree(file);
	}

}
