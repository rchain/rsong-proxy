#!/bin/bash
## usage ./startValidator.sh key
rnode run -s \
      --required-sigs 0 \
      --map_size 2048576000 \
      --thread-pool-size 70 \
      --validator-private-key b081323f1de029986252e28bd2c5f5dd2309551851d2cc5209b2d1025d6472f4
