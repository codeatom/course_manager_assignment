package se.lexicon.course_manager_assignment.data.service.course;

import se.lexicon.course_manager_assignment.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.service.converter.Converters;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.model.Student;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class CourseManager implements CourseService {

    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final Converters converters;

    @Autowired
    public CourseManager(CourseDao courseDao, StudentDao studentDao, Converters converters) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.converters = converters;
    }

    @Override
    public CourseView create(CreateCourseForm form) {
        Course course = courseDao.createCourse(form.getCourseName(), form.getStartDate(), form.getWeekDuration());
        return converters.courseToCourseView(course);
    }

    @Override
    public List<CourseView> searchByCourseName(String courseName) {
        Collection<Course> courseCollection = courseDao.findByNameContains(courseName);
        List<CourseView> courseViews = converters.coursesToCourseViews(courseCollection);

        return courseViews;
    }

    @Override
    public List<CourseView> searchByDateBefore(LocalDate end) {
        Collection<Course> courseCollection = courseDao.findByDateBefore(end);
        List<CourseView> courseViews = converters.coursesToCourseViews(courseCollection);

        return courseViews;
    }

    @Override
    public List<CourseView> searchByDateAfter(LocalDate start) {
        Collection<Course> courseCollection = courseDao.findByDateAfter(start);
        List<CourseView> courseViews = converters.coursesToCourseViews(courseCollection);

        return courseViews;
    }

    @Override
    public boolean addStudentToCourse(int courseId, int studentId) {
        Course course = courseDao.findById(courseId);
        Student student = studentDao.findById(studentId);

        if(course == null || student == null){
            return false;
        }

        Course courseToUpdate = courseDao.addStudentToCourse(courseId, student);

        return true;
    }

    @Override
    public boolean removeStudentFromCourse(int courseId, int studentId) {
        Course course = courseDao.findById(courseId);
        Student student = studentDao.findById(studentId);

        if(course == null || student == null){
            return false;
        }

        Course courseToUpdate = courseDao.removeStudentFromCourse(courseId, student);

        return true;
    }

    @Override
    public CourseView findById(int id) {
        return converters.courseToCourseView(courseDao.findById(id));
    }

    @Override
    public List<CourseView> findAll() {
        return converters.coursesToCourseViews(courseDao.findAll());
    }

    @Override
    public List<CourseView> findByStudentId(int studentId) {
        Collection<Course> courseCollection = courseDao.findByStudentId(studentId);
        List<CourseView> courseViews = converters.coursesToCourseViews(courseCollection);

        return courseViews;
    }

    @Override
    public CourseView update(UpdateCourseForm form) {
        Course course = courseDao.updateCourse(form.getId(), form.getCourseName(), form.getStartDate(), form.getWeekDuration());
        return converters.courseToCourseView(course);
    }

    @Override
    public boolean deleteCourse(int id) {
        Course course = courseDao.findById(id);
        return courseDao.removeCourse(course);
    }
}