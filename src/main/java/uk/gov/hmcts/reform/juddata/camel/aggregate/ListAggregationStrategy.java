package uk.gov.hmcts.reform.juddata.camel.aggregate;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

@SuppressWarnings("unchecked")
public class ListAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange,  Exchange newExchange) {
        final Object value = newExchange.getIn().getBody();
        ArrayList list = null;
        if (oldExchange == null) {
            newExchange.getIn().setBody(Lists.newArrayList(value));
            oldExchange = newExchange;
        } else {
            Message in = oldExchange.getIn();
            list = in.getBody(ArrayList.class);
            list.add(value);
        }
        return oldExchange;
    }
}


