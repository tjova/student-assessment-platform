package pmf.master.platforma.camunda.service;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pmf.master.platforma.main.service.TaskAsyncService;

@Component
@ExternalTaskSubscription("send-student-email")
public class EmailWorker implements ExternalTaskHandler {

    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(TaskAsyncService.class);

    public EmailWorker(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void execute(org.camunda.bpm.client.task.ExternalTask externalTask, org.camunda.bpm.client.task.ExternalTaskService externalTaskService) {
        try {
            String recipient = externalTask.getVariable("email");

            Number pointsNum = externalTask.getVariable("points");
            long points = (pointsNum != null) ? pointsNum.longValue() : 0L;

            String comment = externalTask.getVariable("comment");

            logger.info(String.format("External Task: Sending email to %s with %s points" , recipient, points));

            // 2. Email Logic
            if (recipient != null && !recipient.isEmpty()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(recipient);
                message.setSubject("Grading Result: " + (points >= 50 ? "Passed" : "Failed"));
                message.setText("Hello,\n\nYou scored " + points + " points.\nComment: " + comment);

                mailSender.send(message);

                // 3. Complete the task in camunda
                externalTaskService.complete(externalTask);
                logger.info("External Task: Email sent and task completed!");
            } else {
                logger.error("External Task: No recipient email found!");
                externalTaskService.handleFailure(externalTask, "No email", "Email variable was null", 0, 0);
            }

        } catch (Exception e) {
            logger.error("An error occurred while trying to send email");
            externalTaskService.handleFailure(externalTask, "Email Service Error", e.getMessage(), 0, 0);
        }
    }
}