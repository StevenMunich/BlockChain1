import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Block2TEST {

    //--------------------------tructural Components------------------

    public boolean Genesis = false; //Set to false on other instances;
    //public String timeStampe;
    //USE FOR MINING
    //public string Nonce;
    //public int difficulty;

    public static String Header = "Testosterone";
    public static String Hash_ofP_revious_Block_Header;

    static {
        try {
            Hash_ofP_revious_Block_Header = hash("steven");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
          }
    }

    private static Node MerkleRoot;

    //----------------------------Network Components------------------
    Server myServer;
    Client blockClient;

    public Block2TEST() throws NoSuchAlgorithmException, IOException {

        List<String> elems = Arrays.asList(
                "This", "Is", "Computer", "Science",
                "plus", "biz", "and", "more");
       // System.out.println("Inputs: ");
        for (String elem : elems) {
            System.out.print(elem + " | ");
        }
        System.out.println("\n");
        MerkleTree mtree = new MerkleTree(elems);
        System.out.println(
                "Root Hash: " + mtree.getRootHash() + "\n");
        //mtree.printTree();

        MerkleRoot = mtree.root; //ROOT IS ASSIGNED
        try {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, "Block2");
            client.listenForMessage();
            client.sendMessage();
        } catch (ConnectException c){
            System.out.println("Error connecting, Closing"); return;
        }


       // ServerSocket serverSocket = new ServerSocket(1235);
       // myServer = new Server(serverSocket);
       // myServer.startServer();
        //Thread serverThread = new Thread(myServer);


        System.out.println("Server Online....");

    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        Block2TEST b = new Block2TEST();
        System.out.println(MerkleRoot.content);
        System.out.println();


    }

    public void checkPreviousHeader(){

    }//end check previous

    // Method to hash a string value using SHA-256
    static String hash(String val)
            throws NoSuchAlgorithmException
    {
        MessageDigest digest
                = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                val.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }//end method

}//End Class
