# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package.json ./
COPY yarn.lock ./
RUN yarn install
COPY . .
RUN yarn run build

# production stage
FROM nginx:stable-alpine as production-stage
RUN mkdir -p /usr/share/nginx/webpage
COPY --from=build-stage /app/build /usr/share/nginx/webpage
COPY nginx.conf /etc/nginx/conf.d/nginx.conf

# HTTP
EXPOSE 3000

