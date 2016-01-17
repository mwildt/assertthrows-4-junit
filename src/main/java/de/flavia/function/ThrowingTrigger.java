package de.flavia.function;

/**
 * Created by mwildt on 17.01.16.
 */
@FunctionalInterface
public interface ThrowingTrigger {

    void trigger() throws Exception;

    default void throwingTrigger() {
        try{
            this.trigger();
        } catch (Throwable cause){
            throw new RuntimeException(cause);
        }

    }

}
