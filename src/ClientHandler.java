import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers=new ArrayList<>();
    private String userName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public ClientHandler(Socket socket)
    {
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName=bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server: "+userName+" entered the chat.");
        }catch (Exception e)
        {
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    @Override
    public void run() {  //client in mesajin servere gonderir
        String messageFromClient;
        while(socket.isConnected())
        {
            try {
                messageFromClient=bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }

        }
    }

    private void broadcastMessage(String message) { //yazilan mesaji servere gonderiri
        try {
            for(ClientHandler clientHandler:clientHandlers)
            {
                if(!clientHandler.userName.equals(userName))
                {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }
        }catch (Exception e)
        {
            closeEverything(socket,bufferedWriter,bufferedReader);

        }
    }

    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        broadcastMessage("Server:"+userName+" has left the chat.");
    }

    public void closeEverything(Socket socket,BufferedWriter bufferedWriter,BufferedReader bufferedReader)
    {
        try {
            if(socket!=null) socket.close();
            if(bufferedReader!=null) bufferedReader.close();
            if(bufferedWriter!=null) bufferedWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
