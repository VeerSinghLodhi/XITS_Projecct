package com.example.SamvaadProject.coursepackage;

import com.example.SamvaadProject.usermasterpackage.UserMaster;
import com.example.SamvaadProject.usermasterpackage.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/addcourse")
    public String getPage(Model model){
        model.addAttribute("coursedata" , new CourseMaster());
        return "coursemaster/addnewcourse";
    }

        @PostMapping("/addcourse")
    public String addCourse(@Valid @ModelAttribute("coursedata") CourseMaster courseMaster,
                            BindingResult bindingResult,
                            Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {


        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);// Admin Object for session creation
        if(userMaster==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }
        model.addAttribute("user_master",userMaster);   // Logged in User Means Admin
        model.addAttribute("newUser",new UserMaster());   // For New Registration Object either Student or Faculty.
        model.addAttribute("coursedata" , new CourseMaster());  //  For New Course Object.
        model.addAttribute("allcourses",  courseRepository.findAll()); // All Courses List.

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> System.out.println(objectError.getDefaultMessage()));
            return "AdminHTMLs/admindashboard";
        }
        courseRepository.save(courseMaster);
        CourseMaster saved = courseRepository.save(courseMaster);
        System.out.println("Saved course Id: " + saved.getCourseId());

        redirectAttributes.addAttribute("newCourseAdded",true);  // New Course Added Confirmation.

        return "redirect:/admin/dashboard";  // Back to Dashboard.
//        return "coursemaster/showallcourse";
    }

    @GetMapping("/showallcourse")
    public String showAllCourse(Model model) {
        List<CourseMaster> courseMasters = courseRepository.findAll();
//        System.out.println("Courses in DB: " + courseMasters.size());
//        courseMasters.forEach(c -> System.out.println(c.getCourseName()));

        model.addAttribute("coursedata", courseMasters);
        return "coursemaster/showallcourse";
    }


    @GetMapping("/showallcourseforstudent")
    public String showAllCourseForStudent(Model model, HttpSession session) {
        List<CourseMaster> courseMasters = courseRepository.findAll();
        session.setAttribute("coursedata" , courseMasters);
        model.addAttribute("coursedata", courseMasters);
        return "coursemaster/showallcourse";
    }


    //Delete Course from All Course List.

    @PostMapping("/deletecourse")
    @ResponseBody
    public String deleteCourse(@RequestParam("courseId") Long id){
        courseRepository.deleteById(id);
        return "coursemaster/success";//confirmation popup missing.
    }

    //Update Course Details.

    @PostMapping("/updatecourse")
    public String updateCourse(HttpSession session , Model model , @RequestParam("courseId") Long id){
        List<CourseMaster> courseList = (List<CourseMaster>) session.getAttribute("coursedata");
        CourseMaster courseMaster = courseRepository.findById(id).orElse(null) ;//Edit button se ek courseId bhej rahe hain.
        model.addAttribute("coursedata" , courseMaster);
        return "coursemaster/updatecourse";
    }

    @PostMapping("/saveupdatecourse")
    public String saveUpdateCourse(@ModelAttribute("coursedata") CourseMaster courseMaster , Model model){
        courseRepository.save(courseMaster);
        List<CourseMaster> courseMasters = courseRepository.findAll();
        model.addAttribute("coursedata", courseMasters);
        System.out.println("=======================After updating data==================");
        return "coursemaster/showallcourse";
    }

    @GetMapping("/coursefees/{cid}")
    @ResponseBody
    String getCourseFees(@PathVariable("cid")Long cid){
        return courseRepository.getCourseFees(cid);
    }


    //Data in json
    @GetMapping("/debug")
    @ResponseBody
    public List<CourseMaster> debugCourses() {
        return courseRepository.findAll();
    }

}
