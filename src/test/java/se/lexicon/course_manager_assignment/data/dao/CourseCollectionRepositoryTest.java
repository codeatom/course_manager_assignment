package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {CourseCollectionRepository.class, StudentCollectionRepository.class})
public class CourseCollectionRepositoryTest {

    @Autowired
    private CourseDao testObject;

    @Autowired
    private StudentDao studentDao;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertFalse(testObject == null);
        assertFalse(studentDao == null);
    }

    //Write your tests here


    private Course course_1;
    private Course course_2;

    @AfterEach
    void tearDown() {
        testObject.clear();
        CourseSequencer.setCourseSequencer(0);
    }

    @BeforeEach
    void setup(){
        course_1 = testObject.createCourse("Java", LocalDate.parse("2022-08-30"), 12);
        course_2 = testObject.createCourse("C sharp", LocalDate.parse("2022-08-30"), 10);
    }


    @Test
    void should_Create_And_Return_A_Course() {
        //Arrange
        //----Course construction was done in setup---//

        //Act
        Course expected = testObject.findById(course_1.getId());

        ///Assert
        assertEquals(expected, course_1);
    }

    @Test
    void should_Find_And_Return_A_Course_Using_Course_Id_As_Search_Key() {
        //Arrange

        //Act
        Course expected = testObject.findById(course_1.getId());

        //Assert
        assertEquals(expected, course_1);
    }

    @Test
    void should_Find_And_Return_A_Collection_Of_Courses_Using_A_Given_Name_String_As_Search_Key() {
        //Arrange

        //Act
        Collection<Course> expected = new HashSet<>();
        expected.add(course_1);

        //Assert
        assertEquals(expected, testObject.findByNameContains(course_1.getCourseName()));
    }

    @Test
    void should_Find_And_Return_A_Collection_Of_Courses_Using_A_Given_Date_As_Search_Key() {
        //Arrange

        //Act
        Collection<Course> expected = new HashSet<>();

        //Assert
        assertEquals(expected, testObject.findByDateBefore(LocalDate.parse("2022-08-10")));
    }

    @Test
    void should_Find_And_Return_A_Collection_Of_Courses_Using_A_Given_Past_Date_As_Search_Key() {
        //Arrange

        //Act
        Collection<Course> expected = new HashSet<>();
        expected.add(course_1);
        expected.add(course_2);


        //Assert
        assertEquals(expected.size(), testObject.findByDateAfter(LocalDate.parse("2022-08-10")).size());
    }

    @Test
    void should_Find_And_Return_All_Courses() {
        //Arrange

        //Act
        Collection<Course> expected = new HashSet<>();
        expected.add(course_1);
        expected.add(course_2);

        //Assert
        assertEquals(expected.size(), testObject.findAll().size());
    }

    @Test
    void should_Find_And_Return_A_Collection_Of_Courses_Using_A_Given_Student_Id_As_Search_Key() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");
        Collection<Course> coursesTakenByStudent = new ArrayList<>();

        testObject.addStudentToCourse(course_1.getId(), student);
        testObject.addStudentToCourse(course_2.getId(), student);

        coursesTakenByStudent.add(course_1);
        coursesTakenByStudent.add(course_2);

        //Act
        Collection<Course> courseCollection = testObject.findByStudentId(student.getId());

        //Assert
        assertEquals(courseCollection.size(), coursesTakenByStudent.size());
    }

    @Test
    void should_Find_And_Remove_A_Course_From_Course_Collection() {
        //Arrange

        //Act
        Collection<Course> courseCollection = new HashSet<>();
        courseCollection.add(course_1);

        boolean deleted = testObject.removeCourse(course_2);

        //Assert
        assertAll(
                () -> assertTrue(deleted),
                () -> assertEquals(courseCollection, testObject.findAll())
        );

    }

    @Test
    void should_Empty_Course_Collection() {
        //Arrange

        //Act
        testObject.clear();
        int collectionSize = testObject.findAll().size();

        //Assert
        assertEquals(collectionSize, 0);
    }

    @Test
    void should_Find_And_Update_A_Course_Using_Course_Id_As_Search_Key() {
        //Arrange
        int id = course_1.getId();
        String name = course_1.getCourseName();
        LocalDate startDate = course_1.getStartDate();

        int weekDuration = course_2.getWeekDuration();

        //Act
        Course updatedCourse = testObject.updateCourse(id, name, startDate, weekDuration);
        Course expected = testObject.findById(course_1.getId());

        //Assert
        assertEquals(expected.getWeekDuration(), updatedCourse.getWeekDuration());
    }

    @Test
    void should_Add_A_Student_To_A_Course() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");

        //Act
        testObject.addStudentToCourse(course_1.getId(), student); //Adding just 1 student to course

        int numberOfStudentsAdded = 1;

        //Assert
        assertEquals(testObject.findById(course_1.getId()).getStudents().size(), numberOfStudentsAdded);
    }

    @Test
    void should_Remove_A_Student_From_A_Course() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");

        //Act
        testObject.addStudentToCourse(course_1.getId(), student); //Adding just 1 student to course
        int numberOfStudentsAdded = testObject.findById(course_1.getId()).getStudents().size();

        testObject.removeStudentFromCourse(course_1.getId(), student); //Removing just 1 student to course
        int numberOfStudentsRemoved = 1;

        //Assert
        assertEquals(testObject.findById(course_1.getId()).getStudents().size(), (numberOfStudentsAdded - numberOfStudentsRemoved));
    }


}
