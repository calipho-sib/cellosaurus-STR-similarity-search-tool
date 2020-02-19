# Use maven image for builder stage only
FROM maven:3-jdk-8-alpine

# Change the working directory to app
WORKDIR /app/

# Install R and required packages
RUN apk add font-noto libc-dev g++ R R-dev \
 && Rscript -e 'install.packages("ggplot2", repos="https://cran.rstudio.com")' \
 && Rscript -e 'install.packages("dplyr", repos="https://cran.rstudio.com")' \
 && Rscript -e 'install.packages("reshape2", repos="https://cran.rstudio.com")' \
 && Rscript -e 'install.packages("grid", repos="https://cran.rstudio.com")' \
 && Rscript -e 'install.packages("gridExtra", repos="https://cran.rstudio.com")' \
 && Rscript -e 'install.packages("forcats", repos="https://cran.rstudio.com")'

# Copy the backend files needed for the build
COPY ./backend/ ./

# Use maven to install the backend
RUN mvn install -DskipTests=true \
 && rm -rf *

# Copy the webapp files needed for the build
COPY ./webapp/ ./

# Use maven to install the webapp
RUN mvn install -DskipTests=true \
 && rm -rf *

# Copy the webapp files needed for the build
COPY ./workflows/ ./

# Get Cellosaurus release 33 and use maven to install the workflows
RUN wget --output-document cellosaurus33.zip https://yareta.unige.ch/frontend/search/download/704f826d-e3bd-48d2-9fc4-4cae3a3a0e24 \
 && unzip cellosaurus33.zip \
 && mvn package -DskipTests=true

# Launch workflow
CMD /bin/bash start.sh
