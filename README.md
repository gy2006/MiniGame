# MiniGame

## Environment Variables

- `MINIGAME_HOST`: bound host name, default is `127.0.0.1`
- `MINIGAME_PORT`: bound port number, default is `8080`
- `MINIGAME_MIN_POOL_SIZE`: number of threads to keep in the pool for server, default is `20`
- `MINIGAME_MAX_POOL_SIZE`: the maximum number of threads to allow in the pool for server, default is `200`

## Resources

The default `level` and `users` defined in `src/main/resources`

## Run Unit Test

```bash
mvn test
```

## Build Package

```bash
mvn package

# the package will be created at ./target dir
# to start from jar file: java -jar target/minigame-1.0.jar
```