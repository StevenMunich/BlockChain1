import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// Node class represents a node in the Merkle Tree
class Node {
    Node left, right;
    String value, content;
    boolean isCopied;

    // Node constructor
    Node(Node left, Node right, String value,
         String content, boolean isCopied)
    {
        this.left = left;
        this.right = right;
        this.value = value;
        this.content = content;
        this.isCopied = isCopied;
    }

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
    }

    // Method to create a copy of a node
    Node copy()
    {
        return new Node(this.left, this.right, this.value,
                this.content, true);
    }
}//End of Class Node



// MerkleTree class represents the Merkle Tree
class MerkleTree {
    Node root;

    // MerkleTree constructor
    MerkleTree(List<String> values) throws NoSuchAlgorithmException {
        this.root = buildTree(values);
    }

    // Method to build the Merkle Tree from a list of string
    // values
    Node buildTree(List<String> values)
            throws NoSuchAlgorithmException
    {
        List<Node> leaves = new ArrayList<>();
        for (String e : values) {
            leaves.add(new Node(null, null, Node.hash(e), e,
                    false));
        }
        System.out.println("All of leaves: " + leaves.get(0).content);
        if (leaves.size() % 2 == 1) {
            leaves.add(
                    leaves.get(leaves.size() - 1).copy());
        }
        return buildTreeRec(leaves);  //different function
    }

    // Recursive method to build the Merkle Tree
    Node buildTreeRec(List<Node> nodes) throws NoSuchAlgorithmException {

        System.out.println("Recusive call: List is: " + nodes.subList(0, nodes.size()));
        if (nodes.size() % 2 == 1) {
            nodes.add(nodes.get(nodes.size() - 1).copy());
        }
        int half = nodes.size() / 2;
        //BASE CASE
        if (nodes.size() == 2) {
            return new Node(nodes.get(0), nodes.get(1),
                    Node.hash(nodes.get(0).value
                            + nodes.get(1).value),
                    nodes.get(0).content + "+" //left + right =
                            + nodes.get(1).content,
                    false);
        }

        Node left = buildTreeRec(nodes.subList(0, half));
        Node right = buildTreeRec(
                nodes.subList(half, nodes.size()));
        String value = Node.hash(left.value + right.value);
        String content = left.content + "+" + right.content;
        return new Node(left, right, value, content, false);
    }

    // Method to print the Merkle Tree
    void printTree() { printTreeRec(this.root); }

    // Recursive method to print the Merkle Tree
    void printTreeRec(Node node)
    {
        if (node != null) {
            if (node.left != null) {
                System.out.println("Left: "
                        + node.left.value);
                System.out.println("Right: "
                        + node.right.value);
            }
            else {
                System.out.println("Input");
            }

            if (node.isCopied) {
                System.out.println("(Padding)");
            }
            System.out.println("Value: " + node.value);
            System.out.println("Content: " + node.content);
            System.out.println("Object: " + node);
            System.out.println("");
            printTreeRec(node.left);
            printTreeRec(node.right);
        }
    }

    // Method to get the root hash of the Merkle Tree
    String getRootHash() { return this.root.value; }
}//end of MerkleTree

public class Main {
    public static void main(String[] args)
            throws NoSuchAlgorithmException
    {
        List<String> elems = Arrays.asList(
                "This", "Is", "Computer", "Science",
                "plus", "biz", "and", "more");
        System.out.println("Inputs: ");
        for (String elem : elems) {
            System.out.print(elem + " | ");
        }
        System.out.println("\n");
        MerkleTree mtree = new MerkleTree(elems);
        System.out.println(
                "Root Hash: " + mtree.getRootHash() + "\n");
        mtree.printTree();
    }
}