#!/bin/bash
## usage ./startValidator.sh 

rnode run -s \
      --required-sigs 0 \
      --thread-pool-size 5  \
      --validator-private-key \
      6bd8981cf922a547ca3c2d218f747d8048b7999a2f744f14e124a7082991b7e3


