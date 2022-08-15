package se.lexicon.course_manager_assignment.data.service.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Student;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseManager.class, CourseCollectionRepository.class, ModelToDto.class, StudentCollectionRepository.class})
public class CourseManagerTest {

    @Autowired
    private CourseService testObject;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private StudentDao studentDao;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
        assertNotNull(courseDao);
        assertNotNull(studentDao);
    }

    //Write your tests here


    private CourseView courseView_1;
    private CourseView courseView_2;

    @AfterEach
    void tearDown() {
        courseDao.clear();
        studentDao.clear();
        CourseSequencer.setCourseSequencer(0);
        StudentSequencer.setStudentSequencer(0);
    }

    @BeforeEach
    void setup(){
        CreateCourseForm form_1 = new CreateCourseForm(0, "Java", LocalDate.parse("2022-08-30"), 12);
        CreateCourseForm form_2 = new CreateCourseForm(0, "C sharp", LocalDate.parse("2022-08-30"), 10);

        courseView_1 = testObject.create(form_1);
        courseView_2 = testObject.create(form_2);
    }

    @Test
    void should_Create_And_Return_A_Course_In_Form_Of_CourseView() {
        //Arrange
        int id = courseView_1.getId();
        String courseName = courseView_1.getCourseName();
        LocalDate startDate = courseView_1.getStartDate();
        int weekDuration = courseView_1.getWeekDuration();
        List<StudentView> students = courseView_1.getStudents();

        //Act
        CourseView expected = new CourseView(id, courseName, startDate, weekDuration, students);

        //Assert
        assertEquals(expected, courseView_1);

    }

    @Test
    void should_Find_And_Return_A_List_Of_Courses_In_Form_Of_CourseView_List_Using_Course_Name_As_Search_Key() {
        //Arrange

        //Act
        List<CourseView> expected = new ArrayList<>();
        expected.add(courseView_1);

        //Assert
        assertEquals(expected, testObject.searchByCourseName(courseView_1.getCourseName()));
    }

    @Test
    void should_Find_And_Return_A_List_Of_Courses_In_Form_Of_CourseView_List_Using_A_Given_Date_As_Search_Key() {
        //Arrange

        //Act
        List<CourseView> expected = new ArrayList<>();

        //Assert
        assertEquals(expected, testObject.searchByDateBefore(LocalDate.parse("2022-08-10")));
    }

    @Test
    void should_Find_And_Return_A_List_Of_Courses_In_Form_Of_CourseView_List_Using_A_Given_Past_Date_As_Search_Key() {
        //Arrange

        //Act
        List<CourseView> expected = new ArrayList<>();
        expected.add(courseView_1);
        expected.add(courseView_2);


        //Assert
        assertEquals(expected.size(), testObject.searchByDateAfter(LocalDate.parse("2022-08-10")).size());
    }

    @Test
    void should_Add_A_Student_To_A_Course() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");

        //Act
        testObject.addStudentToCourse(courseView_1.getId(), student.getId()); //Adding just 1 student to course

        int numberOfStudentsAdded = 1;

        //Assert
        assertEquals(testObject.findById(courseView_1.getId()).getStudents().size(), numberOfStudentsAdded);
    }

    @Test
    void should_Remove_A_Student_From_A_Course() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");

        //Act
        testObject.addStudentToCourse(courseView_1.getId(), student.getId()); //Adding just 1 student to course
        int numberOfStudentsAdded = testObject.findById(courseView_1.getId()).getStudents().size();

        testObject.removeStudentFromCourse(courseView_1.getId(), student.getId()); //Removing just 1 student to course
        int numberOfStudentsRemoved = 1;

        int s = testObject.findById(courseView_1.getId()).getStudents().size();

        //Assert
        assertEquals(testObject.findById(courseView_1.getId()).getStudents().size(), (numberOfStudentsAdded - numberOfStudentsRemoved));
    }

    @Test
    void should_Find_And_Return_A_Course_In_Form_Of_CourseView_Using_Course_Id_As_Search_Key() {
        //Arrange

        //Act
        CourseView expected = testObject.findById(courseView_1.getId());

        //Assert
        assertEquals(expected, courseView_1);
    }

    @Test
    void should_Find_And_Return_All_Courses_In_Form_Of_CourseView_List() {
        //Arrange

        //Act
        List<CourseView> expected = new ArrayList<>();
        expected.add(courseView_1);
        expected.add(courseView_2);

        //Assert
        assertEquals(expected.size(), testObject.findAll().size());
    }

    @Test
    void should_Find_And_Return_A_List_Of_Courses_In_Form_Of_CourseView_List_Using_A_Given_Student_Id_As_Search_Key() {
        //Arrange
        Student student = studentDao.createStudent("Chris Test", "co@provider.com", "Karlskrona Sweden");
        List<CourseView> coursesTakenByStudent = new ArrayList<>();

        testObject.addStudentToCourse(courseView_1.getId(), student.getId());
        testObject.addStudentToCourse(courseView_2.getId(), student.getId());

        coursesTakenByStudent.add(courseView_1);
        coursesTakenByStudent.add(courseView_2);

        //Act
        List<CourseView> courseViewList = testObject.findByStudentId(student.getId());

        //Assert
        assertEquals(courseViewList.size(), coursesTakenByStudent.size());
    }

    @Test
    void should_Find_And_Update_A_Course_Using_Course_Id_As_Search_Key() {
        //Arrange
        int id = courseView_1.getId();
        String name = courseView_1.getCourseName();
        LocalDate startDate = courseView_1.getStartDate();
        int weekDuration = courseView_2.getWeekDuration();

        //Act
        UpdateCourseForm updateCourseForm = new UpdateCourseForm(id, name, startDate, weekDuration);

        CourseView updatedCourseView = testObject.update(updateCourseForm);
        CourseView expected = testObject.findById(courseView_1.getId());

        //Assert
        assertEquals(expected.getWeekDuration(), updateCourseForm.getWeekDuration());
    }

    @Test
    void should_Find_And_Remove_A_Course_From_Course_List_Using_Course_Id_As_Search_Key() {
        //Arrange

        //Act
        List<CourseView> courseViewList = new ArrayList<>();
        courseViewList.add(courseView_1);

        boolean deleted = testObject.deleteCourse(courseView_2.getId());

        //Assert
        assertAll(
                () -> assertTrue(deleted),
                () -> assertEquals(courseViewList, testObject.findAll())
        );

    }

}
