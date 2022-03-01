
package uk.gov.hmcts.reform.juddata.camel.servicebus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusMessageBatch;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.ServiceBusTransactionContext;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;

@ExtendWith(MockitoExtension.class)
class TopicPublisherTest {


    private final ServiceBusSenderClient serviceBusSenderClient = mock(ServiceBusSenderClient.class);

    private final ServiceBusTransactionContext transactionContext = mock(ServiceBusTransactionContext.class);

    private final ServiceBusMessageBatch messageBatch = mock(ServiceBusMessageBatch.class);

    @InjectMocks
    private TopicPublisher topicPublisher;

    List<String> sidamIdsList = new ArrayList<>();

    List<ServiceBusMessage> serviceBusMessageList = new ArrayList<>();

    CamelContext camelContext = mock(CamelContext.class);

    @BeforeEach
    public void beforeTest() {
        Map<String, String> options = new HashMap<>();
        options.put(JOB_ID, "1");
        for (int i = 0; i < 5; i++) {
            sidamIdsList.add(UUID.randomUUID().toString());
        }
        topicPublisher.jrdMessageBatchSize = 2;
        topicPublisher.loggingComponentName = "loggingComponent";
        topicPublisher.topic = "dummyTopic";
        when(camelContext.getGlobalOptions()).thenReturn(options);
    }

    @Test
    void sendMessageCallsAzureSendMessage() {
        doReturn(true).when(messageBatch).tryAddMessage(any());
        doReturn(1).when(messageBatch).getCount();
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        when(messageBatch.getCount()).thenReturn(1);
        topicPublisher.sendMessage(sidamIdsList, "1");
        verify(messageBatch, times(3)).tryAddMessage(any());
        verify(messageBatch).getCount();
        verify(serviceBusSenderClient).sendMessages((ServiceBusMessageBatch) any(), any());
        verify(serviceBusSenderClient, times(1)).commitTransaction(any());
    }

    @Test
    void shouldThrowExceptionForConnectionIssues() {
        doReturn(transactionContext).when(serviceBusSenderClient).createTransaction();
        doThrow(new RuntimeException("Some Exception")).when(serviceBusSenderClient).createMessageBatch();
        assertThrows(Exception.class, () -> topicPublisher.sendMessage(sidamIdsList, "1"));
        verify(serviceBusSenderClient, times(1)).rollbackTransaction(any());
    }

    @Test
    void sendMessageNotAbleToAdd() {
        doReturn(false).when(messageBatch).tryAddMessage(any());
        doReturn(1).when(messageBatch).getCount();
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        when(messageBatch.getCount()).thenReturn(1);
        topicPublisher.sendMessage(sidamIdsList, "1");
        verify(serviceBusSenderClient, times(4))
            .sendMessages((ServiceBusMessageBatch) any(), any());
    }


    @Test
    void sendLargeMessageCallsAzureSendMessage() {

        doReturn(1).when(messageBatch).getCount();
        doReturn(false).when(messageBatch).tryAddMessage(any());
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        topicPublisher.sendMessage(sidamIdsList, "1");
        verify(messageBatch, times(6)).tryAddMessage(any());
        verify(serviceBusSenderClient, times(1)).commitTransaction(any());
    }
}


