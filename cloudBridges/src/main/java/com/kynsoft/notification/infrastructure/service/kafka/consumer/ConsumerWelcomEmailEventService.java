package com.kynsoft.notification.infrastructure.service.kafka.consumer;

import com.kynsof.share.core.domain.kafka.entity.UserWelcomKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.notification.application.command.sendMailjetEmail.SendMailJetEMailCommand;
import com.kynsof.share.core.application.mailjet.MailJetRecipient;
import com.kynsof.share.core.application.mailjet.MailJetVar;
import com.kynsoft.notification.domain.dto.MailjetTemplateEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerWelcomEmailEventService {

    @Value("${mail.template.welcome}")
    private int welcomeTemplate;

    private final IMediator mediator;

    public ConsumerWelcomEmailEventService(IMediator mediator) {

        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-welcom-email", groupId = "notification-welcom")
    public void listen(UserWelcomKafka otpKafka) {
        try {
            List<MailJetRecipient> mailJetRecipients = new ArrayList<>();
            mailJetRecipients.add(new MailJetRecipient(otpKafka.getEmail(),otpKafka.getFullName()));

            SendMailJetEMailCommand command = getSendMailJetEMailCommand(otpKafka, mailJetRecipients, this.welcomeTemplate);
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerWelcomEmailEventService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static @NotNull SendMailJetEMailCommand getSendMailJetEMailCommand(UserWelcomKafka otpKafka, List<MailJetRecipient> mailJetRecipients, int template) {
        List<MailJetVar> vars = Arrays.asList(
                new MailJetVar("user_name", otpKafka.getUserName()),
                new MailJetVar("name", otpKafka.getFullName()),
                new MailJetVar("temp_password", otpKafka.getPassword()),
                new MailJetVar("temp_email", otpKafka.getEmail())
        );

        return new SendMailJetEMailCommand(mailJetRecipients, vars, new ArrayList<>(),
                "Correo de Bienvenida",template);
    }

}
