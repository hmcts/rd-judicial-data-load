package uk.gov.hmcts.reform.juddata.camel.servicebus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusMessageBatch;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.messaging.servicebus.ServiceBusTransactionContext;
import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.juddata.camel.util.PublishJudicialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

import static com.google.common.collect.Lists.partition;

@Slf4j
@Service
public class TopicPublisher {

    @Autowired
    private ServiceBusSenderClient serviceBusSenderClient;

    @Value("${logging-component-name}")
    String loggingComponentName;

    @Value("${jrd.publisher.jrd-message-batch-size}")
    int jrdMessageBatchSize;

    @Value("${jrd.publisher.azure.service.bus.topic}")
    String topic;

    String jobId;

    @Autowired
    CamelContext camelContext;


    public void sendMessage(@NotNull List<String> judicalIds, String jobId) {
        ServiceBusTransactionContext transactionContext = null;
        try {
            transactionContext = serviceBusSenderClient.createTransaction();
            publishMessageToTopic(judicalIds, serviceBusSenderClient, transactionContext, jobId);
        } catch (Exception exception) {
            log.error("{}:: Publishing message to service bus topic failed with exception: {}:: Job Id {}",
                loggingComponentName, exception.getMessage(), jobId);
            if (Objects.nonNull(serviceBusSenderClient) && Objects.nonNull(transactionContext)) {
                serviceBusSenderClient.rollbackTransaction(transactionContext);
            }
            throw exception;
        }
        serviceBusSenderClient.commitTransaction(transactionContext);
    }

    private void publishMessageToTopic(List<String> judicalIds,
                                       ServiceBusSenderClient serviceBusSenderClient,
                                       ServiceBusTransactionContext transactionContext,
                                       String jobId) {

        ServiceBusMessageBatch messageBatch = serviceBusSenderClient.createMessageBatch();
        List<ServiceBusMessage> serviceBusMessages = new ArrayList<>();

        partition(judicalIds, jrdMessageBatchSize)
            .forEach(data -> {
                PublishJudicialData judicialDataChunk = new PublishJudicialData();
                judicialDataChunk.setUserIds(data);
                serviceBusMessages.add(new ServiceBusMessage(new Gson().toJson(judicialDataChunk)));
            });

        for (ServiceBusMessage message : serviceBusMessages) {
            if (messageBatch.tryAddMessage(message)) {
                continue;
            }

            // The batch is full, so we create a new batch and send the batch.
            sendMessageToAsb(serviceBusSenderClient, transactionContext, messageBatch, jobId);

            // create a new batch
            messageBatch = serviceBusSenderClient.createMessageBatch();
            // Add that message that we couldn't before.
            if (!messageBatch.tryAddMessage(message)) {
                log.error("{}:: Message is too large for an empty batch. Skipping. Max size: {}. Job id::{}",
                    loggingComponentName, messageBatch.getMaxSizeInBytes(), jobId);
            }
        }
        sendMessageToAsb(serviceBusSenderClient, transactionContext, messageBatch, jobId);
    }

    private void sendMessageToAsb(ServiceBusSenderClient serviceBusSenderClient,
                                  ServiceBusTransactionContext transactionContext,
                                  ServiceBusMessageBatch messageBatch,
                                  String jobId) {
        if (messageBatch.getCount() > 0) {
            serviceBusSenderClient.sendMessages(messageBatch, transactionContext);
            log.info("{}:: Sent a batch of messages to the topic: {} ::Job id::{}", loggingComponentName, topic, jobId);
        }
    }
}

