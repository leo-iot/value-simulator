package at.htl.mqtt.client.boundary;

import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ConfigEndpointTest {

    @Karate.Test
    Karate testGetRoom() {
        return Karate.run("config-resource.feature").relativeTo(getClass());
    }
}
