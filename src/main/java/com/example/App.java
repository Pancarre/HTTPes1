package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ServerSocket Server;
        Socket client;
        boolean vita = true;
        boolean exist;
        try{
            
            Server = new ServerSocket(3000);
             
            ArrayList<String> listaString = new ArrayList<>();        
            String receive;

            while(vita){
                client = Server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                receive = in.readLine();
                String[] str = receive.split(" ");
                String str2 = str[1].substring(1);

                do{

                    receive = in.readLine();
                    listaString.add(receive);
                    System.out.println(receive);
                    

                }while(!receive.isEmpty());

                File f = (new File("test/"+str2));
                /*if(exist){

                    System.out.println("file trovato");

                    String msg = "file trovato";
                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Length: " + msg.length() + "\n");
                    out.writeBytes("Content-Type: text/pain\n");

                    out.writeBytes("\n");
                    out.writeBytes(msg);


                }
                */

                if(f.exists()){

                    sendBinaryFile(client, f);

                }

                else{
                    System.out.println("file non trovato");
                    String msg = "il file non esiste";
                    out.writeBytes("HTTP/1.1 404 Not found\n");
                    out.writeBytes("Content-Length: " + msg.length() + "\n");
                    out.writeBytes("Content-Type: text/pain\n");
                    out.writeBytes("\n");
                    out.writeBytes(msg);
                }

                client.close();

            }
            

        }
        catch(Exception e){

            System.out.println(e.getMessage());
            System.exit(1);


        }
        
    }

    private static void sendBinaryFile(Socket s, File f) throws Exception{

        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeBytes("HTTP/1.1 200 OK\n");
        out.writeBytes("Content-Length: " + f.length() + "\n");

        out.writeBytes("Content-Type: " + getFileExtension(f) + "\n");

        out.writeBytes("\n");

        InputStream in = new FileInputStream(f);
        byte[] buf = new byte[8192];
        int n;
        while((n = in.read(buf)) != -1){

            out.write(buf, 0 , n);

        }

        in.close();

    }

    private static String getFileExtension(File f){

        String fname[] = f.getName().split("\\.");
        String ext = fname[fname.length-1];
        switch (ext){
            case "html":
            case "htm":

                return "text/html";

            case "png":
            
                return "image/png";
            case "jpeg":
            
                return "image/jpeg";
            case "css":

                return "text/css";

            default:

                return "";
                
        }



    }


}