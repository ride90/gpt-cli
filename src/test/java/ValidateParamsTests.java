import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateParamsTests extends BaseTest {

    @Test
    @DisplayName("Run command with empty question.")
    void testEmptyQuestion() {
        // Empty string.
        Integer exitCode = this.commandLine.execute("");
        assertEquals(2, exitCode);
    }

    @Test
    @DisplayName("Run command with wrong temperature value.")
    void testWrongTemperature() {
        // Empty string.
        Integer exitCode = this.commandLine.execute("List all files as a table", "-t=3");
        assertEquals(2, exitCode);
    }
}
