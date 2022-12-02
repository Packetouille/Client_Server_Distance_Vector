import java.io.*;
import java.net.*;
import java.util.*;

public class algorithm_test {
//    public static List<String> routingTableR2 = new ArrayList<String>();    // Global list
    public static void main(String argv[]) throws Exception {
        List<String> routingTableR1 = new ArrayList<String>();
        List<String> routingTableR2 = new ArrayList<String>();
        List<String> updatedRoutingTableR2 = new ArrayList<String>();
        String clientMessage = "";
        String[] clientStringArray;

        // FOR TESTING WITH BUFFEREDREADER USE THIS BLOCK*********************
            // String userInput = "";

            // BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            // System.out.println("Let's populate R2 Routing Table");
            // do{ // Populate routingTableR2
            //     // Keeps scanner open until user inputs -1 or exit, at which point a break from the loop will occur
            //     System.out.println("Enter the next network IP: ");  
            //     userInput = inFromUser.readLine();
            //     if(!userInput.equals("-1")){
            //         routingTableR2.add(userInput);
            //         System.out.println("Enter the distance to the network: ");  
            //         userInput = inFromUser.readLine();;
            //         routingTableR2.add(userInput);
            //         System.out.println("Enter the neighbor: ");  
            //         userInput = inFromUser.readLine();
            //         routingTableR2.add(userInput);
            //     }
            // } while (!userInput.equals("-1"));

            // System.out.println("Let's populate R1 Routing Table");
            // do{ // Populate routingTableR1
            //     // Keeps scanner open until user inputs -1 or exit, at which point a break from the loop will occur
            //     System.out.println("Enter the next network IP: ");  
            //     userInput = inFromUser.readLine();
            //     if(!userInput.equals("-1")){
            //         routingTableR1.add(userInput);
            //         System.out.println("Enter the distance to the network: ");  
            //         userInput = inFromUser.readLine();;
            //         routingTableR1.add(userInput);
            //         System.out.println("Enter the neighbor: ");  
            //         userInput = inFromUser.readLine();
            //         routingTableR1.add(userInput);
            //     }
            // } while (!userInput.equals("-1"));
        //*********************************************************************

        // FOR TESTING WITHOUT BUFFEREDREADER USE THIS BLOCK ******************
            routingTableR2.add("1.2.3.0");
            routingTableR2.add("24");
            routingTableR2.add("R1");
            routingTableR2.add("1.2.4.0");
            routingTableR2.add("17");
            routingTableR2.add("R4");

            clientMessage = "1.2.3.0 22 1.2.4.0 12 1.2.5.0 7";    // Incoming message does not include neighbors

            clientStringArray = clientMessage.split(" ");
            
            int count = 0;
            for(int i = 0; i < clientStringArray.length; i++){
            // Build routing table while adding XX to neighbor field since it is not part of the incoming message
                count++;
                routingTableR1.add(clientStringArray[i]);
                if(count == 2){
                    routingTableR1.add("XX");
                    count = 0;
                }
            }
        //********************************************************************

        // Output routing tables as entered
        System.out.println("\nR1 Routing Table");
        printRoutingTable(routingTableR1);
        System.out.println("\nR2 Routing Table");
        printRoutingTable(routingTableR2);

        updatedRoutingTableR2 = runDistanceVectorAlgorithm(routingTableR1, routingTableR2); // Update routing table R2 using R1's message
        
        // Output updated routing table for R2
        System.out.println("\nR2 Updated Routing Table");
        printRoutingTable(updatedRoutingTableR2);
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
        
        int c;
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
                
                // THE SECOND PART OF THIS LOGIC TEST IS WRONG. IF R1's MESSAGE IS ONLY PASSING 
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

/* 
Algorithm from text book
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