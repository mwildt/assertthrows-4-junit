package de.flavia.junit;

import de.flavia.function.ThrowingSupplier;
import de.flavia.function.ThrowingTrigger;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by mwildt on 17.01.16.
 */
public class Assert<T extends Throwable> {

    private T cause;

    private Assert(T cause){
        this.cause = cause;
    }

    public Assert<T> thenAssertException(Consumer<T> consumer){
        consumer.accept(cause);
        return this;
    }

    public T getCause(){
        return this.cause;
    }

    /*
     * static assertion section
     */

    public static <T extends Throwable, U> Assert<T> assertThrows(Class<T> type, ThrowingSupplier<U> supplier, Consumer<T> ... consumer){
        return Assert.assertThrows (
            String.format("The expected exception of typ %s was not thrown", type.getCanonicalName()),
            type,
            supplier,
            consumer
        );
    }

    public static <T extends Throwable, U> Assert<T> assertThrows(String message, Class<T> type, ThrowingSupplier<U> supplier, Consumer<T>... consumer){
        try{
            supplier.throwingGet();
        } catch (Throwable error){
            Assert.assetError(message, error.getCause(), type, consumer);
            // assertions succeeds if Exception was caught and tested with no AssertionError
            return new Assert<T>((T) error.getCause());
        }
        /*
         * assertion fails if no exception is caught
         * fail() is not used here cause its not recognized to end the execution
         */
        throw new AssertionError(message);
    }

    public static <T extends Throwable> Assert<T> assertThrows(Class<T> type, ThrowingTrigger trigger, Consumer<T>... consumer){
        return Assert.assertThrows(
            String.format("The expected exception of typ %s was not thrown", type.getCanonicalName()),
            type,
            trigger,
            consumer
        );
    }

    public static <T extends Throwable> Assert<T> assertThrows(String message, Class<T> type, ThrowingTrigger trigger, Consumer<T>... consumer){
        try{
            trigger.throwingTrigger();
        } catch (Throwable error){
            Assert.assetError(message, error.getCause(), type, consumer);
            // assertions succeeds if Exception was caught and tested with no AssertionError
            return new Assert<T>((T) error.getCause());
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
