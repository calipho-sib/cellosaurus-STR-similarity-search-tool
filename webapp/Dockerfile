# Use maven image for builder stage only
FROM maven:3.9.11-eclipse-temurin-17-noble as builder

# Change the working directory to app
WORKDIR /app/

# Copy the backend files needed for the build
COPY ./backend/ ./

# Use maven to install the backend
# thibault: 
RUN mvn install -DskipTests=true && rm -rf *

# Copy the webapp files needed for the build
COPY ./webapp/ ./

# pam: copy a local version of the cellosaurus in workdir for faster debuggging
# COPY ./data/ ./

# Use maven to compile the war archive
RUN mvn compile war:war

# Use tomcat image as base
FROM tomcat:11.0.11-jdk25-temurin-noble

# Remove default tomcat apps
RUN rm -rf ./webapps/*

# Copy the war archive from the builder stage into the tomcat webapps folder
COPY --from=builder /app/target/str-search.war ./webapps/str-search.war

# pam: copy the local version of cellosaurus in base directory for faster debugging
# COPY --from=builder /app/cellosaurus.xml ./cellosaurus.xml

# Expose the default tomcat port
EXPOSE 8080

# Run catalina
CMD ["catalina.sh", "run"]
