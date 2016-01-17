package de.flavia.junit;

import de.flavia.function.ThrowingSupplier;
import de.flavia.function.ThrowingTrigger;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by mwildt on 17.01.16.
 */
public class Assert {

    public static <T extends Throwable, U> U assertThrows(Class<T> type, ThrowingSupplier<U> supplier, Consumer<T>... consumer){
        return Assert.assertThrows (
            String.format("The expected exception of typ %s was not thrown", type.getCanonicalName()),
            type,
            supplier,
            consumer
        );
    }

    public static <T extends Throwable, U> U assertThrows(String message, Class<T> type, ThrowingSupplier<U> supplier, Consumer<T>... consumer){
        U value = null;
        try{
            value = supplier.throwingGet();
        } catch (Throwable error){
            Assert.assetError(message, error.getCause(), type, consumer);
            return value; // assertions succeeds if Exception was caught and tested with no AssertionError
        }
        /*
         * assertion fails if no exception is caught
         * fail() is not used here cause its not recognized to end the execution
         */
        throw new AssertionError(message);
    }

    public static <T extends Throwable> void assertThrows(Class<T> type, ThrowingTrigger trigger, Consumer<T>... consumer){
        Assert.assertThrows(
            String.format("The expected exception of typ %s was not thrown", type.getCanonicalName()),
            type,
            trigger,
            consumer
        );
    }

    public static <T extends Throwable> void assertThrows(String message, Class<T> type, ThrowingTrigger trigger, Consumer<T>... consumer){
        try{
            trigger.throwingTrigger();
        } catch (Throwable error){
            Assert.assetError(message, error.getCause(), type, consumer);
            return; // assertions succeeds if Exception was caught and tested with no AssertionError
        }
        /*
         * assertion fails if no exception is caught
         * fail() is not used here cause its not recognized to end the execution
         */
        throw new AssertionError(message);
    }

    static <T> void assetError(String message, Throwable cause, Class<T> type, Consumer<T>... consumer){
        org.junit.Assert.assertTrue(message, cause.getClass().isAssignableFrom(type));
        assertConsumer((T) cause, consumer);
    }

    static <T> void assertConsumer(T cause, Consumer<T> ... consumers){
        Arrays.asList(consumers)
                .stream()
                .forEach(consumer -> consumer.accept(cause));
    }

}
