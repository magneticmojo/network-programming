import java.util.List;

/**
 * Represents a contract for a database service that provides functionalities
 * to save a post and retrieve all posts.
 */
public interface DBService {
    void savePost(Post post);
    List<Post> getAllPosts();
}
