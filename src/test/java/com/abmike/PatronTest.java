package com.abmike;

import com.abmike.model.Patron;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatronTest {


    @Test
    void testPatronConstructorAndGetters() {
        Patron patron=new Patron(1,"Michael","mike@gmail.com", "0552134586");
        assertEquals(1,patron.getId());
        assertEquals("Michael",patron.getName());
        assertEquals("mike@gmail.com",patron.getEmail());
        assertEquals("0552134586",patron.getPhone());
    }

    @Test
    void testPatronSetters() {
        Patron patron=new Patron(1,"Michael","mike@gmail.com", "0552134586");
        patron.setId(2);
        patron.setName("June");
        patron.setEmail("june@gmail.com");
        patron.setPhone("0552134586");

        assertEquals(2,patron.getId());
        assertEquals("June",patron.getName());
        assertEquals("june@gmail.com",patron.getEmail());
        assertEquals("0552134586",patron.getPhone());


    }
}
