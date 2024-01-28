/*
import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testNoArgs() {
        Client client = new Client("dummy", 1234);
        assertEquals("no args\n", errContent.toString());
    }




}
*/
