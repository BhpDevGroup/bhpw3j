package io.bhpw3j.protocol.core;

import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.Bhpw3jService;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0Bhpw3JTest {

    private ScheduledExecutorService scheduledExecutorService
            = mock(ScheduledExecutorService.class);
    private Bhpw3jService service = mock(Bhpw3jService.class);

    private Bhpw3j bhpw3J = Bhpw3j.build(service, 10, scheduledExecutorService);

    @Test
    public void testStopExecutorOnShutdown() throws Exception {
        bhpw3J.shutdown();

        verify(scheduledExecutorService).shutdown();
        verify(service).close();
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsRuntimeExceptionIfFailedToCloseService() throws Exception {
        doThrow(new IOException("Failed to close"))
                .when(service).close();

        bhpw3J.shutdown();
    }
}