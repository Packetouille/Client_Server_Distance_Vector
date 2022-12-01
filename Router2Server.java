// To compile and run the code use commands "javac ApplesA1TCPServer.java" followed by "java ApplesTCPServer.java" in the command line.

import java.io.*;
import java.net.*;
import java.util.*;

class Router2Server{
    public static List<String> routingTableR2 = new ArrayList<String>();    // Global list

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
            // Keeps scanner open until user inputs -1 or exit, at which point a break from the loop will occur
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
        userInputScanner.close();

        while(true){
        // Keeps server on until user inputs exit on client side, at which point a break from the loop will occur
            clientString = inFromClient.readLine();
            System.out.println("Received from Client: " + clientString);
            
            // Converting clientString into array and building the routingTableR1 list
            clientStringArray = clientString.split(" ");
            for (String element: clientStringArray){
                routingTableR1.add(element);
            }

            System.out.println("routingTableR1 contents: " + routingTableR1);

            runDistanceVectorAlgorithm(routingTableR1);

            if (!clientString.equals("exit")){
                outToClient.writeBytes(clientString + '\n');                
            } else{
                welcomeSocket.close();
                System.out.println("Goodbye");
                break;
            }
        }
    }

    public static void runDistanceVectorAlgorithm(List<String> neighborList){
        // ALGORITHM GOES HERE. THE PARAMETERS MAY BE THE LISTS RATHER VARIABLES INDEPENDENTLY
        // List comes in as [ip1, distance1, neighbor1,...,ipN, distanceN, neighborN]

        List<String> tempList = new ArrayList<String>();
        
        System.out.println("Next Network IP | Distance | Neighbor");
        for(int i = 0; (i+2) < neighborList.size(); i+=3){
            System.out.println(neighborList.get(i) + " | " + neighborList.get(i+1) + " | " + neighborList.get(i+2));
            // if r1[distance] + 2 < r2[distance] then update r2 with r1[distance] += 2 and Neighbor = R1
            
            if(Integer.parseInt(neighborList.get(i+1)) + 2 < Integer.parseInt(routingTableR2.get(i+2))){
                
            }
        }

        
        /* Algorithm from text book
        "Repeat forever { 
            Wait for a routing message to arrive over the network from a neighbor; 
            let the sender be switch N; 
            for each entry in the message { 
                Let V be the destination in the entry and let D be the distance; 
                Compute C as D plus the weight assigned to the link over which the message arrived; 
                Examine and update the local routing table: 
                if (no route exists to V ) { 
                    add an entry to the local routing table for destination V with next-hop N and distance C; 
                } else if (a route exists that has next-hop N) { 
                    replace the distance in existing route with C; 
                } else if (a route exists with distance greater than C) { 
                    change the next-hop to N and distance to C; 
                } 
            }"
        }
        */
    }
}