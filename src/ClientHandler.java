import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //allows broadcast to multiple clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    //public static ArrayList<Block> BlocksList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader; //reads messages from client
    private BufferedWriter bufferedWriter; //writes to other clients
    private String clientUserName;
    Boolean headerVerified = false;


    ClientHandler(Socket socket){
        try{
            this.socket = socket; //sets this objects socket to what is passed
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Stream to Send
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Stream to Read
            this.clientUserName = bufferedReader.readLine(); //gets username
            clientHandlers.add(this);
            broadCastMessage("Server: " + clientUserName + "has entered the chat!");
        } catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } //End of Constructor()

    //Runs on a seperate thread.
    //So that the rest of the application isn't stopped by "messageFromClient = bufferedReader.readline();
    @Override
    public void run(){
        String messageFromClient;


        while(socket.isConnected()){ //while client is connected lets listen for messages
            try {
                messageFromClient = bufferedReader.readLine();
               // broadCastMessage(messageFromClient);

                //first thing is to verify blockHeader HASH
                if(!headerVerified)
                    verifyHeader(messageFromClient);
                else
                    broadCastMessage("Hacked: ");



            } catch(IOException ioException){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; //To exit loop when exception is caught
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } //End while
    } //End of run

    public void broadCastMessage(String messageToSend){
        for(ClientHandler clientHandler: clientHandlers){ //iterates through List of Connected users
            try{
                if(!clientHandler.clientUserName.equals(clientUserName)) { //checks if userName is from Sender
                    clientHandler.bufferedWriter.write(messageToSend); //sends the message
                    clientHandler.bufferedWriter.newLine(); //"I am done sending data"
                    clientHandler.bufferedWriter.flush(); //empties the buffer Before it is full
                } //End If
            } catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        } //End Loop
    } //End of broadCastMessage

    public void removeCLientHandler(){
        clientHandlers.remove(this); //removes client handler from static Array
        broadCastMessage("Server: " + clientUserName + "has exited the chat");
    }

    //BLOCKCHAIN
    public void verifyHeader(String messageFromClient) throws IOException, NoSuchAlgorithmException {
        System.out.println("testing header Verification");

        if(Node.hash(Block.Header).compareTo(messageFromClient) <= 0){
            headerVerified = true;
            System.out.println("Client's Header Hash has been verified ");
            broadCastMessage("Verfied! Welcome to the network!");
        } else {
            System.out.println("Compare Failed or Header has been verified.");
            broadCastMessage("Compare Failed or Header has been verified.");
        }

    }


    //NON-CLOCKCHAIN. General Infrastructure
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeCLientHandler();
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    } //end closeEverything


}
