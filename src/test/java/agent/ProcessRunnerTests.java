package agent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.ssehub.recommender.agent.ExecutionFailedException;
import net.ssehub.recommender.agent.ProcessResponse;
import net.ssehub.recommender.agent.ProcessRunner;

class ProcessRunnerTests {

    @Test
    @DisplayName("Test for simple response correctness")
    public void readResponseTest() {
        ProcessResponse result = new ProcessRunner("echo hi").execute();
        assertEquals("hi", result.getContent());
    }
    
    @Test
    @DisplayName("Test if exit code is 0 when execution succeeded") 
    public void checkSuccessExitCode() {
        ProcessResponse result = new ProcessRunner("echo hi").execute();
        assertEquals(0, result.getExitCode());
    }
    
    @Test
    @DisplayName("Test if execution fails with exception when command not existing")
    public void failedCmdTest() {
        assertThrows(ExecutionFailedException.class, () -> new ProcessRunner("jdkhsfsadjhfkj").execute());
    }
    
    @Test
    @DisplayName("Test if execution is interrupted when taking too long")
    public void timeoutCmdTest() {
        assertThrows(ExecutionFailedException.class, 
                () -> new ProcessRunner("sleep 100").setMaxWaitMs(1000).execute());
    }
    
    @Test
    @DisplayName("Test if execution is not interrupted when no limit is set")
    public void waitForResponseTest() {
        assertDoesNotThrow(() -> new ProcessRunner("sleep 5").execute());
        assertDoesNotThrow(() -> new ProcessRunner("sleep 5").setMaxWaitMs(100000).execute());
    }
    
    @Test
    @DisplayName("Test if partial response content on fail is available")
    public void checkPartialResponseContent() {
        assumeTrue(new ProcessRunner("bash -c exit").execute().getExitCode() == 0); // bash installed
        
        try {
            new ProcessRunner("bash src/test/resources/timeoutscript").setMaxWaitMs(100000).execute();
            fail("Execution succeded. Lookout for other test failures");
        } catch (ExecutionFailedException e) {
            System.out.println(e.getMessage());
            assertEquals("hi", e.getProcessResponse().get().getContent());
        }
    }

}
