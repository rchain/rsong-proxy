#!/bin/bash
## usage ./startValidator.sh 

rnode run -s \
      --required-sigs 0 \
      --map_size 2048576000 \
      --thread-pool-size 5  \
      --validator-private-key \
      ae131c0502a2ddf9a23d3f893cd842f122fa29b5530cb2967410ea3c45e7c566
