package uk.gov.hmcts.reform.juddata.camel.vo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class RouteProperties {

    String routeName;

    String childNames;

    String sql;

    String truncateSql;

    String blobPath;

    String processor;

    String mapper;

    String binder;
}
