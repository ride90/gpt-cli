import io.ridelink.cmd.CompletionCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApiKeyNotSetTests {

    CommandLine commandLine;
    CompletionCommand commandMock;

    @BeforeEach
    void setUp() {
        this.commandMock = mock(CompletionCommand.class, CALLS_REAL_METHODS);
        // this.commandMock = new CompletionCommand();
        this.commandLine = new CommandLine(this.commandMock);
    }

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
