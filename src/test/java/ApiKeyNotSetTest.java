import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ApiKeyNotSetTest extends BaseTest {

    @Test
    @DisplayName("Run command when API key is not set.")
    void testApiKeyNotSet() {
        // Ensure OPENAI_API_KEY env var is not set.
        when(this.commandMock.getEnv("OPENAI_API_KEY")).thenReturn(null);
        // Run cmd & get exit code.
        Integer exitCode = this.commandLine.execute("List all files as a table");
        assertEquals(2, exitCode);
    }
}
