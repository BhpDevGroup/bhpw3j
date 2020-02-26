package io.bhpw3j.protocol.rx;

import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.core.BlockParameter;
import io.bhpw3j.protocol.core.BlockParameterIndex;
import io.bhpw3j.protocol.core.BlockParameterName;
import io.bhpw3j.protocol.core.methods.response.BhpBlockCount;
import io.bhpw3j.protocol.core.methods.response.BhpGetBlock;
import io.bhpw3j.protocol.core.methods.response.Transaction;
import io.bhpw3j.protocol.core.polling.BlockPolling;
import io.bhpw3j.utils.Observables;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * bhpw3j reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Bhpw3j bhpw3J;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Bhpw3j bhpw3J, ScheduledExecutorService scheduledExecutorService) {
        this.bhpw3J = bhpw3J;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<BigInteger> bhpBlockObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockPolling blockPolling = new BlockPolling(bhpw3J, subscriber::onNext);
            blockPolling.run(scheduledExecutorService, pollingInterval);
            subscriber.add(Subscriptions.create(blockPolling::cancel));
        });
    }

    public Observable<BhpGetBlock> replayBlocksObservable(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<BhpGetBlock> replayBlocksObservable(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<BhpGetBlock> replayBlocksObservableSync(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<BhpGetBlock> replayBlocksObservableSync(
            BlockParameter startBlock, BlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> bhpw3J.getBlock(new BlockParameterIndex(i), fullTransactionObjects).observable());
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> bhpw3J.getBlock(new BlockParameterIndex(i), fullTransactionObjects).observable());
        }
    }

    public Observable<BhpGetBlock> catchUpToLatestBlockObservable(
            BlockParameter startBlock, boolean fullTransactionObjects,
            Observable<BhpGetBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<BhpGetBlock> catchUpToLatestBlockObservable(
            BlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.empty());
    }

    private Observable<BhpGetBlock> catchUpToLatestBlockObservableSync(
            BlockParameter startBlock, boolean fullTransactionObjects,
            Observable<BhpGetBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservableSync(
                            new BlockParameterIndex(startBlockNumber),
                            new BlockParameterIndex(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(() -> catchUpToLatestBlockObservableSync(
                            new BlockParameterIndex(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteObservable)));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            BlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<BhpGetBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            BlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<BhpGetBlock> blockObservable(boolean fullTransactionObjects, long pollingInterval) {
        return bhpBlockObservable(pollingInterval)
                .flatMap(blockIndex ->
                        bhpw3J.getBlock(new BlockParameterIndex(blockIndex), fullTransactionObjects).observable());
    }

    private static List<Transaction> toTransactions(BhpGetBlock bhpGetBlock) {
        return bhpGetBlock.getBlock().getTransactions().stream().collect(Collectors.toList());
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(BlockParameterName.LATEST).subtract(BigInteger.ONE);
    }

    private BigInteger getBlockNumber(
            BlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof BlockParameterIndex) {
            return ((BlockParameterIndex) defaultBlockParameter).getBlockIndex();
        } else {
            if (defaultBlockParameter instanceof BlockParameterName) {
                if (defaultBlockParameter.getValue() == BlockParameterName.EARLIEST.getValue()) {
                    return BigInteger.ZERO;
                }
            }
            BhpBlockCount latestBhpBlock = bhpw3J.getBlockCount().send();
            return latestBhpBlock.getBlockIndex();
        }
    }

}
