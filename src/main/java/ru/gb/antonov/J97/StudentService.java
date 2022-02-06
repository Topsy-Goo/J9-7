package ru.gb.antonov.J97;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;

    @Transactional (readOnly = true)
    public Student findById (Long id) {
        return inlineFindByID (id);
    }

    @Transactional (readOnly = true)
    public List<Student> findAll () {
        long count = studentRepo.count();
        return iterableToList (studentRepo.findAll(), (int)count);
    }

    @Transactional
    public Student create (String name, Integer age) {
        return studentRepo.save (new Student (name, age));
    }

    @Transactional
    public Student update (Long id, String name, Integer age) {
        Student student = inlineFindByID (id);
        if (student != null  &&  student.setAge (age)  &&  student.setName (name))
            student = studentRepo.save (student);
        else
            student = null;
        return student;
    }

    @Transactional
    public Student deleteById (Long id) {
        Student student = inlineFindByID (id);
        if (student != null)
            studentRepo.deleteById (id);
        return student;
    }

    @Transactional
    public boolean changeAge (Long id, Integer delta) {
        boolean ok = false;
        Student student = inlineFindByID (id);
        if (student != null && student.setAge (student.getAge() + delta))
            ok = studentRepo.save (student) != null;
        return ok;
    }

    private Student inlineFindByID (Long id) {
        return (id != null && id > 0L) ? studentRepo.findById (id).orElse(null)
                                       : null;
    }

/** Преобразуем последовательность Iterable<T> в коллекцию List<T>.
@param count Указывает количество элементов в списке. Если NULL или <= 0, то считается равным 10.
@param iterable Преобразуемая последовательность. */
    public static <T> List<T> iterableToList (Iterable<T> iterable, Integer count) {
        if (count == null || count <= 0)
            count = 10;
        List<T> list = new ArrayList<>(count);
        iterable.forEach(list::add);
        return list;
    }
}
