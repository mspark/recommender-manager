package net.ssehub.recommender.agent;

/**
 * Record. 
 * Represents the response 
 * 
 * @author marcel
 */
public class ProcessResponse {
    private final String resultContent;
    private final int statusCode;
    
    /**
     * Holds information about the response of a process which was executed at runtime.
     * 
     * @param result
     * @param statusCode
     */
    public ProcessResponse(String result, int statusCode) {
        this.resultContent = result;
        this.statusCode = statusCode;
    }
    
    /**
     * The output of the process. 
     * 
     * @return Output merged to a single string, can contain line breaks
     */
    public String getContent() {
        System.out.println(resultContent);
        return this.resultContent.substring(0, resultContent.length() - 1);
    }

    /**
     * Process exist code.
     * 
     * @return 0 is success
     */
    public int getExitCode() {
        return this.statusCode;
    }
}
