package com.example.project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
    protected static final String SERVER_IP = "127.0.0.1"; // const
    protected static final int SERVER_PORT = 8827; // const

    private Socket socket;
    private Boolean userLogged = false;


    // this function opens a socket to the DB server
    public void startConnection()
    {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // insert logic
    }

    // send a login message to the server
    public void Login(String username, String password)
    {
        try
        {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            String msg = "msg";
            outputStream.writeUTF(msg);
            outputStream.flush();

            String responseData = inputStream.readUTF();

            if (responseData.startsWith("69"))
            {
                throw new RuntimeException("bruh");
            }
            userLogged = true;

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


}
