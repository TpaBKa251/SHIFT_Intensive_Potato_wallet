FROM openjdk:17-jdk-slim
COPY ./build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cqfpp1pu0jms7386bqbg-a:5432/wallet_v9dv
ENV SPRING_DATASOURCE_USERNAME=wallet_v9dv_user
ENV SPRING_DATASOURCE_PASSWORD=GAqNaY7ew86J0SOs8JdaUVtVWUv6hyoL
EXPOSE 8080
CMD java -jar /opt/service.jar