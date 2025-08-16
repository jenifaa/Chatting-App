import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHelperClient {
    private static final String DATABASE_NAME = "Chat";
    private static final String COLLECTION_NAME = "Client";
    
    static {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
    }

    static String getConnectionString() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("config.properties"); 
            props.load(fis);
            String username = props.getProperty("MONGO_USER");
            String password = props.getProperty("MONGO_PASS");
            String cluster = props.getProperty("MONGO_CLUSTER", "cluster0.5lka3.mongodb.net");
            fis.close();

            return String.format("mongodb+srv://%s:%s@%s/?retryWrites=true&w=majority", 
                               username, password, cluster);

        } catch (Exception e) {
            System.err.println("Error reading database credentials: " + e.getMessage());
            return null;
        }
    }

    public static boolean insertUser(String username, String fullName, String email, String password) {
        String connectionString = getConnectionString();
        if (connectionString == null) {
            System.err.println("Could not read database credentials!");
            return false;
        }

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

          
            if (collection.countDocuments(new Document("$or", Arrays.asList(
                    new Document("username", username),
                    new Document("email", email)
            ))) > 0) {
                System.err.println("User with this username or email already exists");
                return false;
            }

            Document doc = new Document("username", username)
                                .append("fullName", fullName)
                                .append("email", email)
                                .append("password", password); 

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
    
    public static Document getUserByUserIDAndPassword(String userID, String password) {
        String connectionString = getConnectionString();
        if (connectionString == null) return null;

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document query = new Document();
            query.put("password", password);
            query.put("$or", Arrays.asList(
                new Document("username", userID),
                new Document("email", userID)
            ));

            return collection.find(query).first();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean resetPassword(String userID, String newPassword) {
        String connectionString = getConnectionString();
        if (connectionString == null) {
            System.err.println("Could not read database credentials!");
            return false;
        }

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document result = collection.findOneAndUpdate(
                new Document("$or", Arrays.asList(
                    new Document("username", userID),
                    new Document("email", userID)
                )),
                new Document("$set", new Document("password", newPassword))
            );

            return result != null; 
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public static boolean checkUserExists(String userID) {
        String connectionString = getConnectionString();
        if (connectionString == null) {
            System.err.println("Could not read database credentials!");
            return false;
        }

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document user = collection.find(
                new Document("$or", Arrays.asList(
                    new Document("username", userID),
                    new Document("email", userID)
                ))
            ).first();

            return user != null;
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }
}