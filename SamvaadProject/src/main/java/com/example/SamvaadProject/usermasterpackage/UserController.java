package com.example.SamvaadProject.usermasterpackage;

import com.example.SamvaadProject.admissionpackage.AdmissionDTO;
import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import com.example.SamvaadProject.assignmentpackage.AssignmentMaster;
import com.example.SamvaadProject.assignmentpackage.AssignmentRepository;
import com.example.SamvaadProject.assignmentpackage.SubmitAssignment;
import com.example.SamvaadProject.assignmentpackage.SubmitRepository;
import com.example.SamvaadProject.attendancepackage.AttendanceMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMaster;
import com.example.SamvaadProject.batchmasterpackage.BatchMasterRepository;
import com.example.SamvaadProject.coursepackage.CourseMaster;
import com.example.SamvaadProject.coursepackage.CourseRepository;
import com.example.SamvaadProject.emailservicespackage.EmailService;
import com.example.SamvaadProject.emailservicespackage.OtpService;
import com.example.SamvaadProject.feespackage.FeePayment;
import com.example.SamvaadProject.pdfpackage.PdfDTO;
import com.example.SamvaadProject.pdfpackage.PdfMaster;
import com.example.SamvaadProject.pdfpackage.PdfRepository;
import com.example.SamvaadProject.studentbatchpackage.StudentBatchRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    StudentBatchRepository studentBatchRepository;

    @Autowired
    SubmitRepository submitRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    PdfRepository pdfRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    public String processLogin(@RequestParam String username,
//                               @RequestParam String password,
//                               Model model,
//                               HttpSession session) {
//        UserMaster user = userService.login(username, password);
//
//        if (user == null) {
//            model.addAttribute("error", "Invalid username or password!");
//            return "login";
//        }
//
//        if(!user.getStatus()){
//            model.addAttribute("error","This account has been disabled by the admin!!");
//            return "login";
//        }
//
//        session.setAttribute("userId",user.getUserId());
//
//        // ✅ Role based redirection
//        switch (user.getRole()) {
//            case ADMIN:
//                return "redirect:/admin/dashboard";
//            case STUDENT:
//                return "redirect:/student/dashboard";
//            case FACULTY:
//                return "redirect:/faculty/dashboard";
//            default:
//                model.addAttribute("error", "Unknown role!");
//                return "login";
//        }
//    }



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
                          @RequestParam(value = "feeSubmitted",required = false)Boolean isFeeSubmitted,
                          @RequestParam(value = "studyMaterialUploaded",required = false)Boolean isStudyMaterialUploaded,
                          @RequestParam(value = "notePdfDeleted",required = false)Boolean isNotePdfDeleted,
                          @RequestParam(value = "facultyUpdated",required = false)Boolean isFacultyUpdated){

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
        if (Boolean.TRUE.equals(isStudyMaterialUploaded)) {
            model.addAttribute("studyMaterialUploaded", true);
        }
        if (Boolean.TRUE.equals(isNotePdfDeleted)) {
            model.addAttribute("notePdfDeleted", true);
        }
        if (Boolean.TRUE.equals(isFacultyUpdated)) {
            model.addAttribute("facultyUpdated", true);
        }


        model.addAttribute("user_master",userMaster);   // Logged-in User Means Admin
        model.addAttribute("newUser",new UserMaster());   // For New Registration Object either Student or Faculty.
        model.addAttribute("coursedata" , new CourseMaster());  //  For New Course Object.
        model.addAttribute("allcourses",  courseRepository.findAll()); // All Courses List.
        model.addAttribute("allstudents",userRepository.findByRoleOrderByFullNameAsc(UserMaster.Role.STUDENT)); // All Student In ComboBox for Admission.
        model.addAttribute("allcourses", courseRepository.findAll(Sort.by(Sort.Direction.ASC, "courseId")));// All Courses List.
        model.addAttribute("newbatch",new BatchMaster());// For add new Batch
        model.addAttribute("allfaculties",userRepository.findByRoleOrderByFullNameAsc(UserMaster.Role.FACULTY));  // All Faculties for new batch creation.
        model.addAttribute("allbatches",batchMasterRepository.findAll()); // All Batches List.
        model.addAttribute("allactivebatches",batchMasterRepository.findAllBatchByStatus("ACTIVE"));
        model.addAttribute("newadmission",new AdmissionMaster());  // Object for new Admission.
        model.addAttribute("alladmissions",admissionRepository.findAll(Sort.by(Sort.Direction.ASC,"admissionId"))); // All Admissions for Updating
        model.addAttribute("feePayment", new FeePayment()); // For new Fees
//        List<AttendanceMaster> AllattendanceOfStudents=attendanceRepository.findByAdmissionIds(AdmissionIds);
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
//        model.addAttribute("studentdetai")


        return "AdminHTMLs/admindashboard";
    }

    @GetMapping("/faculty/dashboard")
    public String getFacultyDashboard(HttpSession session,
                          Model model,
                          @RequestParam(value = "newAssignmentAdded",required = false)Boolean isNewAssignmentAdded,
                          @RequestParam(value = "assignmentDeleted",required = false)Boolean isAssignmentDelete,
                          @RequestParam(value = "assignmentUpdated",required = false)Boolean isAssignmentUpdate ,
                          @RequestParam(value = "attendanceMarked",required = false)Boolean isAttendanceMarked,
                          @RequestParam(value = "attendanceUpdated",required = false)Boolean isAttendanceUpdated,
                          @RequestParam(value = "attendanceAlreadyErrorMessage",required = false)Boolean isAttendanceAlreadyErrorMessage){
        Long userId=(Long) session.getAttribute("userId");
        UserMaster userMaster=userRepository.findById(userId).orElse(null);
        if(userMaster==null){
            model.addAttribute("error","Session expired!");
            return "login";
        }

        if (Boolean.TRUE.equals(isNewAssignmentAdded)) {
            model.addAttribute("newAssignmentAdded", true);
        }
        if (Boolean.TRUE.equals(isAssignmentDelete)) {
            model.addAttribute("assignmentDeleted", true);
        }
        if (Boolean.TRUE.equals(isAssignmentUpdate)) {
            model.addAttribute("assignmentUpdated", true);
        }
        if (Boolean.TRUE.equals(isAttendanceMarked)) {
            model.addAttribute("attendanceMarked", true);
        }
        if (Boolean.TRUE.equals(isAttendanceUpdated)) {
            model.addAttribute("attendanceUpdated", true);
        }
        if (Boolean.TRUE.equals(isAttendanceAlreadyErrorMessage)) {
            model.addAttribute("attendanceAlreadyErrorMessage", true);
        }

//        model.addAttribute("groupedAssignments",admissionRepository.findByUserMaster_UserId(userMaster.getUserId()));
    model.addAttribute("batches",batchMasterRepository.getAllBatchesByFaculty(userMaster.getUserId(),"ACTIVE")); //checking Status Too Active batch or archived
        model.addAttribute("user_master",userMaster);


//        Attendance Part
        model.addAttribute("selectedDate",LocalDate.now());



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
        System.out.println("User id "+userId);
//        List<AdmissionMaster>adm=admissionRepository.findByUserMaster_UserId(userId);
//        System.out.println("Size is "+adm.size());
        List<Long> batchIds1 = admissionRepository.findByUserMaster_UserId(userId)
                .stream()
                .map(ad -> Optional.ofNullable(studentBatchRepository.findByAdmission_AdmissionId(ad.getAdmissionId()))
                        .map(sb -> sb.getBatch())
                        .map(batch -> batch.getBatchId())
                        .orElse(null))   // orElse(-1L) if you want default instead of null
                .filter(Objects::nonNull)  // remove nulls if you don't want them
                .toList();


        List<BatchMaster> batches1 = batchMasterRepository.findAllById(batchIds1);
        model.addAttribute("studentbatches", batches1);


        model.addAttribute("user_master",userMaster);
        return "StudentHTMLs/studentdashboard";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }


    @PostMapping("/admin/registration")
    public String addNewUser(@ModelAttribute("newUser")UserMaster newUser,
                             @RequestParam("userPhoto")MultipartFile userPhoto,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes){
        try {
            System.out.println("Registration Okk");
            if(!userPhoto.isEmpty())
                newUser.setPhotoPath(userPhoto.getBytes());
            else
                newUser.setPhotoPath(null);

            newUser.setCreatedOn(new Date());
            newUser.setRoleList(List.of("ROLE_"+newUser.getRole()));
            System.out.println("Username is "+getGenerateUsername(newUser.getFullName(),newUser.getDob()));
            newUser.setUsername(getGenerateUsername(newUser.getFullName(),newUser.getDob()));
            String encodedPassword = passwordEncoder.encode("Xits@143");
            newUser.setPassword(encodedPassword);
            newUser.setStatus(true);

            Long adminId=(Long) session.getAttribute("userId");
            UserMaster admin=userRepository.findById(adminId).orElse(null);
            if(admin==null){
                model.addAttribute("error","Session expired!");
                return "login";
            }
            System.out.println("Before Registration");
            userRepository.save(newUser);
            System.out.println("After Registration");
            redirectAttributes.addAttribute("newUserAdded",true);
            emailService.getSendUsernameAndPassword(newUser.getFullName(),newUser.getUsername(),"Xits@143",newUser.getEmail());
            return "redirect:/admin/dashboard#student";

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

    @GetMapping("/userphoto/{userId}")
    ResponseEntity<byte[]>getUserProfile(@PathVariable("userId")Long userId){

        UserMaster userMaster=userRepository.findById(userId).orElse(null);// Admin Object for session creation
        if(userMaster==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(userMaster.getPhotoPath());

    }


    // For get Faculty as well as student Data
    @GetMapping("/faculty/data/{facultyId}")  //7
    @ResponseBody
    public FacultyDTO getFacultyData(@PathVariable("facultyId")Long facultyId){
         UserMaster faculty= userRepository.findById(facultyId).orElse(null);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         FacultyDTO facultyDTO=new FacultyDTO();
         facultyDTO.setFacultyId(faculty.getUserId());
         facultyDTO.setFacultyName(faculty.getFullName());
         facultyDTO.setFacultyEmail(faculty.getEmail());
         facultyDTO.setFacultyContactNo(faculty.getContactNo());
         facultyDTO.setFacultyDOB(faculty.getDob());
         facultyDTO.setFacultyAddress(faculty.getAddress());
         facultyDTO.setFacultyCity(faculty.getCity());
         facultyDTO.setFacultyStatus(faculty.getStatus().toString());
         facultyDTO.setPhoto(faculty.getPhotoPath());
         System.out.println("Faculty Status "+faculty.getStatus().toString());


         return facultyDTO;
    }

    // For Faculty as well as student update
    @PostMapping("/admin/faculty/update")
    public String getUpdate(@RequestParam("facultySelectId")Long facultyId,
                            @ModelAttribute("newUser")UserMaster facultyData,
                            @RequestParam("facultyPhoto")MultipartFile facultyPhoto,
                            Model model,
                            RedirectAttributes redirectAttributes){
            try {
                UserMaster updateFaculty = userRepository.findById(facultyId).orElse(null);
                updateFaculty.setFullName(facultyData.getFullName());
                updateFaculty.setEmail(facultyData.getEmail());
                updateFaculty.setContactNo(facultyData.getContactNo());
                updateFaculty.setDob(facultyData.getDob());
                updateFaculty.setAddress(facultyData.getAddress());
                updateFaculty.setCity(facultyData.getCity());
                updateFaculty.setStatus(facultyData.getStatus());  // IMP!!

                if (!facultyPhoto.isEmpty()) {
                    updateFaculty.setPhotoPath(facultyPhoto.getBytes());
                }

                userRepository.save(updateFaculty);


                redirectAttributes.addAttribute("facultyUpdated",true);

//                updateFaculty.getRole().toString();
//                System.out.println("Role is "+updateFaculty.getRole().toString());
                System.out.println(updateFaculty.getRole());
                if(updateFaculty.getRole().toString().equals("FACULTY"))
                     return "redirect:/admin/dashboard#allfaculties";
                else
                     return "redirect:/admin/dashboard#allstudents";

            }catch(Exception e){
                System.out.println("Error is "+e.getMessage());
                model.addAttribute("error","Something went wrong!");
                return "login";
            }

//        System.out.println("Faculty Id "+facultyId);
//        System.out.println("Faculty Name "+facultyData.getFullName());
//        System.out.println("Faculty DOB "+facultyData.getDob());
//        System.out.println("Faculty Address "+facultyData.getAddress());
//        System.out.println("Faculty Status "+facultyData.getStatus());
//        System.out.println("Faculty File "+facultyPhoto.getOriginalFilename());

    }


    @GetMapping("/student/course/material/batch/{batchId}")
    @ResponseBody
    public List<PdfDTO> getMaterialByBatch(@PathVariable("batchId")Long batchId){
        Long courseId=batchMasterRepository.findById(batchId).orElse(null).getCourse().getCourseId();
        return pdfRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(pdf->new PdfDTO(pdf.getDocumentName(),pdf.getUploadedAt().toString(),pdf.getPdfId()))
                .toList();
    }

//    Download Notes
@GetMapping("/student/notes/download/{pdfId}")
public ResponseEntity<byte[]> getResume(@PathVariable("pdfId")Long pdfId) {

    PdfMaster pdfMaster=pdfRepository.findById(pdfId).orElse(null);
    if (pdfMaster == null || pdfMaster.getDocumentPath() == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF) // or whatever format you accept
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+pdfMaster.getDocumentName()+"\"")
            .body(pdfMaster.getDocumentPath());
    }

    @PostMapping("/faculty/updateFaculty")
    @ResponseBody
    public Map<String,String>getUpdateFaculty(@ModelAttribute UserMaster userMaster){
        UserMaster master=userRepository.findById(userMaster.getUserId()).orElse(null);

        master.setFullName(userMaster.getFullName().trim());
        master.setAddress(userMaster.getAddress().trim());
        master.setContactNo(userMaster.getContactNo().trim());
        master.setEmail(userMaster.getEmail().trim());
        master.setDob(userMaster.getDob());
        userRepository.save(master);

        Map<String,String>response=new HashMap<>();
        response.put("message","Details have been updated!");
        return response;
    }

    @PostMapping("/student/updateStudent")
    @ResponseBody
    public Map<String,String>getUpdateStudent(@ModelAttribute UserMaster userMaster){
        UserMaster master=userRepository.findById(userMaster.getUserId()).orElse(null);

        master.setFullName(userMaster.getFullName().trim());
        master.setAddress(userMaster.getAddress().trim());
        master.setContactNo(userMaster.getContactNo().trim());
        master.setEmail(userMaster.getEmail().trim());
        master.setDob(userMaster.getDob());
        userRepository.save(master);

        Map<String,String>response=new HashMap<>();
        response.put("message","Details have been updated!");
        return response;
    }

    @PostMapping("/update-password")
    @ResponseBody
    public Map<String,Boolean> updatePassword(@RequestParam String currentPassword,
                                                 @RequestParam String newPassword,
                                                 Principal principal) {
//        System.out.println("Principal Name "+principal.getName());
//        System.out.println("Inside the Update Password Faculty Method");
        String res=userService.updatePassword(principal.getName(), currentPassword, newPassword);
        Map<String,Boolean>response=new HashMap<>();
        response.put("incorrect",false);
        response.put("samePassword",false);
        response.put("updated",false);
        if(res.equals("currentFalse"))
            response.replace("incorrect",false,true);
        if(res.equals("currentSame"))
            response.replace("samePassword",false,true);
        if(res.equals("updated"))
            response.replace("updated",false,true);

        System.out.println("Inside the Update Password Faculty Method Completed");
        return response;
    }



//    ==========================  OTP Part   =======================

    @Autowired
    OtpService otpService;


    // OTP Page
    @GetMapping("/request-password-update")
    public String getRequestPasswordUpdate(){
        return "resetPasswordByOTP";
    }



    //  Send OTP for password set
    @PostMapping("/request-password-update")
    @ResponseBody
    public Map<String,Boolean> requestPasswordUpdate(@RequestParam("username") String  username) {

        Map<String,Boolean>response=new HashMap<>();
        response.put("usernameNotFound",false);
        response.put("optSent",false);

        UserMaster master=userRepository.findByUsername(username.trim());

        if(master!=null){
            String otp = otpService.generateOtp(master.getEmail());
            emailService.sendOtpEmail(master.getEmail(), otp);
            response.replace("otpSent",false,true);
        }else{
            response.replace("usernameNotFound",false,true);
        }

        return response;
    }

    @PostMapping("/verify-otp-and-update-password2")
    public ResponseEntity<String> verifyOtpAndUpdatePassword(@RequestParam() String otp,
                                                             @RequestParam() String newPassword,
                                                             Principal principal) {
        String email = principal.getName();
System.out.println("email");
        if (!otpService.validateOtp(email, otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }

        // OTP valid → update password
        UserMaster user = userRepository.findByUsername(email);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpService.clearOtp(email);
        return ResponseEntity.ok("Password updated successfully");
    }


@PostMapping("/verify-otp-and-update-password")
@ResponseBody
public Map<String, Boolean> verifyOtpAndUpdatePassword2(
        @RequestParam("otp") String otp,
        @RequestParam("newPassword") String newPassword,
        @RequestParam("username") String username) {
    System.out.println("Method Called");
    Map<String, Boolean> response = new HashMap<>();
    response.put("invalidOrExpired", false);
    response.put("userNotFound", false);
    response.put("passwordUpdated", false);

    UserMaster user = userRepository.findByUsername(username);

    if (user == null) {
        response.replace("userNotFound", true);
        return response;
    }

    boolean isValidOtp = otpService.validateOtp(user.getEmail(), otp);
    if (!isValidOtp) {
        response.replace("invalidOrExpired", true);
        return response;
    }

    // ✅ OTP valid → update password
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    otpService.clearOtp(user.getEmail());

    response.replace("passwordUpdated", true);
    return response;
}





}
