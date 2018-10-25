#!/bin/bash
## usage ./startValidator.sh 
DIR=~/dev/workspaces/workspace-rchain/rchain/node/target/universal/rnode-0.7.1
EX=$DIR/bin/rnode
$EX run -s \
      --mapSize 2048576000 \
      --required-sigs 0 \
       --thread-pool-size 5  \
       --bonds-file ~/.rnode/genesis/bonds.txt \
      --validator-private-key \
      793fbe701f0292743629484ca9f2d73d7cfb8ccdd186f536768fefd997b08074
