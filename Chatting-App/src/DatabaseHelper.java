import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

public class DatabaseHelper {
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
        if (connectionString == null) return false;

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection("users");

           
            Document existing = collection.find(new Document("email", email)).first();
            if (existing != null) {
                System.out.println("Email already registered!");
                return false;
            }

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

//    public static Document getUserByEmailAndPassword(String email, String password) {
//        String connectionString = getConnectionString();
//        if (connectionString == null) return null;
//
//        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
//            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
//            MongoCollection<Document> collection = database.getCollection("users");
//
//            return collection.find(new Document("email", email)
//                                   .append("password", password)).first();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
//    public static void insertMessage(String email, String messageText) {
//        String connectionString = getConnectionString();
//        if (connectionString == null) return;
//
//        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
//            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
//            MongoCollection<Document> collection = database.getCollection("messages");
//
//            Document doc = new Document("email", email)
//                                .append("text", messageText)
//                                .append("timestamp", new Date());
//
//            collection.insertOne(doc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static Iterable<Document> getMessagesByEmail(String email) {
//        String connectionString = getConnectionString();
//        if (connectionString == null) return null;
//
//        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
//            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
//            MongoCollection<Document> collection = database.getCollection("messages");
//
//            return collection.find(new Document("email", email));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
