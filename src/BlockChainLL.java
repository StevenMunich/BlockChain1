import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class NodeLL {
    Block data; NodeLL next; NodeLL prev;

    //Constructors for Nodes #1 General
    NodeLL(Block data) {
        this.data = data; prev = null; next = null;
    }
    //2 The one used to link prev Node
    NodeLL(Block data, NodeLL prev) throws NoSuchAlgorithmException {
    this.data = data;
    this.prev = prev;
    data.Hash_ofP_revious_Block_Header = hash(prev.data.Header);
    //next = null;
    }
    //3 All feilds used.
    NodeLL(Block data, NodeLL prev, NodeLL next) {this.data = data; this.prev = prev; this.next = next; }
    //4 no feilds but pointer
    public NodeLL(NodeLL next) {
        this.next = next;
        //data.Hash_ofP_revious_Block_Header = prev.data.Header;
    }
//------------------------------------END OF CONSTRUCTORS--------------------------------

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

}//end Class NodeLL

class BlockChainLL {
    NodeLL head;
    NodeLL tail;
    int size = 0;

    boolean isEmpty(){
        return head == null & tail == null;
    }
    public void addNode(Block newData, int index) {
        NodeLL newNode = new NodeLL(newData);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            size++;
            return;
        }
    }//end add node

    public void addToHead(NodeLL newHead){
        head = new NodeLL(head);
        if(tail == null)
            tail = head;
        size++;
    }
    public void addToTail(NodeLL newTail){
        tail.next = newTail;
        tail = newTail;
        size++;
    }
    public void insert(NodeLL newNode, int index){

        NodeLL current_Node = head;
        for(int i = 0; i < index; i++){
            current_Node = current_Node.next; //iteration
        }
        //If any after current node we attach to new Node to the Right
        NodeLL nodeAfter = current_Node.next;
        //Change the pointers
        newNode.prev = current_Node;
        newNode.next = nodeAfter;
        nodeAfter.prev = newNode;
        current_Node.next = newNode;
        size++;
    }//end Insert()

    public void displayList(){
        NodeLL currentNode = head;
        for(int i = 0; i < size; i++) {
            if(currentNode == null) break; //end of Loop
            //Print Statements
            System.out.println("Current node header: " + currentNode.data.Header);
            System.out.println("Current node hash: " + currentNode.data.Hash_ofP_revious_Block_Header);
            System.out.println("Current node hash: " + currentNode.data.Hash_ofP_revious_Block_Header);

            if (currentNode.next != null) //Iteration C++ style
                currentNode = currentNode.next;
            //end if
        }//end Loop
    }//end function

}

class bCLLTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        BlockChainLL bLL = new BlockChainLL(); //custom LL
        List<NodeLL> theList = new ArrayList<NodeLL>();


        Block b = new Block("Steven");
        NodeLL nodeLL = new NodeLL(b);
        theList.add(nodeLL);

        bLL.addToHead(nodeLL);

        Block b2 = new Block("Munich");
        NodeLL nodeLL2 = new NodeLL(b2, nodeLL);
        theList.add(nodeLL2);

        Block b3 = new Block("Danee");
        NodeLL nodeLL3 = new NodeLL(b3  , nodeLL2);
        theList.add(nodeLL3);

        printList(theList);

        NodeLL pNode = nodeLL3; //pNode is pointing to tail

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenu:\n1)add Block\n2)search Tree\n3)display BlockChain From outside function\n4) display BlockChain From Object\n999)Exit");
            String answer = scanner.nextLine();
            if (answer.equals("1")) {
                System.out.print("input data that will become header: ");
                String s = scanner.nextLine();
                Block bInput = new Block(s);
                NodeLL nodeInput = new NodeLL(bInput, pNode);
                theList.add(nodeInput);
                pNode = nodeInput;
                System.out.println(pNode.data.Header + " has been added with a hash of "
                        + pNode.data.Hash_ofP_revious_Block_Header
                        + " created on " + pNode.data.timeStamp
                        + " and linked to block with header of: " + pNode.prev.data.Header);
            }//End If

            if (answer.equals("3"))
                printList(theList);
            if (answer.equals("4"))
                bLL.displayList();

            if (answer.equals("999"))
                break;
        } //End While
        scanner.close();
    } //End of Main


    private static void printList(List<NodeLL> theList){
        try{
            for(NodeLL n: theList){
                if(n.prev != null)
                    System.out.println(n.prev.data.Header);
                if(n.data != null){
                    System.out.println(n.data.Hash_ofP_revious_Block_Header);
                    System.out.println(n.data.Header);
                }
            }//end Loop
        }catch(Exception e){
            System.out.println("Error!");
        }//end Try
    }
} //End of Class