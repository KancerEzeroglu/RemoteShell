package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer implements Runnable{
	Socket clientSocketMulti;
	
	public EchoServer(Socket clientSocketMulti) {
		this.clientSocketMulti = clientSocketMulti;
	}
	
public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);
        
        try{
        	ServerSocket serverSocket =
                    new ServerSocket(Integer.parseInt(args[0]));
        	while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected Server");
                new Thread(new EchoServer(clientSocket)).start();
        	}
            
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

@Override
public void run() {
	PrintWriter out;
	try {
		out = new PrintWriter(clientSocketMulti.getOutputStream(), true);
		BufferedReader in = new BufferedReader(
	            new InputStreamReader(clientSocketMulti.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            out.println(inputLine);
	            System.out.println(inputLine);
	        }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}                   
        
	
}
}
