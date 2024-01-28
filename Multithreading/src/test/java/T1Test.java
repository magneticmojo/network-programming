import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class T1Test {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testT1ThreadRunsAndInterrupts() throws InterruptedException {
        T1 t1 = new T1("ThreadT1");
        t1.start();

        // Let the thread run for 3 seconds
        Thread.sleep(3000);

        t1.interrupt();

        // Give it a bit of time to handle the interruption
        Thread.sleep(100);

        assertTrue(outContent.toString().contains("ThreadT1"));
    }
}


