import java.io.*;
import java.net.*;
import java.util.*;

public class algorithm_test {
    public static List<String> routingTableR2 = new ArrayList<String>();    // Global list
    public static void main(String argv[]) throws Exception {
        List<String> routingTableR1 = new ArrayList<String>();

        // FOR TESTING WITH BUFFEREDREADER USE THIS BLOCK*********************
            // String userInput = "";

            // BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));

            // System.out.println("Let's populate R2 Routing Table");
            // do{ // Populate routingTableR2
            //     // Keeps scanner open until user inputs -1 or exit, at which point a break from the loop will occur
            //     System.out.println("Enter the next network IP: ");  
            //     userInput = inFromClient.readLine();
            //     if(!userInput.equals("-1")){
            //         routingTableR2.add(userInput);
            //         System.out.println("Enter the distance to the network: ");  
            //         userInput = inFromClient.readLine();;
            //         routingTableR2.add(userInput);
            //         System.out.println("Enter the neighbor: ");  
            //         userInput = inFromClient.readLine();;
            //         routingTableR2.add(userInput);
            //     }
            // } while (!userInput.equals("-1"));

            // System.out.println("Let's populate R1 Routing Table");
            // do{ // Populate routingTableR1
            //     // Keeps scanner open until user inputs -1 or exit, at which point a break from the loop will occur
            //     System.out.println("Enter the next network IP: ");  
            //     userInput = inFromClient.readLine();
            //     if(!userInput.equals("-1")){
            //         routingTableR1.add(userInput);
            //         System.out.println("Enter the distance to the network: ");  
            //         userInput = inFromClient.readLine();;
            //         routingTableR1.add(userInput);
            //         System.out.println("Enter the neighbor: ");  
            //         userInput = inFromClient.readLine();;
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

            routingTableR1.add("1.2.3.0");
            routingTableR1.add("22");
            routingTableR1.add("R3");
            routingTableR1.add("1.2.4.0");
            routingTableR1.add("12");
            routingTableR1.add("R4");
            routingTableR1.add("1.2.5.0");
            routingTableR1.add("7");
            routingTableR1.add("R3");
        //********************************************************************


        System.out.println("R1 Length = " + routingTableR1.size() + " | R1: " + routingTableR1);
        System.out.println("R2 Length = " + routingTableR2.size() + " | R2: " + routingTableR2);
        runDistanceVectorAlgorithm(routingTableR1);
    }

    public static void runDistanceVectorAlgorithm(List<String> routingTableR1){
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
                c = Integer.parseInt(routingTableR1.get(i+1)) + 2; 
                routingTableR2.set(i+1, String.valueOf(c));
            } else if ((routingTableR2.get(i) == routingTableR1.get(i)) && (Integer.parseInt(routingTableR2.get(i+1)) > (Integer.parseInt(routingTableR1.get(i+1)) + 2))){
                // If a route exists with distance greater than C then change the next-hop to N and distance to C   
                c = Integer.parseInt(routingTableR1.get(i+1)) + 2;
                routingTableR2.set(i+1, String.valueOf(c));
                routingTableR2.set(i+2, routingTableR1.get(i+2));
            }
            
        }

        System.out.println("R2 AFTER: " + routingTableR2);
        
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
