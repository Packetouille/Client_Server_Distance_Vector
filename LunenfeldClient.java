// To compile and run the code use commands "javac LunenfeldClient.java" followed by "java LunenfeldClient.java" in the command line.

import java.io.*;
import java.net.*;

class LunenfeldClient{
    public static void main(String argv[]) throws Exception {
        String userInput = "";
        String messageToSend = "";
        String neighbor = "";
        String serverResponse;

        // Create input stream
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        
        //Create socket, connection to server (hostid and port#). TCP connection setup
        Socket clientSocket = new Socket("localhost", 11112);

        // Create input (BufferedReader) & output (DataOutputStream) stream attached to socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        do{
            do{
                System.out.println("Enter the next network IP: ");  
                userInput = inFromUser.readLine();
                if(!userInput.equals("-1") && !userInput.equals("exit")){
                    messageToSend += userInput + " ";
                    System.out.println("Enter the distance to the network: ");  
                    messageToSend += userInput = inFromUser.readLine() + " ";
                    System.out.println("Enter the neighbor: ");  
                    neighbor= userInput = inFromUser.readLine() + " ";      // Store neighbor separately as it will not be sent to server.
                }
            } while(!userInput.equals("-1") && !userInput.equals("exit"));
            if(userInput.equals("-1")){
                outToServer.writeBytes(messageToSend + '\n');
            } else{
                outToServer.writeBytes("exit" + '\n');
            }
            if(!userInput.equals("exit")){ 
                serverResponse = inFromServer.readLine();
                System.out.println("Response from server: " + serverResponse);
            }

            messageToSend = "";
        } while (!userInput.equals("exit"));

        System.out.println("Goodbye");
        clientSocket.close();
    }
}