FROM openjdk:17-jdk-slim
COPY build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cr5ir7tumphs73e4rnjg-a:5432/wallet_gu3s
ENV SPRING_DATASOURCE_USERNAME=wallet_gu3s_user
ENV SPRING_DATASOURCE_PASSWORD=hiNaqnJ9xOzXp3PMw88rSpDajcBs23je
EXPOSE 8080
CMD java -jar /opt/service.jar
