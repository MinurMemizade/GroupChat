import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws IOException {
            while(!serverSocket.isClosed())
            {
                Socket socket=serverSocket.accept();
                System.out.println("A new client connected.");
                ClientHandler clientHandler=new ClientHandler(socket);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        }

        public void closeServer() throws Exception
        {
            if(serverSocket!=null)
            {
                serverSocket.close();
            }
        }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket=new ServerSocket(1234);
        Server server=new Server(serverSocket);
        server.startServer();
    }
}
