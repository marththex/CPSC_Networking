/**
*	Email Program
*	Connects to a TCP Server
*	Receives a line of input from the keyboard and sends it to the server
*	Receives a response from the server and displays it.
*
*	@author: Marcus Chong
@	version: 2.1
*/

import java.io.*;
import java.net.*;
import java.util.Arrays;

class Email {

    public static void main(String argv[]) throws Exception
    {
        String sentence = "";
        String modifiedSentence;
	String from;
	String to;
	String subject;
	String[] message = new String[50];
	int count = 0;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = null;

		try
		{
			clientSocket = new Socket("smtp.chapman.edu", 25);
		}

		catch(Exception e)
		{
			System.out.println("Failed to open socket connection");
			System.exit(0);
		}

        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
		    BufferedReader inFromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	
	System.out.print("From: ");
	from = inFromUser.readLine();
	System.out.print("To: ");
	to = inFromUser.readLine();
	System.out.print("Subject: ");
	subject = inFromUser.readLine();
	System.out.print("Message(Max: 50 Lines): ");
	while(true)
        {
            sentence = inFromUser.readLine().toString();

            //Check for DONE indication
            if(sentence.length() >= 1)
                if (sentence.substring(0,1).equals("."))
                    break;

            //Append line to message array, increase line count 
            message[count] = sentence;
            ++count;

            //Check if max line count is reached
            if(count == 50)
            {
                System.out.println("Max line count reached - sending message as is.");
                break;
            }
        }
	//Sends info to SMTP server
	outToServer.println("HELO llb16.chapman.edu");
	modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

	outToServer.println("MAIL FROM: " + from);
	modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

	outToServer.println("RCPT TO: " + to);
	modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

	outToServer.println("DATA");
	modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

	outToServer.println("From: " + from);
	outToServer.println("To: " + to);
	outToServer.println("Subject: " + subject);

	for(int i = 0; i < count; ++i)
	{
		outToServer.println(message[i]);
	}
	outToServer.println(".");
	modifiedSentence = inFromServer.readLine();
        System.out.println(modifiedSentence);

	outToServer.println("QUIT");
	System.out.println("SENT");


        clientSocket.close();

    }
}
