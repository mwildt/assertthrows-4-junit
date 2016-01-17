package de.flavia.function;

/**
 * Created by mwildt on 17.01.16.
 */
public interface ThrowingSupplier<U> {

    <T> T get();

    default <T> T throwingGet() {
        try{
            return this.get();
        } catch (Throwable cause){
            throw new RuntimeException(cause);
        }

    }

}


