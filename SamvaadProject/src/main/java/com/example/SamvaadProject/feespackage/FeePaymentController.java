package com.example.SamvaadProject.feespackage;

import com.example.SamvaadProject.admissionpackage.AdmissionMaster;
import com.example.SamvaadProject.admissionpackage.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fees")
public class FeePaymentController {

    @Autowired
    private FeeRepository feePaymentRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @GetMapping("/feenew")
    public String showFeeForm(Model model) {
        model.addAttribute("feePayment", new FeePayment());
        model.addAttribute("admissions", admissionRepository.findAll());
        return "fee-form";
    }

    @PostMapping("/save")
    public String saveFee(@RequestParam("admissionId") String admissionId,
                          @ModelAttribute("feePayment") FeePayment feePayment,
                          RedirectAttributes redirectAttributes) {

        AdmissionMaster admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Admission ID: " + admissionId));

        Double balance=admission.getBalance();
        System.out.println("Balance "+balance);

        Double amount=feePayment.getAmount();
        admission.setBalance(balance-amount);

        feePayment.setAdmission(admission);
        feePayment.setPaymentDate(new Date());
        admissionRepository.save(admission);// Updated Balance
        feePaymentRepository.save(feePayment);

        redirectAttributes.addAttribute("feeSubmitted",true);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/list")
    public String listFees(Model model) {

        model.addAttribute("fees", feePaymentRepository.findAllByOrderByFeeIdDesc()); //decending order list
        return "fee-list";
    }

    @GetMapping("/edit/{id}")
    public String editFee(@PathVariable("id") Long id, Model model) {
        FeePayment feePayment = feePaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Fee ID: " + id));


        model.addAttribute("feePayment", feePayment);
        model.addAttribute("admissions", admissionRepository.findAll());

        return "fee-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteFee(@PathVariable("id") Long id) {
        FeePayment feePayment = feePaymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Fee ID: " + id));

        feePaymentRepository.delete(feePayment);
        return "redirect:/fees/list";
    }




    // ==============================================Second Controller Codes===================================

    @GetMapping("/myfees")
    public String showSearchPage() {
        return "student-fee-search";
    }

    @PostMapping("/myfees")
    public String getMyFees(@RequestParam("admissionId") String admissionId, Model model) {
        AdmissionMaster admission = admissionRepository.findById(admissionId).orElse(null);

        if (admission == null) {
            model.addAttribute("error", "Invalid Admission ID!");
            return "student-fee-search";
        }

        List<FeePayment> myFees = feePaymentRepository.findByAdmission_AdmissionId(admissionId);

        double totalPaid = myFees.stream().mapToDouble(FeePayment::getAmount).sum();
        double remaining = admission.getFees() - totalPaid - admission.getDiscount();


        model.addAttribute("admission", admission);
        model.addAttribute("fees", myFees);
        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("remaining", remaining);

        return "student-fee-list";
    }
}









