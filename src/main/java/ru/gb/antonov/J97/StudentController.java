package ru.gb.antonov.J97;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping ("/")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    public static final String
        PROMPT_MARK_CHANGED     = "Возраст изменён.",
        PROMPT_MARK_NOT_CHANGED = "Не удалось изменить возраст.",
        REDIRECT_TO_SHOW_ALL    = "redirect:/showallstudents",
        PAGE_NAME_FORM          = "form",
        PAGE_NAME_ALLSTUDENTS   = "allstudents",
        PAGE_NAME_VIEW          = "view",
        ATTRIBUTE_PROMPT        = "prompt",
        ATTRIBUTE_ALLSTUDENTS   = "allthestudents",
        ATTRIBUTE_STUDENT       = "student"
        ;
    private String prompt4Allstudents = "";

    @GetMapping                         //http://localhost:17333/j97
    public String showMainPage() {    return REDIRECT_TO_SHOW_ALL;    }

    @GetMapping ("/showallstudents")    // http://localhost:17333/j97/showallstudents
    public String getAll (Model model) {
        model.addAttribute (ATTRIBUTE_ALLSTUDENTS, studentService.findAll());
        String msg = prompt4Allstudents;
        prompt4Allstudents = "";
        model.addAttribute (ATTRIBUTE_PROMPT, msg);
        return PAGE_NAME_ALLSTUDENTS;
    }

/** Вызывается при перенапрвлении на страницу просмотра информации. */
    @GetMapping ("/view/{id}")
    public String viewItem (@PathVariable Long id, Model model) {

        Student student = studentService.findById(id);
        if (student == null) {
            model.addAttribute (ATTRIBUTE_PROMPT, "Ошибка! Не удалось получить инфорацию о тсудента.");
            return PAGE_NAME_VIEW;
        }
        model.addAttribute (ATTRIBUTE_PROMPT, "Информация о студенте получена.");
        model.addAttribute (ATTRIBUTE_STUDENT, student);
        return PAGE_NAME_VIEW;
    }

/** Вызывается при перенапрвлении на страницу формы. */
    @GetMapping ("/edit/{id}")
    public String editItem (@PathVariable Long id, Model model) {

        Student student = studentService.findById(id);
        if (student == null) {
            model.addAttribute (ATTRIBUTE_PROMPT, "Ошибка!");
            return REDIRECT_TO_SHOW_ALL;
        }
        model.addAttribute (ATTRIBUTE_PROMPT, "Заполните форму и нажмите кнопку 'Сохранить'.");
        model.addAttribute (ATTRIBUTE_STUDENT, student);
        return PAGE_NAME_FORM;
    }

/** Вызывается при перенапрвлении на страницу формы. */
    @GetMapping ("/create")             // http://localhost:17333/j97/create
    public String createItem (Model model) {
        model.addAttribute (ATTRIBUTE_PROMPT, "Заполните форму и нажмите кнопку 'Сохранить'.");
        model.addAttribute (ATTRIBUTE_STUDENT, new Student("(без имени)", 1));
        return PAGE_NAME_FORM;
    }

/** Вызывается при нажатии submit-кнопки в форме. */
    @PostMapping ("/create")
    public String createOrUpdateItem (@RequestParam (name = "name") String name,
                                      @RequestParam (name = "age") Integer age,
                                      @RequestParam (name = "id", required = false) Long id, Model model)
    {   Student student;
        String msg;
        if (id == null) {
            student = studentService.create (name, age);
            msg = student != null ? "Студент создан." : "Не удалось создать студента.";
        }
        else {
             student = studentService.update (id, name, age);
             msg = student != null ? "Студент изменён." : "Не удалось изменить студента.";
        }
        model.addAttribute (ATTRIBUTE_PROMPT, msg);
        model.addAttribute (ATTRIBUTE_STUDENT, student);
        return PAGE_NAME_FORM;
    }

    @GetMapping ("/delete/{id}")
    public String deleteItem (@PathVariable Long id, Model model) {
        prompt4Allstudents = (studentService.deleteById (id) != null)
                                ? "Студент удалён." : "Не удалось удалить студента.";
        return REDIRECT_TO_SHOW_ALL;
    }

    @GetMapping ("/decrease/{id}")
    public String decrease (@PathVariable Long id, Model model) {
        prompt4Allstudents = (studentService.changeAge (id, -1))
                                ? PROMPT_MARK_CHANGED : PROMPT_MARK_NOT_CHANGED;
        return REDIRECT_TO_SHOW_ALL;
    }

    @GetMapping ("/increase/{id}")
    public String increase (@PathVariable Long id, Model model) {
        prompt4Allstudents = (studentService.changeAge (id, 1))
                                ? PROMPT_MARK_CHANGED : PROMPT_MARK_NOT_CHANGED;
        return REDIRECT_TO_SHOW_ALL;
    }
}
