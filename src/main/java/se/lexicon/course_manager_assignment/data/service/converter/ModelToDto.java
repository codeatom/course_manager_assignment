package se.lexicon.course_manager_assignment.data.service.converter;

import org.springframework.stereotype.Component;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ModelToDto implements Converters {
    @Override
    public StudentView studentToStudentView(Student student) {
        StudentView studentView = new StudentView(student.getId(),
                student.getName(), student.getEmail(), student.getAddress());

        return studentView;
    }

    @Override
    public CourseView courseToCourseView(Course course) {
        List<StudentView> studentViewList = new ArrayList<>();

        if(course.getStudents() != null){
            for (Student student : course.getStudents()) {
                StudentView studentView = studentToStudentView(student);
                studentViewList.add(studentView);
            }
        }

        CourseView courseView = new CourseView(course.getId(), course.getCourseName(),
                course.getStartDate(), course.getWeekDuration(), studentViewList);

        return  courseView;
    }

    @Override
    public List<CourseView> coursesToCourseViews(Collection<Course> courses) {
        List<CourseView> courseViewList = new ArrayList<>();
        List<Course> courseList = new ArrayList<>(courses);

        if(courseList != null){
            for (Course course : courseList) {
                courseViewList.add(courseToCourseView(course));
            }
        }

        return courseViewList;
    }

    @Override
    public List<StudentView> studentsToStudentViews(Collection<Student> students) {
        List<StudentView> studentViewList = new ArrayList<>();
        List<Student> studentList = new ArrayList<>(students);

        if(studentList != null){
            for (Student student : studentList) {
                studentViewList.add(studentToStudentView(student));
            }
        }

        return studentViewList;
    }
}