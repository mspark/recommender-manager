package net.ssehub.recommender.agent;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Represent the recommender field configuration. 
 * Typically this is done in the application.yml
 * 
 * @author marcel
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "recommender-engine")
public class RecommenderEngine {
    private String cmd;
    private String outputFile;
    private String dbFilePath;
    private int maxWait;
    
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
    public void setDbFilePath(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }
    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
    public String getCmd() {
        return cmd;
    }
    public Optional<String> getOutputFile() {
        return Optional.ofNullable(outputFile);
    }
    public String getDbFilePath() {
        return dbFilePath;
    }
    public int getMaxWait() {
        return maxWait;
    }
}
