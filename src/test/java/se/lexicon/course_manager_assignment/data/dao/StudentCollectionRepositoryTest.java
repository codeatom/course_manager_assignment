package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Student;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {StudentCollectionRepository.class})
public class StudentCollectionRepositoryTest {

    @Autowired
    private StudentDao testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertFalse(testObject == null);
    }

    //Write your tests here


    private Student student_1;
    private Student student_2;
    private Student student_3;

    @AfterEach
    void tearDown() {
        testObject.clear();
        StudentSequencer.setStudentSequencer(0);
    }

    @BeforeEach
    void setup(){
        student_1 = testObject.createStudent("Chris Developer", "cd@provider.com", "Karlskrona Sweden");
        student_2 = testObject.createStudent("Test Testing", "tt@provider.com", "Växjö Sweden");
        student_3 = testObject.createStudent("Developer Lucky", "dl@provider.com", "Västerås Sweden");
    }


    @Test
    void should_Create_And_Return_A_Student() {
        //Arrange
             //----Student construction was done in setup---//

        //Act
        Student expected = testObject.findById(student_1.getId());

        ///Assert
        assertEquals(expected, student_1);
    }

    @Test
    void should_Find_And_Return_A_Student_Using_Student_Email_As_Search_Key() {
        //Arrange

        //Act
        Student expected = testObject.findByEmailIgnoreCase(student_1.getEmail());

        //Assert
        assertEquals(expected, student_1);
    }

    @Test
    void should_Find_And_Return_A_Collection_Of_Students_Using_A_Given_Name_String_As_Search_Key() {
        //Arrange

        //Act
        Collection<Student> expected = new HashSet<>();
        expected.add(student_1);

        //Assert
        assertEquals(expected, testObject.findByNameContains(student_1.getName()));
    }

    @Test
    void should_Find_And_Return_A_Student_Using_Student_Id_As_Search_Key() {
        //Arrange

        //Act
        Student expected = testObject.findById(student_1.getId());

        //Assert
        assertEquals(expected, student_1);
    }

    @Test
    void should_Find_And_Return_All_Students_In_A_Collection() {
        //Arrange
        Collection<Student> expected = new HashSet<>();

        //Act
        expected.add(student_1);
        expected.add(student_2);
        expected.add(student_3);

        //Assert
        assertEquals(expected.size(), testObject.findAll().size());
    }

    @Test
    void should_Find_And_Remove_A_Student_From_Student_Collection_Using_Student_Id_As_Search_Key() {
        //Arrange
        Collection<Student> studentCollection = new HashSet<>();
        studentCollection.add(student_2);
        studentCollection.add(student_3);

        //Act
        boolean deleted = testObject.removeStudent(student_1);

        //Assert
        assertAll(
                () -> assertTrue(deleted),
                () -> assertEquals(studentCollection.size(), testObject.findAll().size())
        );

    }

    @Test
    void should_Empty_Student_Collection() {
        //Arrange

        //Act
        testObject.clear();
        int collectionSize = testObject.findAll().size();

        //Assert
        assertEquals(collectionSize, 0);
    }

    @Test
    void should_Find_And_Update_A_Student_Using_Student_Name_Email_And_Address_As_Search_Key() {
        //Arrange
        String name = student_1.getName();
        String address = student_1.getAddress();

        String email = student_2.getEmail();

        //Act
        Student updatedStudent = testObject.updateStudent(name, email, address);
        Student expected = testObject.findByEmailIgnoreCase(student_2.getEmail());

        //Assert
        assertAll(
                () -> assertEquals(expected.getName(), updatedStudent.getName()),
                () -> assertEquals(expected.getAddress(), updatedStudent.getAddress())
        );
    }

}
