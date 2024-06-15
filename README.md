# for-dogs-api
For Dogs Service API

## Development

### Requirement
- Java >= 17ga
- docker >= 3

### Setup Keystore
```bash
# To use HTTPS in the local environment of the project, you need to generate the `keystore.p12` file
mkdir -p src/main/resources/ssl
keytool -genkey -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore src/main/resources/ssl/keystore.p12 -validity 4000
```

### Run
```bash
# Using Docker Compose to start containers
docker-compose up -d --force-recreate

# Stopping services defined by Docker Compose
docker-compose down -v
```
