package de.flavia.junit;

import de.flavia.function.ThrowingSupplier;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mwildt on 17.01.16.
 *
 *
 */
public class AssertTest {

    static class MyException extends Exception {

        public MyException(String message){
            super(message);
        }

    }

    static class MyOtherException extends Exception {

        public MyOtherException(String message){
            super(message);
        }

    }

    @Test
    public void testAssertErrorFails(){

        try{
            Assert.assetError("MESSAGE", new MyOtherException(""), MyException.class);
            fail("Unexpected");
        } catch (Throwable cause){
            assertEquals("MESSAGE", cause.getMessage());
        }

    }

    @Test
    public void testAssertErrorOK(){
        try{
            Assert.assetError("MESSAGE", new MyException(""), MyException.class);
        } catch (Throwable cause){
            fail("Unexpected");
        }
    }

    @Test
    public void testAssertConsumerOK(){
        Assert.assetError("MESSAGE",
                new MyException("the exceptionMessage"),
                MyException.class, (e) -> assertEquals("the exceptionMessage", e.getMessage())
        );
    }

    @Test
    public void testAssertConsumerFails(){
        try{
            Assert.assetError("MESSAGE",
                    new MyException("the exceptionMessage"),
                    MyException.class, (e) -> assertTrue("error message", "not the exceptionMessage".equals(e.getMessage()))
            );
            fail("Unexpected");
        } catch (Throwable cause){
            assertEquals("error message", cause.getMessage());
        }
    }

    @Test
    public void testAssertConsumerFailsOnSecond(){
        try{
            Assert.assetError("MESSAGE",
                    new MyException("the exceptionMessage"),
                    MyException.class,
                    (e) -> assertTrue("error message 1", "the exceptionMessage".equals(e.getMessage())),
                    (e) -> assertTrue("error message 2", "not the exceptionMessage".equals(e.getMessage()))
            );
            fail("Unexpected");
        } catch (Throwable cause){
            assertEquals("error message 2", cause.getMessage());
        }
    }

    @Test
    public void assertThrowsTriggerOK(){
        Assert.assertThrows(MyException.class, () -> {throw new MyException("M");} );
    }

    @Test
    public void assertThrowsTriggerFailsOnWrongException(){
        try {
            Assert.assertThrows("WRONG EXCEPTION", MyOtherException.class, () -> {
                throw new MyException("M");
            });
        }catch(Throwable cause){
            assertEquals(cause.getMessage(), "WRONG EXCEPTION");
        }
    }

    @Test
    public void assertThrowsTriggerFailsOnNoException(){
        try {
            Assert.assertThrows("NO EXCEPTION", MyOtherException.class, () -> {});
        }catch(Throwable cause){
            assertEquals("NO EXCEPTION", cause.getMessage());
        }
    }



    class ThrowingSupplierDummi implements ThrowingSupplier<String> {

        Exception e;

        public ThrowingSupplierDummi(Exception e) {
            this.e = e;
        }

        @Override
        public String get() throws Exception {
            throw e;
        }

    };

    @Test
    public void assertThrowsSupplierOK(){
        Assert.assertThrows(MyException.class, new ThrowingSupplierDummi( new MyException("M")));
    }


    @Test
    public void assertThrowsSupplierFailsOnNoException(){
        try {
            Assert.assertThrows("NO EXCEPTION", MyException.class, new ThrowingSupplier<String>() {

                @Override
                public String get() throws Exception {
                    return "HALLO";
                }

            });
        } catch(Throwable cause){
            assertEquals("NO EXCEPTION", cause.getMessage());
        }
    }

    @Test
    public void assertThrowsSupplierFailsOnWrongException(){
        try {
            Assert.assertThrows("WRONG EXCEPTION", MyException.class, new ThrowingSupplierDummi( new MyOtherException("M")));
        }catch(Throwable cause){
            assertEquals(cause.getMessage(), "WRONG EXCEPTION");
        }
    }




}
