import javax.swing.*;
import java.awt.*;

/**
 * A graphical user interface for sending emails. Users can input email details,
 * choose between a default email server or provide custom configurations, and send emails.
 */
public class EmailSenderGUI extends JFrame {

    private JTextField toField, subjectField, emailServerField, portNumberField, usernameField, passwordField;
    private JTextArea messageField;
    private JButton submitButton;
    private EmailService emailService;
    private JRadioButton defaultServerButton;
    private JRadioButton customConfigButton;

    /**
     * Constructs a new EmailSenderGUI with the given EmailService.
     *
     * @param service The service responsible for sending emails.
     */
    public EmailSenderGUI(EmailService service) {
        this.emailService = service;

        setTitle("Email Sender");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(false);

        GridBagConstraints constraints = createDefaultConstraints();

        initializeFields(constraints);
        initializeRadioButtons(constraints);
        initializeSubmitButton(constraints);

        pack();
    }

    /**
     * Creates and returns a default set of GridBagConstraints.
     * These constraints are commonly used throughout the GUI setup.
     *
     * @return The default GridBagConstraints.
     */
    private GridBagConstraints createDefaultConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(1, 10, 1, 10);
        return constraints;
    }

    /**
     * Initializes and adds the fields (To, Subject, Message, Email Server, Port Number,
     * Username, and Password) to the layout with the provided constraints.
     *
     * @param constraints The GridBagConstraints to be used for component placement.
     */
    private void initializeFields(GridBagConstraints constraints) {
        addToLayout(new JLabel("To:"), 0, 0, constraints);
        toField = new JTextField(20);
        addToLayout(toField, 1, 0, constraints);

        addToLayout(new JLabel("Subject:"), 0, 1, constraints);
        subjectField = new JTextField(20);
        addToLayout(subjectField, 1, 1, constraints);

        addToLayout(new JLabel("Message:"), 0, 2, constraints);
        messageField = new JTextArea(5, 20);
        constraints.fill = GridBagConstraints.BOTH;
        addToLayout(new JScrollPane(messageField), 1, 2, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL; // Reset to original fill constraint

        addToLayout(new JLabel("Email Server:"), 0, 5, constraints);
        emailServerField = new JTextField(20);
        addToLayout(emailServerField, 1, 5, constraints);

        addToLayout(new JLabel("Port Number:"), 0, 6, constraints);
        portNumberField = new JTextField(20);
        addToLayout(portNumberField, 1, 6, constraints);

        addToLayout(new JLabel("Username:"), 0, 7, constraints);
        usernameField = new JTextField(20);
        addToLayout(usernameField, 1, 7, constraints);

        addToLayout(new JLabel("Password:"), 0, 8, constraints);
        passwordField = new JTextField(20);
        addToLayout(passwordField, 1, 8, constraints);
    }

    /**
     * Initializes and adds the radio buttons (Default Server and Custom Configuration)
     * to the layout with the provided constraints. It also sets up action listeners to
     * toggle the visibility of custom configuration fields.
     *
     * @param constraints The GridBagConstraints to be used for component placement.
     */
    private void initializeRadioButtons(GridBagConstraints constraints) {
        defaultServerButton = new JRadioButton("Use Default Server", true);
        addToLayout(defaultServerButton, 0, 3, constraints);

        customConfigButton = new JRadioButton("Custom Configuration");
        addToLayout(customConfigButton, 1, 3, constraints);

        ButtonGroup group = new ButtonGroup();
        group.add(defaultServerButton);
        group.add(customConfigButton);

        defaultServerButton.addActionListener(e -> toggleCustomConfigFields(false));
        customConfigButton.addActionListener(e -> toggleCustomConfigFields(true));

        toggleCustomConfigFields(false);
    }

    /**
     * Initializes and adds the submit button to the layout with the provided constraints.
     * It also sets up an action listener to handle email submissions when clicked.
     *
     * @param constraints The GridBagConstraints to be used for component placement.
     */
    private void initializeSubmitButton(GridBagConstraints constraints) {
        submitButton = new JButton("Submit");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        addToLayout(submitButton, 0, 9, constraints);

        submitButton.addActionListener(e -> handleSubmit());
    }

    /**
     * Adds a component to the layout using the provided GridBagConstraints.
     *
     * @param comp         The component to be added.
     * @param x            The x-coordinate for the grid position.
     * @param y            The y-coordinate for the grid position.
     * @param constraints  The GridBagConstraints to be used for component placement.
     */
    private void addToLayout(Component comp, int x, int y, GridBagConstraints constraints) {
        constraints.gridx = x;
        constraints.gridy = y;
        add(comp, constraints);
    }

    /**
     * Toggles the visibility of the custom configuration fields based on the provided value.
     *
     * @param enable True to enable and show the custom configuration fields; false to disable them.
     */
    private void toggleCustomConfigFields(boolean enable) {
        emailServerField.setEnabled(enable);
        portNumberField.setEnabled(enable);
        usernameField.setEnabled(enable);
        passwordField.setEnabled(enable);
    }

    /**
     * Handles the email submission from the GUI.
     * 1. Constructs an `Email` using user inputs.
     * 2. Sets up a `Configuration` with either default or custom server settings.
     * 3. If custom settings are chosen, validates and uses them. Errors, like invalid ports,
     *    prompt user notifications.
     * 4. Uses an `EmailWorker` to asynchronously send the email.
     * 5. Captures and notifies the user of any `InvalidEmailException` occurrences.
     */
    private void handleSubmit() {
        try {
            Email email = new Email(toField.getText(), subjectField.getText(), messageField.getText());

            Configuration config = new Configuration();

            if (customConfigButton.isSelected()) {
                // Use the custom configuration
                config.setEmailServer(emailServerField.getText());
                try {
                    config.setPort(Integer.parseInt(portNumberField.getText()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                config.setUsername(usernameField.getText());
                config.setPassword(passwordField.getText());
            }
            // If "Use Default Server" is selected, the Configuration object will use its default values

            new EmailWorker(emailService, email, config).execute();

        } catch (InvalidEmailException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Email", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Entry point for the EmailSenderGUI application. This method sets up the Swing
     * environment, initializes the email service, and displays the main GUI frame.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmailService service = new EmailService();
            EmailSenderGUI frame = new EmailSenderGUI(service);
            frame.setVisible(true);
        });
    }
}

