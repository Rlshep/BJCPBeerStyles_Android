package io.github.rlshep.bjcp2015beerstyles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockitoTest  {

    @Test
    public void test1()  {
        //  create mock
        MyClass test = mock(MyClass.class);

        // define return value for method getUniqueId()
        when(test.getUniqueId()).thenReturn(43);

        // use mock in test....
        assertEquals(test.getUniqueId(), 43);
    }

    private class MyClass {
        private int uniqueId = 0;

        public int getUniqueId() {
            return uniqueId;
        }
    }
}