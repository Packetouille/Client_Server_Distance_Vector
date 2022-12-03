// To compile and run the code use commands "javac ApplesA1TCPServer.java" followed by "java ApplesTCPServer.java" in the command line.

import java.io.*;
import java.net.*;
import java.util.*;

class LunenfeldServer{
    public static void main(String argv[]) throws Exception {
        String clientString;
        String userInput;
        String[] clientStringArray;
        List<String> routingTableR1 = new ArrayList<String>();
        List<String> routingTableR2 = new ArrayList<String>();
        List<String> updatedRoutingTableR2 = new ArrayList<String>();

        // Create socket for incoming request
        ServerSocket welcomeSocket = new ServerSocket(11112);

        // Wait for incoming connection request. TCP Connection setup
        Socket connectionSocket = welcomeSocket.accept();

        // Create input (BufferedReaders) & output (DataOutputStream) stream attached to socket
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        do{
            // Reads user input until enters -1 or exit, at which point a break from the loop will occur
            System.out.println("Enter the next network IP: ");  
            userInput = inFromUser.readLine();
            if(!userInput.equals("-1") && !userInput.equals("exit")){
                routingTableR2.add(userInput);
                System.out.println("Enter the distance to the network: ");  
                userInput = inFromUser.readLine();
                routingTableR2.add(userInput);
                System.out.println("Enter the neighbor: ");  
                userInput = inFromUser.readLine();
                routingTableR2.add(userInput);
            }
        } while (!userInput.equals("-1") && !userInput.equals("exit"));

        while(true){
        // Keeps server on until user inputs exit on client side, at which point a break from the loop will occur
            clientString = inFromClient.readLine();
            System.out.println("Received from Client: " + clientString);
            
            clientStringArray = clientString.split(" ");    // Converting clientString into array for building of routingTableR1

            // Build routing table while adding XX to neighbor field since it is not part of the incoming message
            int count = 0;  // count variable is used for keeping track of the indices where a "XX" should be insterted
            for(int i = 0; i < clientStringArray.length; i++){
                count++;
                routingTableR1.add(clientStringArray[i]);
                if(count == 2){
                    routingTableR1.add("XX");
                    count = 0;
                }
            }

            // Output routing tables as entered
            System.out.println("\nR1 Routing Table");
            printRoutingTable(routingTableR1);
            System.out.println("\nR2 Routing Table");
            printRoutingTable(routingTableR2);

            updatedRoutingTableR2 = runDistanceVectorAlgorithm(routingTableR1, routingTableR2);    // Update routing table R2 using R1's message

            // Output updated routing table for R2
            System.out.println("\nR2 Updated Routing Table");
            printRoutingTable(updatedRoutingTableR2);

            if (!clientString.equals("exit")){
                outToClient.writeBytes(clientString + '\n');                
            } else{
                welcomeSocket.close();
                System.out.println("Goodbye");
                break;
            }
        }
    }

    public static void printRoutingTable(List<String> routingTable){
        System.out.println("DESTINATION | DISTANCE | NEXT HOP");

        for (int i = 0; (i+2) < routingTable.size(); i+=3){
            if(routingTable.get(i+1).length() == 1){
                System.out.println("  " + routingTable.get(i) + "  " + " | " + "    " + routingTable.get(i+1) 
                + "   " + " | " + "   " + routingTable.get(i+2) + "   ");
            } else{
                System.out.println("  " + routingTable.get(i) + "  " + " | " + "   " + routingTable.get(i+1) 
                + "   " + " | " + "   " + routingTable.get(i+2) + "   ");                
            }
        }
    }

    public static List<String> runDistanceVectorAlgorithm(List<String> routingTableR1, List<String> routingTableR2){
        // List comes in as [ip1, distance1, neighbor1,...,ipN, distanceN, neighborN]
        // V = destination | D = distance | N = next-hop | C = D + 2 (the weight assigned to the link over which message arrived)
        
        int c = 0;
        int weight = 2;

        for (int i = 0; (i+2) < routingTableR1.size(); i+=3){
            if (routingTableR2.indexOf(routingTableR1.get(i)) == -1){
                // If no route exists to V then add an entry to the local routing table for destination V with next-hop N and distance C
                c = Integer.parseInt(routingTableR1.get(i+1)) + weight;
                routingTableR2.add(routingTableR1.get(i));
                routingTableR2.add(String.valueOf(c));
                routingTableR2.add("R1"); // next hop is sender
            }else if ((routingTableR2.get(i).equals(routingTableR1.get(i)) && (routingTableR2.get(i+2).equals("R1")))){
                // If a route exists that has next-hop N then replace distance of existing route with C
                
                // THE SECOND PART OF THIS LOGIC TEST MAY BE WRONG. IF R1's MESSAGE IS ONLY PASSING 
                // THE DESTINATION AND DISTANCE, HOW DO WE KNOW WHAT ITS NEXT HOPS ARE WITHIN THE SERVER????
                c = Integer.parseInt(routingTableR1.get(i+1)) + weight; 
                routingTableR2.set(i+1, String.valueOf(c));
            }else if ((routingTableR2.get(i).equals(routingTableR1.get(i))) && (Integer.parseInt(routingTableR2.get(i+1)) > (Integer.parseInt(routingTableR1.get(i+1)) + 2))){
                // If a route exists with distance greater than C then change the next-hop to N and distance to C   
                c = Integer.parseInt(routingTableR1.get(i+1)) + weight;
                routingTableR2.set(i+1, String.valueOf(c));
                routingTableR2.set(i+2, "R1");
            }
        }

        return routingTableR2;
    }
}
    