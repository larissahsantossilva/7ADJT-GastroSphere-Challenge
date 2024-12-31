# Usa imagem base do Maven com suporte ao Java 21 para o estágio de build
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho para o build
WORKDIR /app

# Copia o arquivo pom.xml para o diretório de trabalho no container
COPY pom.xml /app/pom.xml

# Baixa as dependências do Maven para preparar o ambiente offline
RUN mvn dependency:go-offline

# Copia o código-fonte para o container
COPY src /app/src

# Realiza o build do projeto, criando o pacote JAR sem executar os testes
RUN mvn clean package -DskipTests

# Usa uma imagem base menor com o Java 21 para o estágio de deploy
FROM eclipse-temurin:21-alpine AS deploy

# Define o diretório de trabalho para o deploy
WORKDIR /app

# Copia o JAR gerado no estágio de build para o diretório de trabalho
COPY --from=build /app/target/*.jar /app/gastrosphere.jar

# Configura o comando de entrada para executar o JAR da aplicação
ENTRYPOINT ["java", "-jar", "gastrosphere.jar"]
