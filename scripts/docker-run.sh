#!/bin/bash
##
## run docker image for local dev
##
docker run \
       -p9000:9000 \
       -e GRPC_SERVER=${GRPC_SERVER} \
       -e GRPC_PORT=${GRPC_PORT} \
       -e API_VERSION=${API_VERSION} \
       -e HTTP_PORT=${HTTP_PORT} \
       -e AUTH_NAME}=${AUTH_NAME} \
       -e AUTH_PASSWORD}=${AUTH_PASSWORD} \
       -e DROP_BOX_ACCESS_TOKEN=${DROP_BOX_ACCESS_TOKEN} \
       kayvank/immersion-rc-proxy:1.0-SNAPSHOT

