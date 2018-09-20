#!/bin/bash
##
## run docker image for local dev
## script assumes .envrc is set
##
if [[ $# < 1 ]]; then
    echo "Usage is $0 docker-image"
    exit -1
fi

docker run \
       -p9000:9000 \
       -e GRPC_SERVER=${GRPC_SERVER} \
       -e GRPC_PORT_INTERNAL=${GRPC_PORT_INTERNAL} \
       -e API_VERSION=${API_VERSION} \
       -e HTTP_PORT=${HTTP_PORT} \
       -e HOST_URL=${LOCAL_K8} \
       $1

