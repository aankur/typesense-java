# Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/typesense/typesense-java.


## Pull Request Process

Please note we have a code of conduct, please follow it in all your interactions with the project.

Before making a PR to this repository look out for the following guidelines.

## Guidelines for updating the client

- Head over to [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) and download the cli jar.
- Download the latest api spec for typesense server from [typesense-api-spec](https://github.com/typesense/typesense-api-spec)
- Create a new `config.json` and add the following content
  ```json
  {
    "modelPackage" : "org.typesense.model",
    "apiPackage" : "org.typesense.api"
  }
  ```
- Run the following command:
  ```bash
  java -jar swagger-codegen-cli-3.0.20.jar generate -i <path-to-spec> -l jaxrs-cxf-client -c <path-to-config.json> -o <out-dir>
  ```
- Now, copy the content under ```<out-dir>/src/gen/java/org/typesense/model``` and replace it with the content of the ```typesense-java/src/main/java/org/typesense/model``` folder in the `typesense-java` client repository.
- And then make the necessary changes in `api` folder.

**NOTE**: The `model` directory is **read-only** make sure not to edit it.

## Publishing

Publishing requires the presence of a `~/.gradle.properties` file:

```shell
signing.keyId=x1234567
signing.password=
signing.secretKeyRingFile=/Users/user/.gnupg/secring.gpg

sonatypeUsername=xxxxxx
sonatypePassword=
```

The `secring.gpg` file can be generated with:

```shell
gpg --keyring secring.gpg --export-secret-keys > ~/.gnupg/secring.gpg
```

```shell
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

