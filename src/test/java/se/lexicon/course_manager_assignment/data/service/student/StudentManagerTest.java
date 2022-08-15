package se.lexicon.course_manager_assignment.data.service.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {StudentManager.class, CourseCollectionRepository.class, StudentCollectionRepository.class, ModelToDto.class})
public class StudentManagerTest {

    @Autowired
    private StudentService testObject;
    @Autowired
    private StudentDao studentDao;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
        assertNotNull(studentDao);
    }

    //Write your tests here


    private StudentView studentView_1;
    private StudentView studentView_2;

    @AfterEach
    void tearDown() {
        StudentSequencer.setStudentSequencer(0);
        studentDao.clear();
    }

    @BeforeEach
    void setup(){
        CreateStudentForm form_1 = new CreateStudentForm(0, "Chris Test", "co@provider.com", "Karlskrona Sweden");
        CreateStudentForm form_2 = new CreateStudentForm(0, "Test Testing", "tt@provider.com", "Växjö Sweden");

        studentView_1 = testObject.create(form_1);
        studentView_2 = testObject.create(form_2);
    }

    @Test
    void should_Create_And_Return_A_Student_In_Form_Of_StudentView() {
        //Arrange
        int id = studentView_1.getId();
        String name = studentView_1.getName();
        String email = studentView_1.getEmail();
        String address = studentView_1.getAddress();

        //Act
        StudentView expected = new StudentView(id, name, email, address);

        ///Assert
        assertEquals(expected, studentView_1);
    }

    @Test
    void should_Find_And_Return_A_Student_In_Form_Of_StudentView_Using_Student_Id_As_Search_Key() {
        //Arrange

        //Act
        StudentView expected = testObject.findById(studentView_1.getId());

        //Assert
        assertEquals(expected, studentView_1);
    }

    @Test
    void should_Find_And_Return_A_Student_In_Form_Of_StudentView_Using_Student_Email_As_Search_Key() {
        //Arrange

        //Act
        StudentView expected = testObject.searchByEmail(studentView_1.getEmail());

        //Assert
        assertEquals(expected, studentView_1);
    }

    @Test
    void should_Find_And_Return_A_List_Of_Students_In_Form_Of_StudentView_List_Using_A_Given_Name_String_As_Search_Key() {
        //Arrange

        //Act
        List<StudentView> expected = new ArrayList<>();
        expected.add(studentView_1);

        //Assert
        assertEquals(expected, testObject.searchByName(studentView_1.getName()));
    }

    @Test
    void should_Find_And_Return_All_Students_In_Form_Of_StudentView_List() {
        //Arrange

        //Act
        List<StudentView> expected = new ArrayList<>();
        expected.add(studentView_1);
        expected.add(studentView_2);

        //Assert
        assertEquals(expected.size(), testObject.findAll().size());
    }

    @Test
    void should_Find_And_Update_A_Student_Using_Student_Email_As_Search_Key() {
        //Arrange
        int id = studentView_1.getId();
        String name = studentView_1.getName();
        String address = studentView_1.getAddress();

        String email = studentView_2.getEmail();

        //Act
        UpdateStudentForm updateStudentForm = new UpdateStudentForm(id, name, email, address);

        StudentView updatedStudentView = testObject.update(updateStudentForm);
        StudentView expected = testObject.searchByEmail(studentView_2.getEmail());

        //Assert
        assertAll(
                () -> assertEquals(expected.getName(), updatedStudentView.getName()),
                () -> assertEquals(expected.getAddress(), updatedStudentView.getAddress())
        );
    }

    @Test
    void should_Find_And_Remove_A_Student_From_Student_List_Using_Student_Id_As_Search_Key() {
        //Arrange

        //Act
        List<StudentView> studentViewList = new ArrayList<>();
        studentViewList.add(studentView_2);

        boolean deleted = testObject.deleteStudent(studentView_1.getId());

        //Assert
        assertAll(
                () -> assertTrue(deleted),
                () -> assertEquals(studentViewList, testObject.findAll())
        );

    }

}
