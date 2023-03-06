# Use an existing image as the base image
FROM openjdk:11-jdk

# Set the working directory
WORKDIR /app

# Copy the compiled JAR files to the image
COPY word-count-driver/target/word-count-driver-0.0.1-SNAPSHOT.jar /app/word-count-driver.jar
COPY word-count-spark-job/target/word-count-spark-job-1.0-SNAPSHOT.jar /app/word-count-spark-job.jar

# Set the entrypoint command to run the JAR file
ENTRYPOINT ["java", "-jar", "word-count-driver.jar"]