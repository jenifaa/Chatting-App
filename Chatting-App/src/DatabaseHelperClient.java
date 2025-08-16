import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

public class DatabaseHelperClient {
    private static final String DATABASE_NAME = "Chat";

     static String getConnectionString() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties"); 
            props.load(fis);
            String username = props.getProperty("MONGO_USER");
            String password = props.getProperty("MONGO_PASS");
            fis.close();

            return "mongodb+srv://" + username + ":" + password + "@cluster0.5lka3.mongodb.net/?retryWrites=true&w=majority";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean insertUser(String username, String fullName, String email, String password) {
        String connectionString = getConnectionString();
        if (connectionString == null) {
            System.out.println("Could not read database credentials!");
            return false;
        }

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("Client");

            Document doc = new Document("username", username)
                                .append("fullName", fullName)
                                .append("email", email)
                                .append("password", password); 

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Document getUserByUserIDAndPassword(String userID, String password) {
        String connectionString = getConnectionString();
        if (connectionString == null) return null;

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("Client");

            
            Document user = collection.find(
                new Document("$and", Arrays.asList(
                    new Document("$or", Arrays.asList(
                        new Document("username", userID),
                        new Document("email", userID)
                    )),
                    new Document("password", password)
                ))
            ).first();

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
