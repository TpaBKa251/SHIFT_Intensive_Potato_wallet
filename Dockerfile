FROM openjdk:17-jdk-slim
COPY ./build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://wallet_v9dv_user:GAqNaY7ew86J0SOs8JdaUVtVWUv6hyoL@dpg-cqfpp1pu0jms7386bqbg-a/wallet_v9dv
ENV POSTGRES_USER=wallet_v9dv_user
ENV POSTGRES_PASSWORD=GAqNaY7ew86J0SOs8JdaUVtVWUv6hyoL
EXPOSE 8080
CMD java -jar /opt/service.jar