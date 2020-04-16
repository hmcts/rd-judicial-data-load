package uk.gov.hmcts.reform.juddata.camel.processor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.MappingConstants.LEAF_ROUTE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

public class ArchiveAzureFileProcessorTest {

    ArchiveAzureFileProcessor azureFileProcessor = new ArchiveAzureFileProcessor();

    private final CamelContext camelContextMock = mock(CamelContext.class);

    private final Exchange exchangeMock = mock(Exchange.class);

    private final Message messageMock = mock(Message.class);

    private final Endpoint endpointMock = mock(Endpoint.class);

    private final ConsumerTemplate consumerTemplateMock = mock(ConsumerTemplate.class);

    @Test
    public void test_process() throws Exception {

        List<String> archivalFileNames = new ArrayList<>();
        File file = new File("src/test/resources/sourceFiles/test.csv");
        String absolutePath = file.getAbsolutePath();
        String folder = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        String fileName = absolutePath.substring(absolutePath.lastIndexOf("/")).replaceFirst("/", "");
        archivalFileNames.add(folder + "?fileName=" + fileName + "&noop=true");

        when(exchangeMock.getFromEndpoint()).thenReturn(endpointMock);
        when(endpointMock.getEndpointUri()).thenReturn("direct://leaf");
        when(exchangeMock.getProperty("CamelLoopIndex",Integer.class)).thenReturn(0);
        when(exchangeMock.getIn()).thenReturn(messageMock);
        when(exchangeMock.getContext()).thenReturn(camelContextMock);
        when(camelContextMock.createConsumerTemplate()).thenReturn(consumerTemplateMock);
        when(exchangeMock.getMessage()).thenReturn(messageMock);
        when(consumerTemplateMock.receiveBody(any(String.class), any(Long.class))).thenReturn(file);
        when(messageMock.getHeader(LEAF_ROUTE)).thenReturn(LEAF_ROUTE);

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
        FieldSetter.setField(azureFileProcessor, azureFileProcessor
                .getClass().getDeclaredField("leafRouteName"), "direct:leaf");
        FieldSetter.setField(azureFileProcessor, azureFileProcessor
                .getClass().getDeclaredField("leafArchivalFileNames"), archivalFileNames);

        azureFileProcessor.process(exchangeMock);
        Assert.assertNotNull(exchangeMock);
    }
}
