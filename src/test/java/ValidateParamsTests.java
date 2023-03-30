import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateParamsTests extends BaseTest {

    @Test
    @DisplayName("Run command with empty question.")
    void testEmptyQuestion() {
        Integer exitCode = this.commandLine.execute("");
        assertEquals(2, exitCode);
    }

    @Test
    @DisplayName("Run command with wrong temperature value.")
    void testWrongTemperature() {
        Integer exitCode = this.commandLine.execute("List all files as a table", "-t=3");
        assertEquals(2, exitCode);
    }

    @Test
    @DisplayName("Run command with wrong temperature value.")
    void testWrongModel() {
        Integer exitCode = this.commandLine.execute("List all files as a table", "-m=mmmm");
        assertEquals(2, exitCode);
    }
}
