import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MongoDBTest {
    public static void main(String[] args) {
        try {
         
            Properties props = new Properties();
            InputStream input = new FileInputStream("config.properties"); 
            props.load(input);

            String username = props.getProperty("MONGO_USER");
            String password = props.getProperty("MONGO_PASS");

           
            String cluster = "cluster0.mongodb.net"; 
            String databaseName = "Chat"; 
            String uri = "mongodb+srv://" + username + ":" + password + "@cluster0.5lka3.mongodb.net/" 
                         + databaseName + "?retryWrites=true&w=majority&appName=Cluster0";


            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase(databaseName);

            MongoCollection<Document> collection = database.getCollection("myCollection");

      
            Document doc = new Document("name", "Jenifa")
                    .append("age", 20)
                    .append("city", "Dhaka");
            collection.insertOne(doc);

            System.out.println("Document inserted successfully!");

            mongoClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
