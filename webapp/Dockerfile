# Use maven image for builder stage only
FROM maven:3-jdk-8-slim as builder

# Change the working directory to app
WORKDIR /app/

# Copy the backend files needed for the build
COPY ./backend/ ./

# Use maven to install the backend
RUN mvn install -DskipTests=true \
 && rm -rf *

# Copy the webapp files needed for the build
COPY ./webapp/ ./

# Use maven to compile the war archive
RUN mvn compile war:war

# Use tomcat imagae as base
FROM tomcat:9-jdk8-openjdk-slim

# Remove default tomcat apps
RUN rm -rf ./webapps/*

# Copy the war archive from the builder stage into the tomcat webapps folder
COPY --from=builder /app/target/str-search.war ./webapps/str-search.war

# Expose the default tomcat port
EXPOSE 8080

# Run catalina
CMD ["catalina.sh", "run"]
