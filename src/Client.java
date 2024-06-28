import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        }catch (Exception e)
        {
            closeEverything(socket,bufferedWriter,bufferedReader);
        }

    }

    public void sendMessage() { //mesaj gonderir
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = sc.nextLine();
                bufferedWriter.write(userName + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (Exception e)
        {
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
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

    public void listenMessage() //Client in basqa clientlarin mesajini gormeye yariyir
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while(socket.isConnected())
                {
                    try {
                        messageFromGroupChat=bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (Exception e)
                    {
                        closeEverything(socket,bufferedWriter,bufferedReader);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter your username:");
        String username=sc.nextLine();
        Socket socket=new Socket("localhost",1234);
        Client client=new Client(socket,username);
        client.listenMessage();
        client.sendMessage();
    }
}
