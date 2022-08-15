package se.lexicon.course_manager_assignment.data.service.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ModelToDto.class})
public class ModelToDtoTest {

    @Autowired
    private Converters testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
    }

    //write your tests here

    private Student student;
    private Course course;


    @BeforeEach
    void setup(){
        student = new Student(1, "Chris Developer", "cd@provider.com", "Karlskrona Sweden");
        course = new Course(1, "Java", LocalDate.parse("2022-08-30"), 12);
    }

    @Test
    void should_Convert_Student_To_StudentView() {
        //Arrange
            //----Student construction done in setup---//

        //Act
        StudentView expected = testObject.studentToStudentView(student);

        ///Assert
        assertAll(
                () -> assertEquals(expected.getId(), student.getId()),
                () -> assertEquals(expected.getName(), student.getName()),
                () -> assertEquals(expected.getEmail(), student.getEmail()),
                () -> assertEquals(expected.getAddress(), student.getAddress())
        );
    }

    @Test
    void should_Convert_Course_To_CourseView() {
        //Arrange
           //----Course construction done in setup---//

        //Act
        CourseView expected = testObject.courseToCourseView(course);

        ///Assert
        assertAll(
                () -> assertEquals(expected.getId(), course.getId()),
                () -> assertEquals(expected.getCourseName(), course.getCourseName()),
                () -> assertEquals(expected.getStartDate(), course.getStartDate()),
                () -> assertEquals(expected.getWeekDuration(), course.getWeekDuration())
        );
    }

    @Test
    void should_Convert_A_Collection_Student_To_A_List_Of_StudentView() {
        //Arrange
        Collection<Student> students = new HashSet<>();
        students.add(student);

        //Act
        List<StudentView> expected = testObject.studentsToStudentViews(students);

        ///Assert
        assertAll(
                () -> assertEquals(expected.get(0).getId(), student.getId()),
                () -> assertEquals(expected.get(0).getName(), student.getName()),
                () -> assertEquals(expected.get(0).getEmail(), student.getEmail()),
                () -> assertEquals(expected.get(0).getAddress(), student.getAddress())
        );
    }

    @Test
    void should_Convert_A_Collection_Course_To_A_List_Of_CourseView() {
        //Arrange
        Collection<Course> courses = new HashSet<>();
        courses.add(course);

        //Act
        List<CourseView> expected = testObject.coursesToCourseViews(courses);

        ///Assert
        assertAll(
                () -> assertEquals(expected.get(0).getId(), course.getId()),
                () -> assertEquals(expected.get(0).getCourseName(), course.getCourseName()),
                () -> assertEquals(expected.get(0).getStartDate(), course.getStartDate()),
                () -> assertEquals(expected.get(0).getWeekDuration(), course.getWeekDuration())
        );
    }

}
