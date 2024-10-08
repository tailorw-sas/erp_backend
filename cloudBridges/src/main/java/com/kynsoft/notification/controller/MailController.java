package com.kynsoft.notification.controller;

import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.notification.application.command.sendMailjetEmail.SendMailJetEMailCommand;
import com.kynsof.share.core.application.mailjet.SendMailJetEMailRequest;
import com.kynsoft.notification.application.command.sendMailjetEmail.SendMailjetEmailMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {

//    @Autowired
//    private final IEmailService emailService;
    private final IMediator mediator;
    public MailController( IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendMailJetEMailRequest emailRequest){
        SendMailJetEMailCommand sendMailJetEMailCommand = SendMailJetEMailCommand.fromRequest(emailRequest);
        SendMailjetEmailMessage sendMailjetEmailMessage = mediator.send(sendMailJetEMailCommand);
        return ResponseEntity.ok(sendMailjetEmailMessage);
//        boolean result = true;
//        String msg = "";
////        if (fileParam.isEmpty()) {
////            return ResponseEntity.ok(new SendEmailResponse(result, msg)));
////        }
//        try {
//            List<MailJetAttachment> mailJetAttachments = new ArrayList<>();
//            //String encodedString = convertFileToBase64(fileParam);
//           // mailJetAttachments.add(new MailJetAttachment("application/pdf", "example.pdf", encodedString));
////            mailJetAttachments.add(new MailJetAttachment("image/png", "image.png", "iVBORw0KGgoAAAANSUhEUgAAABQAAAALCAYAAAB/Ca1DAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAB3RJTUUH4wIIChcxurq5eQAAAAd0RVh0QXV0aG9yAKmuzEgAAAAMdEVYdERlc2NyaXB0aW9uABMJISMAAAAKdEVYdENvcHlyaWdodACsD8w6AAAADnRFWHRDcmVhdGlvbiB0aW1lADX3DwkAAAAJdEVYdFNvZnR3YXJlAF1w/zoAAAALdEVYdERpc2NsYWltZXIAt8C0jwAAAAh0RVh0V2FybmluZwDAG+aHAAAAB3RFWHRTb3VyY2UA9f+D6wAAAAh0RVh0Q29tbWVudAD2zJa/AAAABnRFWHRUaXRsZQCo7tInAAABV0lEQVQokaXSPWtTYRTA8d9N7k1zm6a+RG2x+FItgpu66uDQxbFurrr5OQQHR9FZnARB3PwSFqooddAStCBoqmLtS9omx+ESUXuDon94tnP+5+1JYm057GyQjZFP+l+S6G2FzlNe3WHtHc2TNI8zOlUUGLxsD1kDyR+EEQE2P/L8Jm/uk6RUc6oZaYM0JxtnpEX9AGPTtM6w7yzVEb61EaSNn4QD3j5m4QabH6hkVFLSUeqHyCeot0ib6BdNVGscPM/hWWr7S4Tw9TUvbpFUitHTnF6XrS+sL7O6VBSausT0FZonSkb+nZUFFm+z8Z5up5Btr1Lby7E5Zq4yPrMrLR263ZV52g+LvfW3iy6PXubUNVrnhqYNF3bmiZ1i1MmLnL7OxIWh4T+IMpYeRNyrRzyZjWg/ioh+aVgZu4WfXxaixbsRve5fiwb8epTo8+kZjSPFf/sHvgNC0/mbjJbxPAAAAABJRU5ErkJggg=="));
////            mailJetAttachments.add(new MailJetAttachment("text/plain", "test.txt", "VGhpcyBpcyB5b3VyIGF0dGFjaGVkIGZpbGUhISEK"));
//
//            List<MailJetRecipient> mailJetRecipients = new ArrayList<>();
//            mailJetRecipients.add(new MailJetRecipient("gg@kynsoft.com","Keimer"));
//            mailJetRecipients.add(new MailJetRecipient("niurky921116@gmail.com","Niurka"));
//            mailJetRecipients.add(new MailJetRecipient("keimermo1989@gmail.com","Niurka"));
//
//            List<MailJetVar> vars = Arrays.asList(
//                    new MailJetVar("username", "Niurka"),
//                    new MailJetVar("otp_token", "5826384")
//            );
//
//         // int  templateId =3931552;
//           int  templateId =5826460;
//            EmailRequest emailRequest = new EmailRequest(mailJetRecipients, vars, mailJetAttachments,"SubjectTest", templateId);
//         //   result = emailService.sendEmailMailjet(emailRequest);
//
//            if (result) {
//                msg = "Message sent successfully!";
//            } else {
//                 msg = "Message sent failed!";
//            }
//            return ResponseEntity.ok(new SendEmailResponse(result, msg));
//        } catch (Exception ex) {
//            return ResponseEntity.ok(new SendEmailResponse(result, msg));
//        }
    }
}
