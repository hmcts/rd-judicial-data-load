//package uk.gov.hmcts.reform.juddata.mail;
//
//import com.icegreen.greenmail.util.GreenMail;
//import com.icegreen.greenmail.util.ServerSetup;
//import org.junit.rules.ExternalResource;
//import javax.mail.internet.MimeMessage;
//
//public class SmtpServerRule extends ExternalResource {
//
//    private GreenMail smtpServer;
//    private int port;
//
//    public SmtpServerRule(int port) {
//        this.port = port;
//    }
//
//    @Override
//    protected void before() throws Throwable {
//        super.before();
//        smtpServer = new GreenMail(new ServerSetup(port, "smtp.gmail.com", "smtp"));
//        smtpServer.start();
//    }
//
//    public MimeMessage[] getMessages() {
//        return smtpServer.getReceivedMessages();
//    }
//
//    @Override
//    protected void after() {
//        super.after();
//        smtpServer.stop();
//    }
//}
