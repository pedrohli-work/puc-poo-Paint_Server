import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Servidor {

    public static void main(String[] args) {
        int porta = 27017;

        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    System.out.println("Erro ao aceitar conexão do cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Não foi possível iniciar o servidor: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
            Janela desenho = (Janela) inputStream.readObject();
            salvarDesenhoNoMongoDB(desenho);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao processar a conexão do cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar socket do cliente: " + e.getMessage());
            }
        }
    }

    private void salvarDesenhoNoMongoDB(Janela desenho) {
        try (MongoClient mongoClient = MongoClients
                .create("mongodb+srv://pedrohliedu:if0DCZoTailaemEe@cluster0.vtnlrbv.mongodb.net/")) {
            MongoDatabase database = mongoClient.getDatabase("POO_JAVA");
            MongoCollection<Document> collection = database.getCollection("desenhos");

            Document doc = new Document("desenho", desenho.toString());
            collection.insertOne(doc);
        } catch (Exception e) {
            System.out.println("Erro ao salvar no MongoDB: " + e.getMessage());
        }
    }
}
