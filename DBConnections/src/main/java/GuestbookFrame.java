import javax.swing.*;
import java.awt.*;

/**
 * Represents a graphical interface for a guestbook application where users can submit comments.
 * The frame allows users to input their name, email, website, and a comment. Submitted comments
 * are sanitized to prevent malicious inputs and then saved into a database.
 */
public class GuestbookFrame extends JFrame {

    private JTextField nameField, emailField, websiteField;
    private JTextArea commentArea, displayArea;
    private JButton submitButton;
    private DBService databaseService;

    /**
     * Constructs a GuestbookFrame that interacts with the given database service.
     *
     * @param service The database service to be used for data storage and retrieval.
     */
    public GuestbookFrame(DBService service) {
        this.databaseService = service;

        initializeFrame();
        setupLayoutConstraints();
        setupActionListeners();

        pack();
    }

    private void initializeFrame() {
        setTitle("Guestbook");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
    }

    private void setupLayoutConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        setupNameField(constraints);
        setupEmailField(constraints);
        setupWebsiteField(constraints);
        setupCommentArea(constraints);
        setupSubmitButton(constraints);
        setupDisplayArea(constraints);
    }

    private void setupNameField(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Name:"), constraints);
        constraints.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, constraints);
    }

    private void setupEmailField(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(new JLabel("Email:"), constraints);
        constraints.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, constraints);
    }

    private void setupWebsiteField(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel("Website:"), constraints);
        constraints.gridx = 1;
        websiteField = new JTextField(20);
        add(websiteField, constraints);
    }

    private void setupCommentArea(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(new JLabel("Comment:"), constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        commentArea = new JTextArea(5, 20);
        add(new JScrollPane(commentArea), constraints);
    }

    private void setupSubmitButton(GridBagConstraints constraints) {
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        submitButton = new JButton("Submit");
        add(submitButton, constraints);
    }

    private void setupDisplayArea(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        displayArea = new JTextArea(10, 20);
        add(new JScrollPane(displayArea), constraints);
    }

    /**
     * Configures and sets up the main action listener for the submit button.
     * On action, it sanitizes input data, creates a {@link Post} object and
     * saves it to the database.
     */
    private void setupActionListeners() {
        submitButton.addActionListener(e -> handleSubmit());
    }

    /**
     * Handles the submission of the form. This method does the following steps:
     * 1. Sanitizes the user input from the various fields using the {@link HTMLSanitizer} class.
     * 2. Creates a new {@link Post} object with the sanitized input.
     * 3. Initializes a new {@link DBWorker} task to asynchronously save the post to the database
     *    and fetch all existing posts.
     * 4. Executes the DBWorker task, which will eventually update the display area with all posts.
     */
    private void handleSubmit() {
        String name = HTMLSanitizer.sanitize(nameField.getText());
        String email = HTMLSanitizer.sanitize(emailField.getText());
        String website = HTMLSanitizer.sanitize(websiteField.getText());
        String comment = HTMLSanitizer.sanitize(commentArea.getText());

        Post post = new Post(name, email, website, comment);
        new DBWorker(databaseService, post, displayArea).execute();
    }

    /**
     * The main entry point of the Guestbook application.
     * This method initiates the application by performing the following steps:
     * 1. It ensures that the creation and display of the GUI occurs on the Event Dispatch Thread (EDT) using
     *    `SwingUtilities.invokeLater`.
     * 2. It initializes the database service using `MySQLDBService`, which is an implementation of the `DBService`
     *    interface. This service provides the required functionality for database interactions.
     * 3. It creates an instance of the `GuestbookFrame`, passing the database service to it. The `GuestbookFrame`
     *    is the main window of the application where users can input their details and view existing posts.
     * 4. It sets the frame to be visible, which displays the application window to the user.
     *
     * @param args Command-line arguments, currently not utilized in this application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DBService service = new MySQLDBService();
            GuestbookFrame frame = new GuestbookFrame(service);
            frame.setVisible(true);
        });
    }
}
