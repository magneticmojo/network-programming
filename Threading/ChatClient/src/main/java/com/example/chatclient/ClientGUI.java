package com.example.chatclient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    private Client client;
    private String host;
    private int port;

    public static void main(String[] args) {
        // Check the number of command-line arguments and extract host and port
        if (args.length >= 2) {
            String host = args[0];
            int port;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port.");
                port = Client.DEFAULT_PORT;
            }

            // Launch the application and pass the host and port as parameters
            launch(ClientGUI.class, "--host=" + host, "--port=" + port);
        } else {
            launch(ClientGUI.class);
        }
    }

/*    @Override
    public void init() {
        // Retrieve the host and port parameters
        Parameters params = getParameters();

        // Check if the host and port parameters are not null before parsing
        if (params.getNamed().get("host") != null) {
            host = params.getNamed().get("host");
        }

        if (params.getNamed().get("port") != null) {
            try {
                port = Integer.parseInt(params.getNamed().get("port"));
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port.");
                port = Client.DEFAULT_PORT;
            }
        }
    }*/


    @Override
    public void start(Stage primaryStage) {
        // Create a new Stage for the pop-up
        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);


        // Create TextFields for user input
        TextField hostField = new TextField();
        TextField portField = new TextField();
        TextField aliasField = new TextField();

        hostField.setPromptText("Host");
        portField.setPromptText("Port");
        aliasField.setPromptText("Alias");


        // Create a Button for the user to submit their input
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText()); // Add error handling for non-integer input
            String alias = aliasField.getText();

            // Initialize the client with the entered information
            client = new Client(host, port, alias);

            // Close the pop-up stage and show the main application window
            popupStage.close();

            // Proceed with setting up the main application window
            // For example: setupMainApplicationWindow(primaryStage);
        });

        // Arrange the TextFields and Button in a layout
        VBox vbox = new VBox(5, hostField, portField, aliasField, connectButton);

        // Set the pop-up Stage's scene with the layout
        Scene popupScene = new Scene(vbox);
        popupStage.setScene(popupScene);

        // Show the pop-up Stage
        popupStage.showAndWait();

        // After the pop-up is closed, continue with setting up the main application window if the client is initialized
        if (client != null) {
            setupMainApplicationWindow(primaryStage);
        }
    }

    // Method to setup the main application window
    private void setupMainApplicationWindow(Stage primaryStage) {
        // Create UI components for the main application window
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);
        TextField inputField = new TextField();
        Button sendButton = new Button("Send");

        // Handle button click for sending messages
        sendButton.setOnAction(e -> {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                /*client.sendMessage(message);*/
                inputField.clear();
            }
        });

        VBox vbox = new VBox(5, messageArea, inputField, sendButton);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 400);

        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
