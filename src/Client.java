import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
//in COnfigurations -> Modify options -> Allow multiple instances

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String userName;

    public Client(Socket socket, String userName) throws ConnectException{
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Stream to Send
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendBlockHeader(){ //Sends message to clientHandler
        try{
            bufferedWriter.write(Block2TEST.Hash_ofP_revious_Block_Header); //WHat ClientHandler is waiting for
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } //End send messag

    public void sendMessage(){ //Sends message to clientHandler
        //if (s == null) s = "0";
        try{

            System.out.print("Sending Hash of Previous Block Header... ");
            bufferedWriter.write(Block2TEST.Hash_ofP_revious_Block_Header); //WHat ClientHandler is waiting for
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);

            while(socket.isConnected()){
                System.out.print("Send Message: ");
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }

        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    } //End send message

    //Listens for messages that are broadcasted
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while(socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    } catch (IOException e) {
                       // throw new RuntimeException(e);
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }

                }//End While

            }//End run()
        }).start();
    }//End of listenForMessage

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write your username");
        String username = scanner.nextLine();
        try {
            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, username);
            client.listenForMessage();
           // client.sendMessage();
        } catch (ConnectException c){
            System.out.println("Error connecting, Closing"); return;
        }

    }

}
