package se.lexicon.course_manager_assignment.data.dao;

import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;


public class CourseCollectionRepository implements CourseDao{

    private Collection<Course> courses;

    public CourseCollectionRepository(Collection<Course> courses) {
        this.courses = courses;
    }

    @Override
    public Course createCourse(String courseName, LocalDate startDate, int weekDuration) {
        Course course = new Course(CourseSequencer.nextCourseId(), courseName, startDate, weekDuration);
        courses.add(course);

        return course;
    }

    @Override
    public Course findById(int id) {
        for (Course course : courses){
            if (course.getId() == id){
                return course;
            }
        }

        return null;
    }

    @Override
    public Collection<Course> findByNameContains(String name) {
        Collection<Course> courseCollection = new HashSet<>();

        for (Course course : courses){
            if (course.getCourseName().toLowerCase().contains(name.toLowerCase())){
                courseCollection.add(course);
            }
        }

        return courseCollection;
    }

    @Override
    public Collection<Course> findByDateBefore(LocalDate end) {
        Collection<Course> courseCollection = new HashSet<>();

        long days = 0L;
        int courseDuration = 0;

        for (Course course : courses){
            days = ChronoUnit.DAYS.between( course.getStartDate() , LocalDate.now() );
            courseDuration = course.getWeekDuration();

            if (days/7 > courseDuration){
                courseCollection.add(course);
            }
        }

        return courseCollection;
    }

    @Override
    public Collection<Course> findByDateAfter(LocalDate start) {
        Collection<Course> courseCollection = new HashSet<>();

        for (Course course : courses){
            if (course.getStartDate().isAfter(LocalDate.now())){
                courseCollection.add(course);
            }
        }

        return courseCollection;
    }

    @Override
    public Collection<Course> findAll() {
        return courses;
    }

    @Override
    public Collection<Course> findByStudentId(int studentId) {
        Collection<Course> courseCollection = new HashSet<>();

        for (Course course : courses) {
            for (Student student : course.getStudents()) {
                if (student.getId() == studentId) {
                    courseCollection.add(course);
                    continue;
                }
            }
        }

        return courseCollection;
    }

    @Override
    public boolean removeCourse(Course course ) {
        for (Course courseInCollection : courses){
            if (courseInCollection.equals(course)){
                courses.remove(courseInCollection);
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        this.courses = new HashSet<>();
    }

    @Override
    public Course updateCourse(int id, String courseName, LocalDate startDate, int weekDuration) {
        Course course = findById(id);

        if (course != null){
            course.setCourseName(courseName);
            course.setStartDate(startDate);
            course.setWeekDuration(weekDuration);
        }

        return course;
    }

    @Override
    public Course addStudentToCourse(int courseId, Student student) {
        Course course = findById(courseId);

        if (course != null && student != null){
            course.getStudents().add(student);
        }

        return course;
    }

    @Override
    public Course removeStudentFromCourse(int courseId, Student student) {
        Course course = findById(courseId);

        if (course != null && student != null){
            course.getStudents().remove(student);
        }

        return course;
    }

}
