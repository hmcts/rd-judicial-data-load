# rd-judicial-data-load
Judicial reference data load (JRD)

JRD is batch application and JRD batch is scheduled with Kubernetes which runs once in a day per cluster.

JRD consume data files from an external source, transform that data into the destination format 
and load the data into JRD database. 

JRD Uses Data-Ingestion-Library (https://github.com/hmcts/data-ingestion-lib) and configure camel routes with
data-ingestion-lib & JRD custom configuration files & crates camel integration framework which reads CSV files & Stores
the CSV files data in JRD Database. 


Library should be included with build.gradle like follows
compile group: 'uk.gov.hmcts.reform', name: 'data-ingestion-lib', version: '0.5.2.4'
And release versions library can be found in bintray (https://bintray.com/hmcts/hmcts-maven/data-ingestion-lib)  

For More Information Please see the confluence.
https://tools.hmcts.net/confluence/display/RTRD/Judicial+Reference+Data

# Consumption of files from a SFTP server
The files received from SFTP server are encrypted using GPG encryption (which complies with OpenPGP standards).

An internal SFTP server (behind a F5 Load balancer) will poll the files at periodic intervals from the  external SFTP server. It will forward the files onto the untrusted network that Palo Alto is listening on.

The Palo Alto untrusted interfaces will form the Palo Alto backend pool, used by requests matched by the path-based rule.

The files are decrypted and then scanned and if everything is okay then trusted traffic is sent to a configured endpoint, in this case an Azure Blob Storage account.

# Data Transformation and Load - This is achieved through a K8S scheduler and Apache Camel.
Kubernetes scheduler triggers Apache Camel routes which process files stored in Azure blob storage and persists it JRD database.

## Plugins

The template contains the following plugins:

  * checkstyle

    https://docs.gradle.org/current/userguide/checkstyle_plugin.html

    Performs code style checks on Java source files using Checkstyle and generates reports from these checks.
    The checks are included in gradle's *check* task (you can run them by executing `./gradlew check` command).

  * pmd

    https://docs.gradle.org/current/userguide/pmd_plugin.html

    Performs static code analysis to finds common programming flaws. Included in gradle `check` task.


  * jacoco

    https://docs.gradle.org/current/userguide/jacoco_plugin.html

    Provides code coverage metrics for Java code via integration with JaCoCo.
    You can create the report by running the following command:

    ```bash
      ./gradlew jacocoTestReport
    ```

    The report will be created in build/reports subdirectory in your project directory.

  * io.spring.dependency-management

    https://github.com/spring-gradle-plugins/dependency-management-plugin

    Provides Maven-like dependency management. Allows you to declare dependency management
    using `dependency 'groupId:artifactId:version'`
    or `dependency group:'group', name:'name', version:version'`.

  * org.springframework.boot

    http://projects.spring.io/spring-boot/

    Reduces the amount of work needed to create a Spring application

  * org.owasp.dependencycheck

    https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html

    Provides monitoring of the project's dependent libraries and creating a report
    of known vulnerable components that are included in the build. To run it
    execute `gradle dependencyCheck` command.

  * com.github.ben-manes.versions

    https://github.com/ben-manes/gradle-versions-plugin

    Provides a task to determine which dependencies have updates. Usage:

    ```bash
      ./gradlew dependencyUpdates -Drevision=release
    ```


# Building and deploying the application
Building the application
The project uses Gradle as a build tool. It already contains ./gradlew wrapper script, so there's no need to install gradle.


### Environment Vars

If running locally for development or testing you will need to add (Application.yml and application-camel-routes-common.yaml) and set the following environment variables

* ACCOUNT_NAME: <The actual account name. Please check with the dev team for more information.>
* ACCOUNT_KEY: <The actual account key. Please check with the dev team for more information.>
* CONTAINER_NAME: jud-ref-data

### Running the application

Please Make sure you are connected to the VPN before running the Application.
(https://portal.platform.hmcts.net/vdesk/webtop.eui?webtop=/Common/webtop_full&webtop_type=webtop_full)


Create the image of the application by executing the following command:

```bash
  ./gradlew assemble
```

Create docker image:

```bash
  docker-compose build
```

Run the distribution (created in `build/install/rd-judicial-data-load directory)
by executing the following command:

```bash
  docker-compose up
```

After, you can start the application from the current source files using Gradle as follows:

```
./gradlew clean bootRun
```


This will start the API container exposing the application's port
(set to `8099` in this template app).

In order to test if the application is up, you can call its health endpoint:

```bash
  curl http://localhost:8094/health
```


### Alternative script to run application

To skip all the setting up and building, just execute the following command:

```bash
./bin/run-in-docker.sh
```

For more information:

```bash
./bin/run-in-docker.sh -h
```

Script includes bare minimum environment variables necessary to start api instance. Whenever any variable is changed or any other script regarding docker image/container build, the suggested way to ensure all is cleaned up properly is by this command:

```bash
docker-compose rm
```

It clears stopped containers correctly. Might consider removing clutter of images too, especially the ones fiddled with:

```bash
docker images

docker image rm <image-id>
```

To build the project execute the following command:
  ./gradlew build

The application exposes health endpoint (http://localhost:8094/health) and metrics endpoint
(http://localhost:8094/metrics).
