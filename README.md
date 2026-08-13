# Sistema de microservicios

Este proyecto tiene dos microservicios backend y un frontend.

## Requisitos

- Java 17
- Maven
- MySQL 8+
- Python 3 (para servir el frontend)

## 1) Crear la base de datos

Abre MySQL y ejecuta:

```sql
CREATE DATABASE bd_soporte_tecnico;
```

Si necesitas crear el usuario y permisos manualmente:

```sql
CREATE USER 'root'@'localhost' IDENTIFIED BY '12345678';
GRANT ALL PRIVILEGES ON bd_soporte_tecnico.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

## 2) Configuración de conexión

Los microservicios usan esta configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_soporte_tecnico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=12345678
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

Los backends no arrancan si MySQL no está activo o si las credenciales de la base de datos no coinciden. El proyecto necesita una base de datos funcional antes de probar la aplicación completa.
