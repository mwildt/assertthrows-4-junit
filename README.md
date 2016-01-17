# Java 8 exception Assertion Utils for JUnit

## Requirements:
* Java 8
* Junit 4

## usage

~~~
import static de.flavia.junit.Assert.*

public class MyTest {

    public static class MyException extends Exception {

        public int code;

        public Exception(String msg, int code){
            super(msg);
            this.code = code;
        }
    }

    void triggerException(){
        throw new MyException("MyException message", 8);
    }

    @Test
    public void simpleUsage(){
        assertThrows(
            MyException.class,
            () -> triggerException(), // trigger the Method under Test
        );
    }

    @Test
    public void advancedUsage(){
        assertThrows(
            "Optional Error Message",
            MyException.class,
            () -> triggerException(), // trigger the Method under Test
            (e) -> assertEquals(e.getMessage(), "MyException message"), // do some custom assertions on the thrown excption
            (e) -> assertTrue(e.code() % 3 == 2) // do some custom assertions on the thrown excption
        );
    }
}
~~~
