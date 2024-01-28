import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * An implementation of the {@link DBService} interface providing database
 * interaction services specific to MySQL. This service reads configurations
 * from a properties file and provides methods to save posts and retrieve
 * all posts from a MySQL database.
 */
public class MySQLDBService implements DBService {

    private static final String CONFIG_FILE = "/config.properties";
    private static final Properties prop = loadProperties();
    private static final String DB_URL = prop.getProperty("db.url");
    private static final String USER = prop.getProperty("db.user");
    private static final String PASS = prop.getProperty("db.password");

    /**
     * Loads the database configuration properties from the specified file {@link #CONFIG_FILE}.
     * The properties file should contain the database URL, user, and password required for
     * establishing the connection to the MySQL database.
     *
     * @return A {@link Properties} object populated with the database configuration.
     * @throws RuntimeException If the properties file is not found or there is an error loading the properties.
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = MySQLDBService.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config from " + CONFIG_FILE, e);
        }
        return properties;
    }

    /**
     * Saves a given {@link Post} object to the MySQL database.
     * This method inserts the details of the post, including name, email, website,
     * and comment into the 'guestbook' table of the database.
     *
     * @param post The {@link Post} object containing details to be saved to the database.
     */
    @Override
    public void savePost(Post post) {
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "INSERT INTO guestbook (name, email, website, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, post.getName());
            statement.setString(2, post.getEmail());
            statement.setString(3, post.getWebsite());
            statement.setString(4, post.getComment());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all the {@link Post} entries from the 'guestbook' table in the MySQL database.
     *
     * @return A list of {@link Post} objects, representing all entries from the 'guestbook' table.
     *         An empty list is returned if there are no entries.
     */
    @Override
    public List<Post> getAllPosts() { // Change the return type here
        List<Post> posts = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT id, timestamp, name, email, website, comment FROM guestbook";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("id");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String website = rs.getString("website");
                String comment = rs.getString("comment");

                Post post = new Post(id, timestamp, name, email, website, comment);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
