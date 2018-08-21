#!/bin/bash
##
## increment playcount
##

host=localhost:9000

 curl -v \
   --header 'Content-Type: application/json'  \
   --header 'Accept: application/json' \
   -X PUT --data \
   '{"userId":"user-321","songId":"song-123","increment":1}' \
   $host/v1/user/playcount
