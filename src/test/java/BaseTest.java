import io.ridelink.cmd.CompletionCommand;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;

import static org.mockito.Mockito.*;

public class BaseTest {

    CommandLine commandLine;
    CompletionCommand commandMock;

    @BeforeEach
    void setUp() {
        this.commandMock = mock(CompletionCommand.class, CALLS_REAL_METHODS);
        this.commandLine = new CommandLine(this.commandMock);
        // Ensure OPENAI_API_KEY env var is set to a fake value.
        when(this.commandMock.getEnv("OPENAI_API_KEY"))
                .thenReturn("lorem-token-ipsulum");
    }
}
