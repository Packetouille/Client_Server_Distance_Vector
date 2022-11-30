// To compile and run the code use commands "javac ApplesA1TCPServer.java" followed by "java ApplesTCPServer.java" in the command line.

import java.io.*;
import java.net.*;
import java.util.*;

class Router2Server{
    public static List<String> routingTableR2 = new ArrayList<String>();
    public static void main(String argv[]) throws Exception {
        String clientString;
        String userInput;
        //List<String> r2Table = new ArrayList<String>();
        List<String> routingTableR1 = new ArrayList<String>();
        String[] clientStringArray;
        Scanner userInputScanner = new Scanner(System.in);

        // Create socket for incoming request
        ServerSocket welcomeSocket = new ServerSocket(11112);

        // Wait for incoming connection request. TCP Connection setup
        Socket connectionSocket = welcomeSocket.accept();

        // Create input (BufferedReader) & output (DataOutputStream) stream attached to socket
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        do{
            System.out.println("Enter the next network IP: ");  
            userInput = userInputScanner.nextLine();
            if(!userInput.equals("-1") && !userInput.equals("exit")){
                routingTableR2.add(userInput);
                System.out.println("Enter the distance to the network: ");  
                userInput = userInputScanner.nextLine();
                routingTableR2.add(userInput);
                System.out.println("Enter the neighbor: ");  
                userInput = userInputScanner.nextLine();
                routingTableR2.add(userInput);
            }
        } while (!userInput.equals("-1") && !userInput.equals("exit"));
        
        System.out.println("routingTableR2 contents: " + routingTableR2);

        while(true){
        // Keeps server on until user inputs exit, at which point a break from the loop will occur
            clientString = inFromClient.readLine();
            System.out.println("Received from Client: " + clientString);
            
            clientStringArray = clientString.split(" ");

            for (String element: clientStringArray){
                routingTableR1.add(element);
            }

            System.out.println("routingTableR1 contents: " + routingTableR1);
  //          System.out.println("stringArray = " + stringArray);

            if (!clientString.equals("exit")){
                outToClient.writeBytes(clientString + '\n');                
            } else{
                welcomeSocket.close();
                System.out.println("Goodbye");
                break;
            }
//            outToClient.writeBytes(clientString);    // '\n' is necessary. Don't know why yet.
        }
    }

    public static void runDistanceVectorAlgorithm(){
        // ALGORITHM GOES HERE. THE PARAMETERS MAY BE THE LISTS RATHER VARIABLES INDEPENDENTLY
    }
}
