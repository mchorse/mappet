package mchorse.mappet.api.scripts;

import com.caoccao.javet.interop.V8Runtime;

import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.caoccao.javet.utils.JavetDateTimeUtils.getUTCNow;

/**
 * Limit execution time for V8Runtime
 * <pre>{@code
 *     ScriptGuard guard = new ScriptGuard(engine, 10000);
 *     engine.getExecutor("while (true) {};").executeVoid();
 *     guard.close();
 * }</pre>
 */
public class ScriptGuard implements Runnable, AutoCloseable {
    protected V8Runtime engine;
    protected Future<?> future;
    protected long timeoutMillis;
    protected volatile boolean quiting;
    protected final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ScriptGuard(V8Runtime engine, long timeoutMillis) {
        this.engine = engine;
        this.timeoutMillis = timeoutMillis;
        this.quiting = false;
        future = executorService.submit(this);
    }


    @Override
    public void run() {
        ZonedDateTime startZonedDateTime = getUTCNow();

        while(!this.quiting) {
            ZonedDateTime currentZonedDateTime = getUTCNow();
            if (startZonedDateTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(this.timeoutMillis))
                    .isBefore(currentZonedDateTime)) {
                if (engine.isInUse()) {
                    engine.terminateExecution();
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Throwable t) {
                    // It's closed.
                }
            }
        }
        quiting = true;
    }

    public void close() {
        this.quiting = true;
        if (!future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
    }

    public void setTimeoutMillis(long timeoutMillis) { this.timeoutMillis = timeoutMillis; }
}
