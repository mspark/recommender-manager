package net.ssehub.recommender.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Provides the unified recommender api for recommender agents for communication
 * between recommender engines and sparkyservice-api .
 * 
 * @author marcel
 */
@SpringBootApplication
public class RecommenderAgentApplication {

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RecommenderAgentApplication.class, args);
    }

}
