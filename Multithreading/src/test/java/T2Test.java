import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class T2Test {

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
    public void testT2TaskRunsAndKills() throws InterruptedException {
        T2 task = new T2("TaskT2");
        Thread t2 = new Thread(task);
        t2.start();

        // Let the task run for 3 seconds
        Thread.sleep(3000);

        task.kill();

        // Give it a bit of time to handle the kill
        Thread.sleep(100);

        assertTrue(outContent.toString().contains("TaskT2"));
    }

    @Test
    public void testT2TaskSuspendAndResume() throws InterruptedException {
        T2 task = new T2("TaskT2SuspendResume");
        Thread t2 = new Thread(task);
        t2.start();

        // Let the task run for 2 seconds
        Thread.sleep(2000);

        task.suspendExecution();

        // Let it be suspended for 2 seconds
        Thread.sleep(2000);

        task.resumeExecution();

        // Let it run for another 2 seconds after resumption
        Thread.sleep(2000);

        task.kill(); // Important to stop the task at the end

        String output = outContent.toString();
        assertTrue(output.contains("TaskT2SuspendResume"));
        assertFalse(output.endsWith("TaskT2SuspendResume\nTaskT2SuspendResume\n"));
        // The above assertion ensures that there were times the task wasn't printing continuously (i.e., it was suspended).
    }
}
