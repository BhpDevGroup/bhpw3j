package io.bhpw3j.protocol;

import io.bhpw3j.protocol.core.JsonRpc2_0Bhpw3J;
import io.bhpw3j.protocol.core.Bhp;
import io.bhpw3j.protocol.rx.Bhpw3jRx;

import java.util.concurrent.ScheduledExecutorService;

/**
 * JSON-RPC Request object building factory.
 */
public interface Bhpw3j extends Bhp, Bhpw3jRx {

    /**
     * Construct a new Bhpw3j instance.
     *
     * @param bhpw3JService bhpw3j service instance - i.e. HTTP or IPC
     * @return new Bhpw3j instance
     */
    static Bhpw3j build(Bhpw3jService bhpw3JService) {
        return new JsonRpc2_0Bhpw3J(bhpw3JService);
    }

    /**
     * Construct a new Bhpw3j instance.
     *
     * @param bhpw3jService            bhpw3j service instance - i.e. HTTP or IPC
     * @param pollingInterval          polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new Bhpw3j instance
     */
    static Bhpw3j build(
            Bhpw3jService bhpw3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Bhpw3J(bhpw3jService, pollingInterval, scheduledExecutorService);
    }

    /**
     * Shutdowns a Bhpw3j instance and closes opened resources.
     */
    void shutdown();
}
