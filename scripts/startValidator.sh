#!/bin/bash
## usage ./startValidator.sh 

rnode run -s \
      --required-sigs 0 \
      --thread-pool-size 5  \
     --validator-private-key \
         bf608076af589b024b9fdd1109a0ae2adfb6806ed62965f874618d5ef044f33f

