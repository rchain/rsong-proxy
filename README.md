![GitHub Logo](docs/design/immersion-rc-proxy.jpeg)

# Immersion to Rchain Contract Proxy

A REST layer to proxy Immersion mobile device requests to RChain contracts

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system

### Prerequisites

#### Installs

- [sbt](https://www.scala-sbt.org/)
- [JDK8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html?printOnly=1)
- [docker](https://www.docker.com/)

#### clone and build the project

```
git clone git@github.com:kayvank/immersion-rc-proxy.git
cd immersion-rc-proxy
sbt compile
```

## Running the project locally

To run the project locally:
- set the environment variables
- run the docker

Alternatively you may build and run the project from source code.

### Environment variables

To run the project locally, configure your environment variables:


```
export HTTP_PORT=9000
export AUTH_NAME=secrete-user-name
export AUTH_PASSWORD=serete-password
export API_VERSION=v1
export GRPC_SERVER=localhost
export GRPC_PORT=5041
export DROPBOX_ACCESS_TOKEN='secrete-dropbox-token'
```

### Run the docker image
The build process pushes the docker image to docker hub. To run the image:

```
./docker-run.sh
```

### Running the Source code

```
sbt clean compile run
## to create a local dokcer image
sbt clean compile docker:stage docker:publishLocal
docker images | grep 'immersion-rc-proxy'
```

### Running tests:
```
curl localhost:9000/v1/song?userId=123
curl localhost:9000/v1/user/123
```

## url Inventory
```
host='localhost:9000'

## create a new user 
curl -X POST $host/v1/user/<user_id>

## retrieve  user  object
curl -X POST $host/v1/user/<user_id>

## reset playcount
curl -X PUT $host/v1/user/<user_id>/playcount

## retrive a user's song
curl -v  GET $host/v1/song/song1?userId=123 | jq

##  retrive user's songs
curl -v  GET $host/v1/song\?userId=user123\&perPage=10\&page=1 | jq

```
## Built With
[circle-ci](https://circleci.com/gh/kayvank)

## Deploymnet
Google Cloud Platform

## References

- [docker-image](https://hub.docker.com/r/kayvank/immersion-rc-proxy/tags/)
- [Rholang](https://developer.rchain.coop/assets/rholang-spec-0.2.pdf)
- [RChain Cooperative](https://www.rchain.coop/)
- [http4s](https://github.com/http4s/http4s)



