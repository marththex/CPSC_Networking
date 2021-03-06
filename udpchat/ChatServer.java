//Made by Logan Jensen
//Partner: Marcus Chong

import java.io.*;
import java.net.*;
import java.util.Arrays;

class ChatServer {
    public static void main(String args[]) throws Exception
    {

        DatagramSocket serverSocket = null;

        try
        {
            serverSocket = new DatagramSocket(9876);
        }

        catch(Exception e)
        {
            System.out.println("Failed to open UDP socket");
            System.exit(0);
        }

        int port =0, port1=0, port2=0;
        InetAddress IPAddress= null, IPAddress1= null, IPAddress2=null;
        String message="";
        String response="";
        DatagramPacket receivePacket=null;
        DatagramPacket sendPacket= null;
        int state=0;

        byte[] receiveData = new byte[1024];
        byte[] sendData  = new byte[1024];
        byte[] messageBytes = new byte[1024];

        while(state != 3)
        {
        	if (state == 0)
            {      
            	receivePacket = new DatagramPacket(receiveData, receiveData.length);            	 
                serverSocket.receive(receivePacket);
                System.out.println("socket");
                message = new String(receivePacket.getData());
                System.out.println("converted message");
                System.out.println(message.substring(0,5));
                if (message.substring(0,5).equals("HELLO")) //Checks for Hello message from client Red or blue
                {
                   
                    IPAddress1 = receivePacket.getAddress();
                    port1 = receivePacket.getPort();
                    response = "100";
                    sendData = response.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                    serverSocket.send(sendPacket);
                  

                    state = 1;
                }
            }

            if (state==1)
            {
            	receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                message = new String(receivePacket.getData());

                if (message.substring(0,5).equals("HELLO"))
                {
                    IPAddress2 = receivePacket.getAddress();

                    port2 = receivePacket.getPort();
                    //Send 200 message to both clienta
                    response = "200";
                    sendData = response.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                    serverSocket.send(sendPacket);
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                    serverSocket.send(sendPacket);

                    state =2;
                }
            }

            if (state == 2)
            {
                //Chat Mode
                Arrays.fill(receiveData, (byte)0);
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                message = new String(receivePacket.getData());

                //Ends the program if Goodbye and sends to both clients
                if (message.length()>= 7 && message.substring(0,7).equals("Goodbye"))
                {
                    state= 3;
                    response = "Goodbye";
                    Arrays.fill(sendData, (byte)0);
                    sendData =response.getBytes();
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                    serverSocket.send(sendPacket);
                    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                    serverSocket.send(sendPacket);
                }
                else
                {
                    //If not Goodbye
                    IPAddress = receivePacket.getAddress();
                    port = receivePacket.getPort();

                    if ((port == port1) && (IPAddress.equals(IPAddress1))) {
                        response = message;
                        Arrays.fill(sendData, (byte)0);
                        sendData = response.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress2, port2);
                        serverSocket.send(sendPacket);
                    } else {
                        response = message;
                        Arrays.fill(sendData, (byte)0);
                        sendData = response.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress1, port1);
                        serverSocket.send(sendPacket);
                    }
                    //Stay in Chat mode untill Goodbye
                }
            }
            
            
        }
        serverSocket.close();
        
    }
}
