package framework;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

public class JSONSchemaValidation {
    public static JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(
                    ValidationConfiguration.newBuilder()
                            .setDefaultVersion(SchemaVersion.DRAFTV4)
                            .freeze()).freeze();

    static {
        JsonSchemaValidator.settings = settings().with().jsonSchemaFactory(factory)
                .and().with().checkedValidation(false);
    }

}

