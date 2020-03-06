package uk.gov.hmcts.reform.juddata.camel.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

public class ArchiveAzureFileProcessorTest {

    ArchiveAzureFileProcessor azureFileProcessor = new ArchiveAzureFileProcessor();

    private final CamelContext camelContext = new DefaultCamelContext();

    private final Exchange exchange = new DefaultExchange(camelContext);

    @Test
    public void test_process() throws Exception {
        List<String> archivalFileNames = new ArrayList<>();
        File file = new File("src/test/resources/sourceFiles/test.csv");
        String absolutePath = file.getAbsolutePath();
        String folder = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        String fileName = absolutePath.substring(absolutePath.lastIndexOf("/")).replaceFirst("/", "");
        archivalFileNames.add(folder + "?fileName=" + fileName + "&noop=true");
        FieldSetter.setField(azureFileProcessor, azureFileProcessor
                .getClass().getDeclaredField("archivalFileNames"), archivalFileNames);
        FieldSetter.setField(azureFileProcessor, azureFileProcessor
                .getClass().getDeclaredField("activeBlobs"), "file:");
        FieldSetter.setField(azureFileProcessor, azureFileProcessor
                .getClass().getDeclaredField("archivalCred"), "");
        FieldSetter.setField(azureFileProcessor,azureFileProcessor
                .getClass().getDeclaredField("archivalDateFormat"), "dd-MM-yyyy--HH-mm");
        FieldSetter.setField(azureFileProcessor,azureFileProcessor
                .getClass().getDeclaredField("fileReadTimeOut"), 1000);
        exchange.setProperty("CamelLoopIndex", 0);
        azureFileProcessor.process(exchange);
        Assert.assertNotNull(exchange.getIn().getHeader("fileName"));
    }
}
