#Usa a imagem base do OpenJDK 11
FROM openjdk:21-jdk-slim

#Define o diretório de trabalho dentro do container
RUN mkdir /app
WORKDIR /app

#Copia o arquivo JAR da aplicação para o container
COPY /target/gastrosphere-0.0.1-SNAPSHOT.jar /app

#Define a variável de ambiente (opcional)
ENV POSTGRES_HOST=postgres \
POSTGRES_PORT=5432 \
POSTGRES_DB=mydb \
POSTGRES_USER=myuser \
POSTGRES_PASSWORD=mypassword

#Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app/gastrosphere-0.0.1-SNAPSHOT.jar"]

#Expor a porta (se necessário)
EXPOSE 8080