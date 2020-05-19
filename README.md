# rd-judicial-data-load
Judicial reference data load (JRD)

JRD is batch application and JRD batch is scheduled with kuberenetes which runs once in day per cluster.

# Building and deploying the application
Building the application
The project uses Gradle as a build tool. It already contains ./gradlew wrapper script, so there's no need to install gradle.

To build the project execute the following command:

  ./gradlew build 
