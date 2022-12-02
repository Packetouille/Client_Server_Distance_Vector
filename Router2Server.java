// To compile and run the code use commands "javac ApplesA1TCPServer.java" followed by "java ApplesTCPServer.java" in the command line.

import java.io.*;
import java.net.*;
import java.util.*;

class Router2Server{
   // public static List<String> routingTableR2 = new ArrayList<String>();    // Global list

    public static void main(String argv[]) throws Exception {
        String clientString;
        String userInput;
        //List<String> r2Table = new ArrayList<String>();
        List<String> routingTableR1 = new ArrayList<String>();
        List<String> routingTableR2 = new ArrayList<String>();
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
            runDistanceVectorAlgorithm(routingTableR1, routingTableR2);

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
                System.out.println("  " + routingTable.get(i) + "  " + " | " + "    " + routingTable.get(i+1) + "   " + " | " + "   " + routingTable.get(i+2) + "   ");
            } else{
                System.out.println("  " + routingTable.get(i) + "  " + " | " + "   " + routingTable.get(i+1) + "   " + " | " + "   " + routingTable.get(i+2) + "   ");                
            }
        }
    }

    public static void runDistanceVectorAlgorithm(List<String> routingTableR1, List<String> routingTableR2){
        // List comes in as [ip1, distance1, neighbor1,...,ipN, distanceN, neighborN]
        // V = destination | D = distance | N = next-hop | C = D + 2 (the weight assigned to the link over which message arrived)
        
        int c = 0;

        for (int i = 0; (i+2) < routingTableR1.size(); i+=3){
            if (routingTableR2.indexOf(routingTableR1.get(i)) == -1){
                // If no route exists to V then add the route to routingTableR2
                routingTableR2.add(routingTableR1.get(i));
                routingTableR2.add(routingTableR1.get(i+1));
                routingTableR2.add(routingTableR1.get(i+2));
            } else if ((routingTableR2.get(i) == routingTableR1.get(i)) && (routingTableR2.get(i+2) == routingTableR1.get(i+2))){
                // If a route exists that has next-hop N then replace distance of existing route with C
                
                System.out.println("IN HERE!");
                System.out.println(routingTableR2.get(i+2) + " == " + routingTableR1.get(i+2));

                c = Integer.parseInt(routingTableR1.get(i+1)) + 2; 
                routingTableR2.set(i+1, String.valueOf(c));
            } else if ((routingTableR2.get(i) == routingTableR1.get(i)) && (Integer.parseInt(routingTableR2.get(i+1)) > (Integer.parseInt(routingTableR1.get(i+1)) + 2))){
                // If a route exists with distance greater than C then change the next-hop to N and distance to C   
                c = Integer.parseInt(routingTableR1.get(i+1)) + 2;
                routingTableR2.set(i+1, String.valueOf(c));
                routingTableR2.set(i+2, routingTableR1.get(i+2));
            }
        }

        System.out.println("\nR1 Routing Table After");
        printRoutingTable(routingTableR1);

        System.out.println("\nR2 Routing Table After");
        printRoutingTable(routingTableR2);

        
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