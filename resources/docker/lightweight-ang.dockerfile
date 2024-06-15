# Stage 1: Build the Angular application
FROM node:18-alpine as app

# Set the working directory
WORKDIR /usr/src/app

COPY . /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies and build the Angular application
RUN npm install && npm run build

FROM nginx:alpine
#make to sure to follow you app name in the next line
COPY --from=app /usr/src/app/dist/my-angular-project/browser /usr/share/nginx/html

RUN ls /usr/share/nginx/html
# optional
# COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80