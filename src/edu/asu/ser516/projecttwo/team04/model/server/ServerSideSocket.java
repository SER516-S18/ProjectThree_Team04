package edu.asu.ser516.projecttwo.team04.model.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSideSocket {

    private final int PORT = 1516;

    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ServerSocket serverSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String inboundMessage;
    private String returnMessage;
    private Socket socket;
    private int[] intArray;

    public ServerSideSocket() throws Exception {

        initServerSocket();
        this.returnMessage = "Default Message";
        listen();
    }

    private void initServerSocket() throws Exception{

        this.serverSocket = new ServerSocket(getPORT());
    }

    private void initSocket() throws Exception{

        this.socket = serverSocket.accept();
    }

    private void initInputStream() throws Exception{

        this.inputStream = socket.getInputStream();
        this.inputStreamReader = new InputStreamReader(inputStream);
        this.bufferedReader = new BufferedReader(inputStreamReader);
    }

    private void initOutputStream() throws Exception {

        this.outputStream  = socket.getOutputStream();
        this.outputStreamWriter = new OutputStreamWriter(outputStream);
        this.bufferedWriter = new BufferedWriter(outputStreamWriter);
    }

    private int getPORT() {

        return PORT;
    }

    private void sendMessage(String returnMessage) throws Exception{

        try {

            initOutputStream();
            System.out.println();
            bufferedWriter.write(returnMessage);
            System.out.println("Datagram reply: " + returnMessage);
            bufferedWriter.flush();
            socket.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void receiveMessage() throws Exception{

        String[] stringArray;
        initSocket();
        initInputStream();

        inboundMessage = bufferedReader.readLine();
        System.out.print("Datagram Received: ");

        try {

            stringArray = inboundMessage.split(",");
            intArray = new int[stringArray.length];

            for (int i = 0; i < stringArray.length; i++) {
                String numberAsString = stringArray[i];
                intArray[i] = Integer.parseInt(numberAsString);
                System.out.print(intArray[i] + " ");
            }

        } catch (NumberFormatException e) {

            returnMessage = "Message Failure!\n";
        }
    }

    public void listen() throws Exception{
        System.out.println("Server is listening");

        while (true) {
            receiveMessage();
            sendMessage(returnMessage);
        }
    }


}
