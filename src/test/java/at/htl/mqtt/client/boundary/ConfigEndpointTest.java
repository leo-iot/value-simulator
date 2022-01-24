package at.htl.mqtt.client.boundary;

import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ConfigEndpointTest {

    @Karate.Test
    Karate testGetRoom() {
        return Karate.run("config-resource.feature").relativeTo(getClass());
    }
}