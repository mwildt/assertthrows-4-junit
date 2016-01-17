package de.flavia.junit;

import de.flavia.functional.*;

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
        try{
            T value = supplier.get()
            org.junit.Assert.fail(message)
        } catch (Throwable error){
            Assert.assetError(error.getCause(), type, supplier);
        }
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
            trigger.trigger()
            org.junit.Assert.fail(message)
        } catch (Throwable error){
            Assert.assetError(error.getCause(), type, supplier);
        }
    }

    private static assetError(Throwable cause, Class<T> type, Consumer<T>... consumer){
        org.junit.Assert.assertTrue(message, cause.getClass().isAssignableFrom(type));
        assertConsumer(cause, consumer);
    }

    private static <T> void assertConsumer(T cause, Consumer<T> ... consumers){
        Arrays.asList(consumers)
                .stream()
                .forEach(consumer -> consumer.accept(cause));
    }


}
