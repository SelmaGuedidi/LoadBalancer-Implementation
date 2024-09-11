package com.example.LoadBalancer;

import com.example.LoadBalancer.utils.BackendServers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable{

    private Socket clientSocket;

    public ClientSocketHandler(final Socket socket){
        this.clientSocket=socket;
    }
    @Override
    public void run() {
    try {
        InputStream ClientToLoadBalancerInputStream = clientSocket.getInputStream();
        OutputStream LoadBalancerToClientOutputStream = clientSocket.getOutputStream();
        String backendHost = BackendServers.getHost();
        System.out.println("Host selected to handle this request: "+backendHost);
        // Create a TCP connection with the backend servers
        Socket backendSocket = new Socket(backendHost, 8080);
        InputStream BackendServerToLoadBalancerInputStream = backendSocket.getInputStream();
        OutputStream LoadBalancerToBackendServerOutputStream = backendSocket.getOutputStream();

        Thread clientDataHandler =new Thread() {
            public void run() {
                int data;
                try {
                    while ((data = ClientToLoadBalancerInputStream.read()) != -1) {
                        LoadBalancerToBackendServerOutputStream.write(data);
                    }

                } catch (IOException e){throw new RuntimeException(e);}
            }
        };
        clientDataHandler.start();
        Thread backendDataHandler =new Thread() {
            public void run() {
                int data;
                try {
                    while ((data = BackendServerToLoadBalancerInputStream.read()) != -1) {
                        LoadBalancerToClientOutputStream.write(data);
                    }

                } catch (IOException e){throw new RuntimeException(e);}
            }
        };
        backendDataHandler.start();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }
}
