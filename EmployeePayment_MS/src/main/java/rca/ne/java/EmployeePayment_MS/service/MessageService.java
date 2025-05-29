package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Message;
import rca.ne.java.EmployeePayment_MS.entity.Payslip;
import rca.ne.java.EmployeePayment_MS.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JavaMailSender emailSender;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByEmployee(Employee employee) {
        return messageRepository.findByEmployee(employee);
    }

    public List<Message> getMessagesByMonthAndYear(Integer month, Integer year) {
        return messageRepository.findByMonthAndYear(month, year);
    }

    public List<Message> getUnsentMessages() {
        return messageRepository.findBySent(false);
    }

    @Transactional
    public void sendPayslipApprovalMessage(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        String messageContent = createPayslipApprovalMessage(employee, payslip);
        
        // Create message entity
        Message message = new Message();
        message.setEmployee(employee);
        message.setMessage(messageContent);
        message.setMonth(payslip.getMonth());
        message.setYear(payslip.getYear());
        message.setSentAt(LocalDateTime.now());
        

        try {
            sendEmail(employee.getEmail(), "Payslip Approval Notification", messageContent);
            message.setSent(true);
        } catch (Exception e) {
            // Log the error but continue with saving the message
            System.err.println("Failed to send email to " + employee.getEmail() + ": " + e.getMessage());
            message.setSent(false);
        }

        messageRepository.save(message);
    }

    @Transactional
    public void resendUnsentMessages() {
        List<Message> unsentMessages = getUnsentMessages();
        
        for (Message message : unsentMessages) {
            try {
                sendEmail(message.getEmployee().getEmail(), "Payslip Approval Notification", message.getMessage());
                message.setSent(true);
                messageRepository.save(message);
            } catch (Exception e) {
                // Log the error but continue with the next message
                System.err.println("Failed to send email to " + message.getEmployee().getEmail() + ": " + e.getMessage());
            }
        }
    }

    private String createPayslipApprovalMessage(Employee employee, Payslip payslip) {
        return String.format(
                "Dear %s, your salary for %d/%d from RCA amounting to %s has been credited to your account %s successfully.",
                employee.getFirstName(),
                payslip.getMonth(),
                payslip.getYear(),
                payslip.getNetSalary().toString(),
                employee.getCode()
        );
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}