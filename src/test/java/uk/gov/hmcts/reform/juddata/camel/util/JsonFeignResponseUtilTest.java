package uk.gov.hmcts.reform.juddata.camel.util;

import com.fasterxml.jackson.core.type.TypeReference;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.juddata.client.IdamClient;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonFeignResponseUtilTest {
    private Response responseMock; //mocked as builder has private access
    private Response.Body bodyMock; //mocked as Body is an interface in Feign.Response
    private Reader readerMock; //mocked as it is an abstract class from Java.io
    final int statusCode = 200;

    @BeforeEach
    public void setUp() throws IOException {
        responseMock = mock(Response.class);
        bodyMock = mock(Response.Body.class);
        readerMock = mock(Reader.class);

        when(responseMock.body()).thenReturn(bodyMock);
        when(responseMock.body().asReader(Charset.defaultCharset())).thenReturn(readerMock);
        when(responseMock.status()).thenReturn(statusCode);
    }

    @Test
    void testToResponseEntityThrowError() throws IOException {
        when(bodyMock.asReader(Charset.defaultCharset())).thenThrow(IOException.class);
        assertThrows(Exception.class, () ->  JsonFeignResponseUtil.toResponseEntity(this.responseMock,
                new TypeReference<List<IdamClient.User>>() {}));
    }

}