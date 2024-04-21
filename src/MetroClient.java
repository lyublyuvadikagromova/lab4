import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class MetroClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private final int clientID;

    public MetroClient(int clientID) {
        this.clientID = clientID;
    }

    public static void main(String[] args) {
        MetroClient client = new MetroClient(123456);
        client.start();
    }

    private void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            while (true) {
                System.out.println("Enter operation (GET_INFO|cardID):");
                String request = userInput.readLine();
                out.println(request);

                String response = in.readLine();
                System.out.println("Response from server: " + response);

                if (response == null) {
                    System.out.println("Connection closed by server.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
