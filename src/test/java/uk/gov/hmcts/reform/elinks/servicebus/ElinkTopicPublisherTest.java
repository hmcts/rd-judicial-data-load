package uk.gov.hmcts.reform.elinks.servicebus;

import com.azure.messaging.servicebus.ServiceBusMessageBatch;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.ServiceBusTransactionContext;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.juddata.camel.util.JrdConstants.JOB_ID;

@ExtendWith(MockitoExtension.class)
class ElinkTopicPublisherTest {

    private final ServiceBusSenderClient serviceBusSenderClient = mock(ServiceBusSenderClient.class);

    private final ServiceBusTransactionContext transactionContext = mock(ServiceBusTransactionContext.class);

    private final ServiceBusMessageBatch messageBatch = mock(ServiceBusMessageBatch.class);

    List<String> sidamIdsList = new ArrayList<>();

    @InjectMocks
    ElinkTopicPublisher elinkTopicPublisher;

    CamelContext camelContext = mock(CamelContext.class);

    @BeforeEach
    public void beforeTest() {
        Map<String, String> options = new HashMap<>();
        options.put(JOB_ID, "1");
        for (int i = 0; i < 5; i++) {
            sidamIdsList.add(UUID.randomUUID().toString());
        }
        elinkTopicPublisher.jrdMessageBatchSize = 2;
        elinkTopicPublisher.loggingComponentName = "loggingComponent";
        elinkTopicPublisher.topic = "dummyTopic";
        when(camelContext.getGlobalOptions()).thenReturn(options);
    }

    @Test
    @DisplayName("Postive scenario for sending message to Azure Sevice Bus")
    void should_send_message_to_Asb() {
        doReturn(true).when(messageBatch).tryAddMessage(any());
        doReturn(1).when(messageBatch).getCount();
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        when(messageBatch.getCount()).thenReturn(1);
        elinkTopicPublisher.sendMessage(sidamIdsList, "1");
        verify(messageBatch, times(3)).tryAddMessage(any());
        verify(messageBatch).getCount();
        verify(serviceBusSenderClient).sendMessages((ServiceBusMessageBatch) any(), any());
        verify(serviceBusSenderClient, times(1)).commitTransaction(any());
    }

    @Test
    @DisplayName("Throw Exception and Rollback Transaction for connection issue while sending message to Azure "
            + "Sevice Bus")
    void should_throwException_and_rollbackTranscation_forConnectionIssues() {
        doReturn(transactionContext).when(serviceBusSenderClient).createTransaction();
        doThrow(new RuntimeException("Some Exception")).when(serviceBusSenderClient).createMessageBatch();
        assertThrows(Exception.class, () -> elinkTopicPublisher.sendMessage(sidamIdsList, "1"));
        verify(serviceBusSenderClient, times(1)).rollbackTransaction(any());
    }


    @Test
    @DisplayName("Throw NullPointerException when passing transactioncontext as NULL")
    void should_throw_NullpointerException_when_transactionContext_is_null() {
        doReturn(null).when(serviceBusSenderClient).createTransaction();
        doThrow(new RuntimeException("NullpointerException")).when(serviceBusSenderClient).createMessageBatch();
        assertThrows(Exception.class, () -> elinkTopicPublisher.sendMessage(sidamIdsList, "1"));

    }

    @Test
    @DisplayName("Throw NullPointerException when passing serviceBusClient as NULL")
    void should_throw_NullpointerException_when_serviceBusClient_is_null() {
        when(serviceBusSenderClient.createMessageBatch()).thenReturn(null);
        doThrow(new RuntimeException("NullpointerException")).when(serviceBusSenderClient).createMessageBatch();
        assertThrows(Exception.class, () -> elinkTopicPublisher.sendMessage(sidamIdsList, "1"));
        verify(serviceBusSenderClient, never()).rollbackTransaction(any());

    }

    @Test
    @DisplayName("Large message not able to add in message batch")
    void not_able_to_add_large_message_in_message_bus() {
        doReturn(false).when(messageBatch).tryAddMessage(any());
        doReturn(1).when(messageBatch).getCount();
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        when(messageBatch.getCount()).thenReturn(1);
        elinkTopicPublisher.sendMessage(sidamIdsList, "1");
        verify(serviceBusSenderClient, times(4))
                .sendMessages((ServiceBusMessageBatch) any(), any());
    }

    @Test
    @DisplayName("Not able to send messsage when message batch is less than zero")
    void not_able_to_send_message_when_message_batch_count_is_less_than_zero() {
        doReturn(false).when(messageBatch).tryAddMessage(any());
        doReturn(0).when(messageBatch).getCount();
        doReturn(messageBatch).when(serviceBusSenderClient).createMessageBatch();
        when(messageBatch.getCount()).thenReturn(0);
        elinkTopicPublisher.sendMessage(sidamIdsList, "1");
        verify(serviceBusSenderClient, never())
                .sendMessages((ServiceBusMessageBatch) any(), any());
    }
}
