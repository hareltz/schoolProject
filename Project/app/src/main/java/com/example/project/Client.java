/*
package com.example.project;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost"; // const
    private static final int SERVER_PORT = 8666; // const
    private static Socket socket;
    private static Boolean userLogged = false;
    private static Boolean socketOpen = false;

    public Client()
    {
        startConnection();
    }

    // this function opens a socket to the DB server
    private void startConnection()
    {
        */
/*if (socketOpen) { return; } // continue if the socket is open
        try {
            //socket = new Socket(SERVER_IP, SERVER_PORT);
            //socketOpen = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*//*

    }
    //////////////////////////////////////////////
    */
/*String hostName = "localhost"; // Change this if the server is running on a different host
    int portNumber = 8080;*//*


    // with this function the user can login
    public void Login(String username, String password)
    {
        */
/*if (!isValidPassword(password))
        {
            throw new RuntimeException("invalid password");
        }

        String msg = String.format("210@%s|||%s", username, password);
        sendMessage(msg);

        String res = getMessage();

        if (res.startsWith("510@001")) // wrong password
        {
            throw new RuntimeException("invalid password");
        }
        else if (res.startsWith("510@002")) // wrong username
        {
            throw new RuntimeException("invalid username");
        }*//*

    }

    // with this function the user can Register
    public void Register (String username, String password, String email, String phoneNumber)
    {
        if (!isValidPassword(password))
        {
            throw new RuntimeException("invalid password");
        }

        String msg = String.format("211@%s|||%s|||%s", username, email, password);
        sendMessage(msg);

        String res = getMessage();

        if (res.startsWith("511@001")) // wrong password
        {
            throw new RuntimeException("invalid password");
        }
        else if (res.startsWith("511@002")) // wrong email
        {
            throw new RuntimeException("invalid email");
        }
        else if (res.startsWith("511@003")) // wrong username
        {
            throw new RuntimeException("user with this username already exist!");
        }
    }

    // with this function the user can logout
    public void Logout()
    {
        String msg = "213@bye";
        sendMessage(msg);

        String res = getMessage();

        if (!res.startsWith("513@bye")) // wrong password
        {
            throw new RuntimeException("unknown error");
        }
    }

    // this function create a new table
    public void createTable(String data)
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

    protected static boolean isValidPassword(String password) {
        // Password criteria: At least 8 characters, contains at least one uppercase letter,
        // one lowercase letter, one digit, and one special character
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else {
                // Consider any character other than letter or digit as a special character
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private void sendMessage(String msg)
    {
        try
        {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(msg);
            outputStream.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private String getMessage()
    {
        try
        {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String responseData = inputStream.readUTF();

            return responseData;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
*/
