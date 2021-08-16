package net.ssehub.recommender.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Test controller.
 * @author marcel
 */
@RestController
@Tag(name = "basic-controller", description = "Test Controller")
public class BasicController {
    
    @Autowired
    private RecommenderEngine engine;

    /**
     * Test methods.
     * @return asd
     */
    @Operation(summary = "Adds a new local user", security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping(value = "/hearbeat")
    @ResponseStatus(HttpStatus.CREATED)
    public String test() {
        engine.getOutputFile()
            .map(file -> new ProcessRunner(engine.getCmd(), file))
            .orElse(new ProcessRunner(engine.getCmd()))
            .setMaxWaitMs(0)
            .execute();
        return "hallo";
    }
}
