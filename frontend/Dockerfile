# Use node image for builder stage only
FROM node:8-slim as builder

# Change the working directory to /app/
WORKDIR /app/

# Copy the package.json file
COPY ./package.json ./package.json

# Install the node depedencies
RUN npm install

# Copy all the required files
COPY . .

# Run gulp to minify the assets
RUN npm run gulp

# Use nginx image as base
FROM nginx:alpine

# Change the working directory to /etc/nginx/
WORKDIR /etc/nginx/

# Download the external libraries
RUN mkdir -p ./html/str-search/external \
 && wget -O ./html/str-search/external/jquery.min.js https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js \
 && wget -O ./html/str-search/external/jquery-ui.min.js https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js \
 && wget -O ./html/str-search/external/jquery-ui.min.css https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css \
 && wget -O ./html/str-search/external/papaparse.min.js https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.1.0/papaparse.min.js \
 && wget -O ./html/str-search/external/xlsx.full.min.js https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.15.3/xlsx.full.min.js

# Copy the files from the builder stage into the nginx html folder
COPY ./assets/img/ ./html/str-search/assets/img/
COPY --from=builder /app/out/ ./html/str-search/

# Add custom nginx config for routing
COPY ./nginx/default.conf ./conf.d/default.conf
