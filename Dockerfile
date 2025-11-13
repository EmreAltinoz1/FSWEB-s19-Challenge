#### ---- BUILD STAGE ---- ####
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY . .

# mvnw çalıştırılabilir olmalı
RUN chmod +x mvnw

# Maven ile jar oluştur
RUN ./mvnw clean package -DskipTests


#### ---- RUN STAGE ---- ####
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# builder aşamasında oluşan jar’ı buraya kopyala
COPY --from=builder /app/target/twitterapi-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Jar dosyasını çalıştır
CMD ["java", "-jar", "app.jar"]
