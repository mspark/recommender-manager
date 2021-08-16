package net.ssehub.recommender.agent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Process Runner provides a way to represent an commandline action in java.
 * 
 * @author marcel
 */
public class ProcessRunner {

    private Logger log = LoggerFactory.getLogger(ProcessRunner.class);
    private final String cmdAction; 
    private Optional<Integer> maxWaitMs = Optional.empty();
    private Optional<File> outputFile = Optional.empty();
    
    /**
     * Represents an action which can be manipulated an run on the host machine. 
     * 
     * @param cmdAction
     */
    public ProcessRunner(String cmdAction) {
        this.cmdAction = cmdAction;
    }
    
    /**
     * Creates a process runner with file response mode. Instead of reading the direct output from the script, 
     * the runner will wait until finish (respecting the timeout) and treats the file as output.
     * 
     * @param cmdAction
     * @param outputFilePath Absolute system path to output files. Process failes when not present after executing
     */
    public ProcessRunner(String cmdAction, String outputFilePath) {
        this.cmdAction = cmdAction;
        this.outputFile = Optional.of(new File(outputFilePath));
    }
    
    /**
     * Specify the max runtime of the process before it is killed. This is handled like the CMD returnes a failure.
     * 
     * @param ms In milliseconds and has no effect when negative or zero
     * @return current object for further management
     */
    public ProcessRunner setMaxWaitMs(int ms) {
        if (ms > 0) {
            this.maxWaitMs = Optional.of(ms);            
        }
        return this;
    }
    
    /**
     * Executes the CMD action.
     * 
     * @return Contains response information of the process
     * @throws E 
     */
    public ProcessResponse execute() throws ExecutionFailedException {
        ProcessResponse response;
        try {
            log.debug("Execute command {}", this.cmdAction);
            Process process = runCmd();
            log.debug("CMD executed."); 
            response = new ProcessResponse(readResponse(process), process.exitValue());
            process.destroy();
        } catch (IOException e) {
            log.error("CMD not finished successfull: {}", e.getMessage());
            throw new ExecutionFailedException(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error("CMD interrupted");
            throw new ExecutionFailedException(e.getMessage(), e);
        }
        return response;
    }

    /**
     * Actually runs the {@link #cmdAction} on local environment.
     * 
     * Implementation note: consider destroying the process later.
     * 
     * @return The finished (succeeded or terminated) process object 
     * @throws IOException
     * @throws InterruptedException
     */
    private Process runCmd() throws IOException, InterruptedException {
        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec(this.cmdAction);
        if (maxWaitMs.isPresent()) {
            log.debug("Wait for max {} milliseconds until finished", maxWaitMs.get());
            if (!process.waitFor(maxWaitMs.get(), TimeUnit.MILLISECONDS)) {
//                process.destroy();                    
            }
        } else {
            process.waitFor();
        }
        return process;
    }
    
    /**
     * Reads the response of the giving process regarding to the output source. 
     * 
     * @param process
     * @return Read response as string
     * @throws IOException
     */
    private String readResponse(Process process) throws ExecutionFailedException {
        String response;
        try {            
            if (outputFile.isPresent()) {
                response = readResponseFromFile(process);
            } else {
                response = readResponeDirect(process);
            }
        } catch (IOException e) {
            throw new ExecutionFailedException("Engine does not return a response - "
                    + "most likly because the execution failed", e);
        }
        return response;
    }
    
    /**
     * Reads the response of the executing process.
     * 
     * @param process
     * @return The response of the cmd
     * @throws IOException
     */
    private String readResponeDirect(Process process) throws IOException {
        var stream = new BufferedInputStream(process.getInputStream());
        int character = stream.read();
        StringBuilder builder = new StringBuilder();
        while (character != -1) {
            builder.append((char) character);
            character = stream.read();
        }
        return builder.toString();
    }

    /**
     * Reads the response from a file.
     * 
     * @param process
     * @return content of the file which was set in constructor
     * @throws IOException
     */
    private String readResponseFromFile(Process process) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.readAllLines(this.outputFile.get().toPath()).stream().forEach(builder::append);
        return builder.toString();
    }
}
