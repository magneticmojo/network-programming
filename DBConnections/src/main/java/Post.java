import java.sql.Timestamp;

/**
 * Represents a post or comment made by a user in the guestbook.
 * Each post contains an ID, timestamp, name, email, website, and a comment.
 */
public class Post {

    private int id;
    private Timestamp timestamp;
    private final String name;
    private final String email;
    private final String website;
    private final String comment;

    /**
     * Constructs a new Post object without an ID and timestamp. Used when creating a post that has not
     * been saved to the database yet.
     *
     * @param name Name of the person making the post.
     * @param email Email of the person making the post.
     * @param website Website of the person making the post.
     * @param comment The actual content/comment.
     */
    public Post(String name, String email, String website, String comment) {
        this(-1, null, name, email, website, comment); // Use -1 for unset ID and null for unset timestamp
    }

    /**
     * Constructs a new Post object with the given details. For retrieval from database.
     *
     * @param id Unique identifier for the post.
     * @param timestamp The time when the post was made.
     * @param name Name of the person making the post.
     * @param email Email of the person making the post.
     * @param website Website of the person making the post.
     * @param comment The actual content/comment.
     */
    public Post(int id, Timestamp timestamp, String name, String email, String website, String comment) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.email = email;
        this.website = website;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getComment() {
        return comment;
    }
}
