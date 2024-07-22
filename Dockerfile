FROM openjdk:17-jdk-slim
COPY ./build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=postgresql://wallet:DOpiXiuhloDbOhkneKhA3OcYCzwqlnio@dpg-cqfapmt6l47c739m9lb0-a/wallet_spgk
ENV POSTGRES_USER=wallet
ENV POSTGRES_PASSWORD=DOpiXiuhloDbOhkneKhA3OcYCzwqlnio
EXPOSE 8080
CMD java -jar /opt/service.jar