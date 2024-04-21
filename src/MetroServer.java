import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MetroServer {
    private static final int PORT = 12345;
    private static final Map<String, Double> cardBalances = new HashMap<>();
    private static final Map<String, String> clientDatabase = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                String[] parts = request.split("\\|");
                String operation = parts[0];
                String cardID = parts[1];

                switch (operation) {
                    case "GET_INFO":
                        System.out.println("Client IDs in database: " + clientDatabase.keySet());
                        String clientInfo = getClientInfo(cardID);
                        out.println(clientInfo);
                        break;
                    case "ISSUE_CARD":
                        issueCard(cardID);
                        out.println("Card issued successfully.");
                        break;
                    case "TOP_UP":
                        double amount = Double.parseDouble(parts[2]);
                        topUpBalance(cardID, amount);
                        out.println("Balance topped up successfully.");
                        break;
                    case "PAY":
                        double fare = Double.parseDouble(parts[2]);
                        payForTrip(cardID, fare);
                        out.println("Trip paid successfully.");
                        break;
                    case "GET_BALANCE":
                        double balance = getBalance(cardID);
                        out.println("Balance on card " + cardID + ": " + balance);
                        break;
                    case "CLOSE_CONNECTION":
                        out.println("Closing connection...");
                        return;
                    default:
                        out.println("Invalid operation");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getClientInfo(String cardID) {

        String clientName = clientDatabase.getOrDefault(cardID, "Client not found");
        double balance = cardBalances.getOrDefault(cardID, 0.0);
        return "ID: " + cardID + "| Balance: " + balance;
    }


    private static void issueCard(String cardID) {

        clientDatabase.put(cardID, "New Client");
        cardBalances.put(cardID, 0.0);
    }

    private static void topUpBalance(String cardID, double amount) {

        double currentBalance = cardBalances.getOrDefault(cardID, 0.0);
        cardBalances.put(cardID, currentBalance + amount);
    }

    private static void payForTrip(String cardID, double fare) {

        double currentBalance = cardBalances.getOrDefault(cardID, 0.0);
        if (currentBalance >= fare) {
            cardBalances.put(cardID, currentBalance - fare);
        } else {
            System.out.println("Insufficient balance for trip.");
        }
    }

    private static double getBalance(String cardID) {

        return cardBalances.getOrDefault(cardID, 0.0);
    }
}

