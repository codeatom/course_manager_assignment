package se.lexicon.course_manager_assignment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTest {

    @Test
    void should_Construct_A_Student_Object() {
        //Arrange
        int id = 1;
        String name = "Chris Christopher";
        String email = "cc@provider.com";
        String address = "Karlskrona Sweden";

        //Act
        Student student = new Student(id, name, email, address);

        //Assert
        assertAll(
                () -> assertEquals(id, student.getId()),
                () -> assertEquals(name, student.getName()),
                () -> assertEquals(email, student.getEmail()),
                () -> assertEquals(address, student.getAddress())
        );
    }
}
