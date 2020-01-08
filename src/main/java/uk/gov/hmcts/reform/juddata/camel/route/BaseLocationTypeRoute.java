package uk.gov.hmcts.reform.juddata.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.juddata.camel.beans.BaseLocationType;
import uk.gov.hmcts.reform.juddata.camel.mapper.BaseLocationRowTypeMapper;
import uk.gov.hmcts.reform.juddata.camel.processor.BaseLocationRecordProcessor;

@Component
public class BaseLocationTypeRoute extends RouteBuilder {

    @Autowired
    BaseLocationRowTypeMapper baseLocationRowTypeMapper;

    @Override
    public void configure() throws Exception {

       /* from("azure-blob://rddemo/jrdtest/BaseLocations.csv?credentials=#credsreg&operation=updateBlockBlob")
                .id("location-route")
                .to("file://blobdirectory1?noop=true&fileExist=Override").end();

        from("file://blobdirectory1?noop=true&fileExist=Override")
                .unmarshal() .bindy(BindyType.Csv, BaseLocationType.class)
                .process(new BaseLocationRecordProcessor())
                .split().body()
                .bean(baseLocationRowTypeMapper , "getMap")
                .to("sql:insert into base_location_type (base_location_id,court_name,court_type,circuit,area_of_expertise) values(:#base_location_id,:#court_name, :#court_type,:#circuit, :#area_of_expertise)?dataSource=dataSource")
                .to("log:test?showAll=true")
                .end();*/

    }

}
