FROM node:19-alpine as node
# Source -> Dest
COPY --chown=gradle:gradle . project
WORKDIR /project/frontend
RUN npm install
RUN npm run build

FROM gradle:7-jdk11 AS build
COPY --from=node /project /project
WORKDIR /project
RUN GRADLE_OPTS='-Dkotlin.daemon.jvm.options=-Xmx1024m'
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 8080
RUN mkdir /app
COPY --from=build /project/build/libs/fat.jar /app/fat.jar
ENTRYPOINT ["java","-jar","/app/fat.jar"]