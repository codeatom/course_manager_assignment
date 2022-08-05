package se.lexicon.course_manager_assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.lexicon.course_manager_assignment.data.service.course.CourseService;
import se.lexicon.course_manager_assignment.data.service.student.StudentService;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;

import java.util.Scanner;


@SpringBootApplication
public class CourseManagerAssignmentApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagerAssignmentApplication.class, args);
    }

    @Autowired
	private StudentService studentManager;
    @Autowired
    private CourseService courseManager;


	@Override
	public void run(String... args) throws Exception {
        CreateStudentForm createStudentForm = new CreateStudentForm(0, "Christopher", "chris@lexicon.com", "lexico växjö");
		studentManager.create(createStudentForm);

        CreateCourseForm createCourseForm = new CreateCourseForm(0, "Java development", null, 24);
        courseManager.create(createCourseForm);

        System.out.println("The added student name is: '" + studentManager.findById(1).getName() + "'");
        System.out.println("The added course name is: '" + courseManager.findById(1).getCourseName() + "'");

        UpdateStudentForm updateStudentForm = new UpdateStudentForm(0, "Lucky", "chris@lexicon.com", "Karlskrona");
        studentManager.update(updateStudentForm);

        System.out.println("Student name has been updated to: '" + studentManager.findById(1).getName() + "'");

	}

}
