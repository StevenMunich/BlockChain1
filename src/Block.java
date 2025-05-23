import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

public class Block {
    public static String Header1 = "Steven"; //in client handler. This code is messy.

    //--------------------------Structural Components------------------
    //Block Attributes
    public String Header;
    public String Hash_ofP_revious_Block_Header = "-----";
    private Node MerkleRoot;
    public String timeStamp;
    protected static int numberOfBlocks = 0;
    private int uniqueBlockID;
    //USE FOR MINING
    public String Nonce = "0";
    public int difficulty = 0;

    //----------------------------Network Components------------------
    //Server myServer;
    //Client blockClient;

    public Block(String s) throws NoSuchAlgorithmException, IOException {
        numberOfBlocks++;
        uniqueBlockID = numberOfBlocks;
        //Header = String.valueOf(uniqueBlockID * 7 + 3 * 5);
        Header = s;
        Date date = new Date();
        timeStamp = date.toString();

        List<String> elems = Arrays.asList(
                "This", "Is", "Computer", "Science",
                "plus", "biz", "and", "more");
        System.out.println("Inputs: ");
        for (String elem : elems) {
            System.out.print(elem + " | ");
        }
        System.out.println("\n");
        MerkleTree mtree = new MerkleTree(elems); //Tree Constructor.
        System.out.println(
                "Root Hash: " + mtree.getRootHash() + "\n");
        mtree.printTree();

        MerkleRoot = mtree.root; //ROOT IS ASSIGNED


    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

       // Block b = new Block();
        //System.out.println(MerkleRoot.content); //Tree is built in node's main method

       // if(!b.Genesis){
           // b.blockClient.sendBlockHeader();
     //   } else{ //Genesis = True
     //       System.out.println("This is the Genesis Block of the Chain: Welcome.");
      //  }



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
