import java.io.*;
import java.net.*;
import java.util.*;

public class distance_vector_algorithm_test {
    public static List<String> routingTableR2 = new ArrayList<String>();    // Global list
    public static void main(String argv[]) throws Exception {
        List<String> routingTableR1 = new ArrayList<String>();

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
        routingTableR1.add("18");
        routingTableR1.add("R3");

        System.out.println(routingTableR1);
        System.out.println(routingTableR2);
        runDistanceVectorAlgorithm(routingTableR1);
    }

    public static void runDistanceVectorAlgorithm(List<String> routingTableR1){
        // ALGORITHM GOES HERE. THE PARAMETERS MAY BE THE LISTS RATHER VARIABLES INDEPENDENTLY
        // List comes in as [ip1, distance1, neighbor1,...,ipN, distanceN, neighborN]
        // V = destination | D = distance | N = next-hop | C = D + 2 (the weight assigned to the link over which message arrived)

        List<String> tempList = new ArrayList<String>();

        for(int i = 0; (i+2) < routingTableR1.size(); i+=3){
            if(routingTableR2.indexOf(routingTableR1.get(i)) == -1){
            // If no route exists to V then add the route to routingTableR2
                routingTableR2.add(routingTableR1.get(i));
                routingTableR2.add(routingTableR1.get(i+1));
                routingTableR2.add(routingTableR1.get(i+2));
            }
            // else if(){
            // // If a route exists that has next-hop N then replace distance of existing route with C
            // }else if(){

            // }
            
            // if r1[distance] + 2 < r2[distance] then update r2 with r1[distance] += 2 and Neighbor = R1
            // if(routingTableR2.indexOf(d))
            // if(Integer.parseInt(routingTableR1.get(i+1)) + 2 < Integer.parseInt(routingTableR2.get(i+2))){
                
            // }
        }
        System.out.println(routingTableR2);
        
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
