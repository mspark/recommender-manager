package net.ssehub.recommender.agent;

import java.util.Optional;

/**
 * Presentation of a failed execution on runtime.
 * 
 * @author marcel
 */
public class ExecutionFailedException extends RuntimeException {
    
    /**
     * Generated.
     */
    private static final long serialVersionUID = -2853725144417455946L;

    private ProcessResponse response;

    /**
     * Default super constructor. 
     * 
     * @param message
     * @param cause
     */
    public ExecutionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * When available, the response from the failed execution. Can contain parts of the response from the engine or 
     * more information about the error. 
     * 
     * @return process response of the failed execution
     */
    public Optional<ProcessResponse> getProcessResponse() {
        return Optional.ofNullable(response);
    }
}
