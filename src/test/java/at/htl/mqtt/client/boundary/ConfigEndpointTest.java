package at.htl.mqtt.client.boundary;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class ConfigEndpointTest {

    @Karate.Test
    Karate testRoom() {
        return Karate.run("config-resource.feature").relativeTo(getClass()).systemProperty("engine.WarnInterpreterOnly", "true");
        //Results results = Runner.path("config-resource.feature").relativeTo(getClass()).parallel(5);
        //assertEquals(0,results.getFailCount(), results.getErrorMessages());
    }
}
