@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  rd-judicial-data-load startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and RD_JUDICIAL_DATA_LOAD_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dfile.encoding=UTF-8"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\rd-judicial-data-load-0.0.1.jar;%APP_HOME%\lib\properties-volume-spring-boot-starter-0.0.4.jar;%APP_HOME%\lib\health-spring-boot-starter-0.0.4.jar;%APP_HOME%\lib\spring-boot-starter-web-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-validation-2.1.4.RELEASE.jar;%APP_HOME%\lib\hibernate-validator-6.0.16.Final.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\spring-boot-starter-data-jpa-2.1.4.RELEASE.jar;%APP_HOME%\lib\service-auth-provider-client-3.0.0.jar;%APP_HOME%\lib\spring-boot-starter-actuator-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-cloud-starter-netflix-hystrix-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-cloud-netflix-hystrix-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-cloud-starter-openfeign-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-cloud-openfeign-core-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-aop-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-json-2.1.4.RELEASE.jar;%APP_HOME%\lib\auth-checker-lib-2.1.3.jar;%APP_HOME%\lib\spring-boot-starter-security-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-jdbc-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-retry-1.2.1.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-cache-2.1.4.RELEASE.jar;%APP_HOME%\lib\caffeine-2.5.6.jar;%APP_HOME%\lib\spring-boot-actuator-autoconfigure-2.1.4.RELEASE.jar;%APP_HOME%\lib\azure-storage-blob-10.0.1-Preview.jar;%APP_HOME%\lib\client-runtime-2.0.0-beta3.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.8.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.8.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.9.8.jar;%APP_HOME%\lib\java-logging-spring-4.0.1.jar;%APP_HOME%\lib\java-logging-httpcomponents-4.0.1.jar;%APP_HOME%\lib\java-logging-appinsights-4.0.1.jar;%APP_HOME%\lib\java-logging-4.0.1.jar;%APP_HOME%\lib\logstash-logback-encoder-5.3.jar;%APP_HOME%\lib\feign-jackson-10.2.0.jar;%APP_HOME%\lib\java-jwt-3.4.0.jar;%APP_HOME%\lib\jjwt-0.7.0.jar;%APP_HOME%\lib\spring-cloud-starter-netflix-archaius-2.1.1.RELEASE.jar;%APP_HOME%\lib\hystrix-metrics-event-stream-1.5.18.jar;%APP_HOME%\lib\hystrix-javanica-1.5.18.jar;%APP_HOME%\lib\hystrix-serialization-1.5.18.jar;%APP_HOME%\lib\feign-hystrix-10.1.0.jar;%APP_HOME%\lib\hystrix-core-1.5.18.jar;%APP_HOME%\lib\archaius-core-0.7.6.jar;%APP_HOME%\lib\jackson-module-afterburner-2.9.8.jar;%APP_HOME%\lib\jackson-dataformat-xml-2.9.8.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.9.8.jar;%APP_HOME%\lib\jackson-databind-2.9.9.3.jar;%APP_HOME%\lib\feign-form-spring-3.8.0.jar;%APP_HOME%\lib\feign-form-3.8.0.jar;%APP_HOME%\lib\rest-assured-3.0.7.jar;%APP_HOME%\lib\springfox-swagger2-2.9.2.jar;%APP_HOME%\lib\springfox-swagger-ui-2.9.2.jar;%APP_HOME%\lib\bouncy-gpg-2.0.1.jar;%APP_HOME%\lib\bcpg-jdk15on-1.56.jar;%APP_HOME%\lib\spring-cloud-starter-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-security-rsa-1.0.7.RELEASE.jar;%APP_HOME%\lib\bcpkix-jdk15on-1.60.jar;%APP_HOME%\lib\bcprov-jdk15on-1.64.jar;%APP_HOME%\lib\flyway-core-5.2.4.jar;%APP_HOME%\lib\postgresql-42.2.5.jar;%APP_HOME%\lib\springfox-swagger-common-2.9.2.jar;%APP_HOME%\lib\springfox-spring-web-2.9.2.jar;%APP_HOME%\lib\springfox-schema-2.9.2.jar;%APP_HOME%\lib\springfox-spi-2.9.2.jar;%APP_HOME%\lib\springfox-core-2.9.2.jar;%APP_HOME%\lib\camel-azure-starter-2.24.2.jar;%APP_HOME%\lib\camel-azure-2.24.2.jar;%APP_HOME%\lib\azure-storage-8.0.0.jar;%APP_HOME%\lib\azure-keyvault-core-1.0.0.jar;%APP_HOME%\lib\guava-27.0-jre.jar;%APP_HOME%\lib\camel-csv-starter-2.24.2.jar;%APP_HOME%\lib\camel-bindy-starter-2.24.2.jar;%APP_HOME%\lib\camel-jdbc-starter-2.24.2.jar;%APP_HOME%\lib\camel-spring-boot-starter-2.24.2.jar;%APP_HOME%\lib\camel-csv-2.24.2.jar;%APP_HOME%\lib\camel-sql-2.24.2.jar;%APP_HOME%\lib\camel-quartz-2.13.0.jar;%APP_HOME%\lib\camel-restlet-2.24.2.jar;%APP_HOME%\lib\camel-core-starter-2.24.2.jar;%APP_HOME%\lib\camel-bindy-2.24.2.jar;%APP_HOME%\lib\camel-jdbc-2.24.2.jar;%APP_HOME%\lib\camel-http-common-2.24.2.jar;%APP_HOME%\lib\camel-spring-boot-2.24.2.jar;%APP_HOME%\lib\camel-spring-2.24.2.jar;%APP_HOME%\lib\camel-core-2.24.2.jar;%APP_HOME%\lib\javax.transaction-api-1.3.jar;%APP_HOME%\lib\xml-path-3.1.1.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\hibernate-core-5.3.9.Final.jar;%APP_HOME%\lib\spring-data-jpa-2.1.6.RELEASE.jar;%APP_HOME%\lib\spring-aspects-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-2.1.4.RELEASE.jar;%APP_HOME%\lib\micrometer-core-1.1.4.jar;%APP_HOME%\lib\spring-security-config-5.1.5.RELEASE.jar;%APP_HOME%\lib\spring-security-web-5.1.5.RELEASE.jar;%APP_HOME%\lib\spring-webmvc-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-plugin-metadata-1.2.0.RELEASE.jar;%APP_HOME%\lib\spring-plugin-core-1.2.0.RELEASE.jar;%APP_HOME%\lib\spring-context-support-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-actuator-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-boot-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-security-core-5.1.5.RELEASE.jar;%APP_HOME%\lib\spring-context-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.1.6.RELEASE.jar;%APP_HOME%\lib\aspectjweaver-1.9.2.jar;%APP_HOME%\lib\spring-web-5.1.6.RELEASE.jar;%APP_HOME%\lib\HikariCP-3.2.0.jar;%APP_HOME%\lib\spring-orm-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-2.1.4.RELEASE.jar;%APP_HOME%\lib\spring-cloud-netflix-ribbon-2.1.1.RELEASE.jar;%APP_HOME%\lib\rxjava-reactive-streams-1.2.1.jar;%APP_HOME%\lib\spring-data-commons-2.1.6.RELEASE.jar;%APP_HOME%\lib\spring-tx-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-expression-5.1.6.RELEASE.jar;%APP_HOME%\lib\spring-core-5.1.6.RELEASE.jar;%APP_HOME%\lib\swagger-models-1.5.20.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.1.4.RELEASE.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.26.jar;%APP_HOME%\lib\applicationinsights-logging-logback-2.1.2.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\quartz-2.3.1.jar;%APP_HOME%\lib\feign-slf4j-10.1.0.jar;%APP_HOME%\lib\HikariCP-java7-2.4.13.jar;%APP_HOME%\lib\log4j-to-slf4j-2.11.2.jar;%APP_HOME%\lib\slf4j-api-1.7.26.jar;%APP_HOME%\lib\commons-fileupload-1.4.jar;%APP_HOME%\lib\groovy-xml-2.5.6.jar;%APP_HOME%\lib\json-path-3.1.1.jar;%APP_HOME%\lib\groovy-json-2.5.6.jar;%APP_HOME%\lib\rest-assured-common-3.1.1.jar;%APP_HOME%\lib\groovy-2.5.6.jar;%APP_HOME%\lib\org.restlet.ext.httpclient-2.3.12.jar;%APP_HOME%\lib\httpmime-4.5.8.jar;%APP_HOME%\lib\googleauth-1.1.5.jar;%APP_HOME%\lib\httpclient-4.5.8.jar;%APP_HOME%\lib\hamcrest-library-1.3.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\tagsoup-1.2.1.jar;%APP_HOME%\lib\swagger-annotations-1.5.20.jar;%APP_HOME%\lib\classmate-1.4.0.jar;%APP_HOME%\lib\mapstruct-1.2.0.Final.jar;%APP_HOME%\lib\applicationinsights-web-2.1.2.jar;%APP_HOME%\lib\spring-cloud-context-2.1.1.RELEASE.jar;%APP_HOME%\lib\lombok-1.18.6.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\failureaccess-1.0.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-qual-2.5.2.jar;%APP_HOME%\lib\error_prone_annotations-2.2.0.jar;%APP_HOME%\lib\j2objc-annotations-1.1.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.17.jar;%APP_HOME%\lib\commons-csv-1.6.jar;%APP_HOME%\lib\javax.servlet-api-4.0.1.jar;%APP_HOME%\lib\org.restlet-2.3.12.jar;%APP_HOME%\lib\jaxb-core-2.3.0.jar;%APP_HOME%\lib\jaxb-impl-2.3.0.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\hibernate-commons-annotations-5.0.4.Final.jar;%APP_HOME%\lib\jboss-logging-3.3.2.Final.jar;%APP_HOME%\lib\javax.persistence-api-2.2.jar;%APP_HOME%\lib\javassist-3.23.1-GA.jar;%APP_HOME%\lib\byte-buddy-1.9.12.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jandex-2.0.5.Final.jar;%APP_HOME%\lib\dom4j-2.1.1.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\snakeyaml-1.23.jar;%APP_HOME%\lib\HdrHistogram-2.1.9.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\tomcat-embed-websocket-9.0.17.jar;%APP_HOME%\lib\tomcat-embed-core-9.0.17.jar;%APP_HOME%\lib\tomcat-embed-el-9.0.17.jar;%APP_HOME%\lib\spring-cloud-commons-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-cloud-netflix-archaius-2.1.1.RELEASE.jar;%APP_HOME%\lib\commons-configuration-1.8.jar;%APP_HOME%\lib\rxjava-1.3.8.jar;%APP_HOME%\lib\commons-lang3-3.8.1.jar;%APP_HOME%\lib\asm-5.0.4.jar;%APP_HOME%\lib\rxjava-2.2.8.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\spring-jcl-5.1.6.RELEASE.jar;%APP_HOME%\lib\commons-io-2.2.jar;%APP_HOME%\lib\httpcore-4.4.11.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\applicationinsights-core-2.1.2.jar;%APP_HOME%\lib\feign-core-10.2.0.jar;%APP_HOME%\lib\netty-codec-http-4.1.34.Final.jar;%APP_HOME%\lib\netty-handler-4.1.34.Final.jar;%APP_HOME%\lib\netty-codec-4.1.34.Final.jar;%APP_HOME%\lib\netty-transport-4.1.34.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.34.Final.jar;%APP_HOME%\lib\azure-annotations-1.2.0.jar;%APP_HOME%\lib\mchange-commons-java-0.2.15.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\apache-mime4j-core-0.7.2.jar;%APP_HOME%\lib\spring-security-crypto-5.1.5.RELEASE.jar;%APP_HOME%\lib\commons-lang-2.6.jar;%APP_HOME%\lib\netty-resolver-4.1.34.Final.jar;%APP_HOME%\lib\netty-common-4.1.34.Final.jar;%APP_HOME%\lib\woodstox-core-5.0.3.jar;%APP_HOME%\lib\stax2-api-3.1.4.jar;%APP_HOME%\lib\log4j-api-2.11.2.jar

@rem Execute rd-judicial-data-load
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %RD_JUDICIAL_DATA_LOAD_OPTS%  -classpath "%CLASSPATH%" uk.gov.hmcts.reform.juddata.JudicialApplication %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable RD_JUDICIAL_DATA_LOAD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%RD_JUDICIAL_DATA_LOAD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
