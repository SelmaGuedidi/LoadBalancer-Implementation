package com.example.LoadBalancer.utils;

import java.util.ArrayList;
import java.util.List;

public class BackendServers {
    private static List<String> servers= new ArrayList<>();
    private static int count= 0;
    static{
        // Change IP1 and IP2 with the remote servers IPS
        servers.add("IP1");
        servers.add("IP2");
    }
    public static String getHost(){
        String host= servers.get(count% servers.size());
        count++;
        return host;
    }
}
