package com.example.SamvaadProject.usermasterpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionDTO;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import com.example.SamvaadProject.emailservicespackage.EmailService;
import com.example.SamvaadProject.feespackage.FeePayment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    BatchMasterRepository batchMasterRepository;

    @Autowired
    AdmissionRepository admissionRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        UserMaster user = userService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password!");
            return "login";
        }

        session.setAttribute("userId",user.getUserId());

        // ✅ Role based redirection
        switch (user.getRole()) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case STUDENT:
                return "redirect:/student/dashboard";
            case FACULTY:
                return "redirect:/faculty/dashboard";
            default:
                model.addAttribute("error", "Unknown role!");
                return "login";
        }
    }



    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(HttpSession session,
                          Model model,
                          @RequestParam(value = "newCourseAdded",required = false)Boolean isNewCourseAdded,
                          @RequestParam(value = "newUserAdded",required = false)Boolean isNewUserAdded,
                          @RequestParam(value = "newBatchAdded",required = false)Boolean isNewBatchAdded,
                          @RequestParam(value = "newAdmissionAdded",required = false)Boolean isNewAdmissionAdded,
                          @RequestParam(value = "admissionUpdated",required = false)Boolean isAdmissionUpdated,
                          @RequestParam(value = "batchUpdated",required = false)Boolean isBatchUpdated,
                          @RequestParam(value = "studentsMapped",required = false)Boolean isStudentsMapped,
                          @RequestParam(value = "feeSubmitted",required = false)Boolean isFeeSubmitted){

        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);
        if(userMaster==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }

        if (Boolean.TRUE.equals(isNewCourseAdded)) {
            model.addAttribute("newCourseAdded",true);
        }
        if (Boolean.TRUE.equals(isNewUserAdded)) {
            model.addAttribute("newUserAdded", true);
        }
        if (Boolean.TRUE.equals(isNewBatchAdded)) {
            model.addAttribute("newBatchAdded", true);
        }
        if (Boolean.TRUE.equals(isNewAdmissionAdded)) {
            model.addAttribute("newAdmissionAdded", true);
        }
        if (Boolean.TRUE.equals(isAdmissionUpdated)) {
            model.addAttribute("admissionUpdated", true);
        }
        if (Boolean.TRUE.equals(isBatchUpdated)) {
            model.addAttribute("batchUpdated", true);
        }
        if (Boolean.TRUE.equals(isStudentsMapped)) {
            model.addAttribute("studentsMapped", true);
        }
        if (Boolean.TRUE.equals(isFeeSubmitted)) {
            model.addAttribute("feeSubmitted", true);
        }


        model.addAttribute("user_master",userMaster);   // Logged-in User Means Admin
        model.addAttribute("newUser",new UserMaster());   // For New Registration Object either Student or Faculty.
        model.addAttribute("coursedata" , new CourseMaster());  //  For New Course Object.
        model.addAttribute("allcourses",  courseRepository.findAll()); // All Courses List.
        model.addAttribute("allstudents",userRepository.findByRoleOrderByFullNameAsc(UserMaster.Role.STUDENT)); // All Student In ComboBox for Admission.
        model.addAttribute("allcourses",courseRepository.findAll());  // All Courses List.
        model.addAttribute("newbatch",new BatchMaster());// For add new Batch
        model.addAttribute("allfaculties",userRepository.findByRoleOrderByFullNameAsc(UserMaster.Role.FACULTY));  // All Faculties for new batch creation.
        model.addAttribute("allbatches",batchMasterRepository.findAll()); // All Batches List.
        model.addAttribute("newadmission",new AdmissionMaster());  // Object for new Admission.
        model.addAttribute("alladmissions",admissionRepository.findAll(Sort.by(Sort.Direction.ASC,"admissionId"))); // All Admissions for Updating
        model.addAttribute("feePayment", new FeePayment()); // For new Fees

        List<AdmissionDTO> allAdmissions = admissionRepository.findAll()
                .stream()
                .map(ad -> new AdmissionDTO(
                        ad.getAdmissionId(),
                        ad.getUserMaster().getFullName(),
                        ad.getCourse().getCourseId()   // include courseId
                ))
                .toList();

        model.addAttribute("alladmissions2", allAdmissions);
        model.addAttribute("allstudentusers",userRepository.findByRoleOrderByFullNameAsc(UserMaster.Role.STUDENT));


        return "AdminHTMLs/admindashboard";
    }

    @GetMapping("/faculty/dashboard")
    public String getFacultyDashboard(HttpSession session,
                          Model model){
        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);
        if(userMaster==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }
        model.addAttribute("user_master",userMaster);
        return "FacultyHTMLs/facultydashboard";
    }

    @GetMapping("/student/dashboard")
    public String getStudentDashboard(HttpSession session,
                                      Model model){
        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);
        if(userMaster==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }
        model.addAttribute("user_master",userMaster);
        return "StudentHTMLs/studentdashboard";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }


    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute("newUser")UserMaster newUser,
                             @RequestParam("userPhoto")MultipartFile userPhoto,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes){
        try {
            System.out.println(newUser.getFullName());
            System.out.println(newUser.getDob());
            if(!userPhoto.isEmpty())
                newUser.setPhotoPath(userPhoto.getBytes());
            else
                newUser.setPhotoPath(null);

            newUser.setCreatedOn(new Date());
            System.out.println("Username is "+getGenerateUsername(newUser.getFullName(),newUser.getDob()));
            newUser.setUsername(getGenerateUsername(newUser.getFullName(),newUser.getDob()));
            newUser.setPassword("Xits@143");

            Long adminId=(Long) session.getAttribute("userId");
            UserMaster admin=userRepository.findById(adminId).orElse(null);
            if(admin==null){
                model.addAttribute("error","Session expired!");
                return "login";
            }
            redirectAttributes.addAttribute("newUserAdded",true);
            userRepository.save(newUser);
            emailService.getSendUsernameAndPassword(newUser.getFullName(),newUser.getUsername(),newUser.getPassword(),newUser.getEmail());
            return "redirect:/admin/dashboard";

        }catch(Exception e){
            System.out.println("Error is "+e);
            model.addAttribute("error",e.getMessage());
            return "login";
        }

    }

    public String getGenerateUsername(String uName, LocalDate dob){
        uName = uName.toLowerCase().replaceAll("\\s+", "");
        return (uName.length() >= 4 ? uName.substring(0, 4) : uName)+""+(userRepository.getMaxCount()+1)+""+dob.getYear();
    }

    @GetMapping("/userphoto")
    ResponseEntity<byte[]>getUserProfile(HttpSession session,Model model){

        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);// Admin Object for session creation
        if(userMaster==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userMaster.getPhotoPath());

    }


}
