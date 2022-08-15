package se.lexicon.course_manager_assignment.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseTest {

    @Test
    void should_Construct_A_Course_Object() {
        //Arrange
        int id = 1;
        String courseName = "Chris Christopher";
        LocalDate startDate = LocalDate.parse("2022-08-30");
        int weekDuration = 10;

        //Act
        Course course = new Course(id, courseName, startDate, weekDuration);

        //Assert
        assertAll(
                () -> assertEquals(id, course.getId()),
                () -> assertEquals(courseName, course.getCourseName()),
                () -> assertEquals(startDate, course.getStartDate()),
                () -> assertEquals(weekDuration, course.getWeekDuration())
        );
    }
}
