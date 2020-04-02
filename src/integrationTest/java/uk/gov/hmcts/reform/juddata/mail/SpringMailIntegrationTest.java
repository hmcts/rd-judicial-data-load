//package uk.gov.hmcts.reform.juddata.mail;
//
//import org.apache.camel.BeanInject;
//import org.apache.camel.test.spring.CamelTestContextBootstrapper;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import uk.gov.hmcts.reform.juddata.camel.email.EmailService;
//import uk.gov.hmcts.reform.juddata.camel.email.Mail;
//import uk.gov.hmcts.reform.juddata.config.CamelConfig;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@ContextConfiguration(classes = {CamelConfig.class, CamelTestContextBootstrapper.class, JavaMailSender.class}, initializers = ConfigFileApplicationContextInitializer.class)
//@RunWith(SpringRunner.class)
//public class SpringMailIntegrationTest {
//
//    @Autowired
//     EmailService emailService;
//
//
//    @Rule
//    public SmtpServerRule smtpServerRule = new SmtpServerRule(587);
//
//    @Test
//    public void shouldSendSingleMail() throws MessagingException, IOException {
//        Mail mail = new Mail();
//        mail.setFrom("no-reply@memorynotfound.com");
//        mail.setTo("info@memorynotfound.com");
//        mail.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
//        mail.setContent("We show how to write Integration Tests using Spring and GreenMail.");
//
//        emailService.sendSimpleMessage(mail);
//
//        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
//        assertEquals(1, receivedMessages.length);
//
//        MimeMessage current = receivedMessages[0];
//
//        assertEquals(mail.getSubject(), current.getSubject());
//        assertEquals(mail.getTo(), current.getAllRecipients()[0].toString());
//        assertTrue(String.valueOf(current.getContent()).contains(mail.getContent()));
//
//    }
//
//}