/**
*	Red Program
*	Connects to a UDP Server
*	Receives a line of input from the keyboard and sends it to the server
*	Receives a response from the server and displays it.
*
*	@author: Michael Fahy
@	version: 2.1
*/

import java.io.*;
import java.net.*;

class Red 
{
	public static void main(String args[]) throws Exception
	{

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");

		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
      
		int state  = 0;
		String message = "HELLO";
		String response  = "    ";
		String sentence = "   ";
        
		DatagramPacket sendPacket = null;
		DatagramPacket receivePacket = null;

		sendData = message.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		response = new String(receivePacket.getData());
		System.out.println(response);
		
		
		while(state <3)
		{
			//sendData = new byte[1024];
			//receiveData = new byte[1024];
			switch(state)
			{
				case 0: 
					System.out.println(message);//send initial message to server and wait for response
	      				if (response.substring(0,3).equals("100")) 
					{                             
						state = 1;    //You are first client.  wait for second client to connect     
	                  		}                        
	 				else if (response.substring(0,3).equals("200"))
					{                             
						state = 2;  //you are second client.  Wait for message from first client
					}
					break;
					
				case 1: // Waiting for noticication that the second client is ready
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					response = new String(receivePacket.getData());
					System.out.println(response);
					//get message from user and send it to server
					sentence = inFromUser.readLine();
					sendData = sentence.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
					clientSocket.send(sendPacket);
					
					state = 2;//transition to state 2:chat mode
					break;

				case 2: //chat mode
					//recieve message from other client
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String modifiedSentence = new String(receivePacket.getData());
					
				
					//check for Goodbye message
					if (modifiedSentence.length()>=7 && modifiedSentence.substring(0,7).equals("Goodbye"))
					{
						state = 3;    //prepare to exit the while loop
						clientSocket.close();
						System.exit(0);
					}
					//if not Goodbye, get next message from user and send it;
					else
					{
						System.out.println("FROM SERVER:" + modifiedSentence);
						sentence = inFromUser.readLine();
						sendData = sentence.getBytes();
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						clientSocket.send(sendPacket);
					}
					//stay in state 2
					break; 
			}
			
			
		}
		
	}
}
