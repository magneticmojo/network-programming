import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * An asynchronous worker that interacts with the database to save a {@link Post} and then fetches
 * all posts. This worker uses SwingWorker to ensure that long-running tasks (like database interactions)
 * do not block the Event Dispatch Thread (EDT) ensuring that the GUI remains responsive.
 * Once the task is complete, it updates the provided JTextArea with the fetched posts.
 */
public class DBWorker extends SwingWorker<List<Post>, Void> {
    private DBService databaseService;
    private Post post;
    private JTextArea displayArea;

    /**
     * Constructs a new {@link DBWorker} with the provided parameters.
     *
     * @param service The database service to interact with.
     * @param post The post to be saved.
     * @param displayArea The JTextArea to display the posts.
     */
    public DBWorker(DBService service, Post post, JTextArea displayArea) {
        this.databaseService = service;
        this.post = post;
        this.displayArea = displayArea;
    }

    /**
     * The main computation performed by this worker thread. This method:
     * 1. Saves the provided post to the database using the database service.
     * 2. Fetches all posts from the database.
     *
     * @return A list of posts fetched from the database.
     */
    @Override
    protected List<Post> doInBackground() {
        databaseService.savePost(post);
        return databaseService.getAllPosts();
    }

    /**
     * Executed on the Event Dispatch Thread after the {@code doInBackground}
     * computation is finished. This method does the following:
     * 1. Fetches the posts computed from the doInBackground method.
     * 2. Formats the posts and appends them to a StringBuilder.
     * 3. Sets the displayArea's text to the formatted posts.
     * If any exceptions were encountered during the doInBackground execution,
     * they are caught and printed.
     */
    @Override
    protected void done() {
        try {
            List<Post> posts = get();
            StringBuilder sb = new StringBuilder();
            for (Post post : posts) {
                String formattedText = String.format("NO: %d TIME: %s\nNAME: %s EMAIL: %s HOMEPAGE: %s\nCOMMENT: %s\n\n",
                        post.getId(), post.getTimestamp(), post.getName(), post.getEmail(), post.getWebsite(), post.getComment());
                sb.append(formattedText);
            }
            displayArea.setText(sb.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
