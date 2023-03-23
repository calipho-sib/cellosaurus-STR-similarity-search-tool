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

# Pam
RUN mkdir -p ./html/js \
&& wget -O ./html/js/main.js https://www.cellosaurus.org/js/main.js

RUN mkdir -p ./html/css \
&& wget -O ./html/css/expasy_geneva.css https://www.cellosaurus.org/css/expasy_geneva.css \
&& wget -O ./html/css/normalize.css https://www.cellosaurus.org/css/normalize.css

RUN mkdir -p ./html/images/cellosaurus \
&& wget -O ./html/images/cellosaurus/cellosaurus.png https://www.cellosaurus.org/images/cellosaurus/cellosaurus.png

RUN mkdir -p ./html/images/icons \
&& wget -O ./html/images/icons/home.svg https://www.cellosaurus.org/images/icons/home.svg \
&& wget -O ./html/images/icons/browse.svg https://www.cellosaurus.org/images/icons/browse.svg \
&& wget -O ./html/images/icons/tool.svg https://www.cellosaurus.org/images/icons/tool.svg \
&& wget -O ./html/images/icons/download.svg https://www.cellosaurus.org/images/icons/download.svg \
&& wget -O ./html/images/icons/help.svg https://www.cellosaurus.org/images/icons/help.svg \
&& wget -O ./html/images/icons/contact.svg https://www.cellosaurus.org/images/icons/contact.svg

RUN mkdir -p ./html/images/expasy_logos \
&& wget -O ./html/images/expasy_logos/Expasy_logo_base_addon.svg https://www.cellosaurus.org/images/expasy_logos/Expasy_logo_base_addon.svg \
&& wget -O ./html/images/expasy_logos/irdirc_logo.png https://www.cellosaurus.org/images/expasy_logos/irdirc_logo.png \
&& wget -O ./html/images/expasy_logos/elixir_logo.png https://www.cellosaurus.org/images/expasy_logos/elixir_logo.png

RUN mkdir -p ./html/images/sib_images \
&& wget -O ./html/images/sib_images/sib_logo_199x106.gif https://www.cellosaurus.org/images/sib_images/sib_logo_199x106.gif


# Copy the files from the builder stage into the nginx html folder
COPY ./assets/img/ ./html/str-search/assets/img/
COPY --from=builder /app/out/ ./html/str-search/

# Add custom nginx config for routing
COPY ./nginx/default.conf ./conf.d/default.conf
