#!/bin/bash
rm -rf ~/.rnode

~/opt/rnode.sh
sleep 5
kill -9 `cat ~/tmp/.rnode.pid`
sleep 1
ps -aef | grep rnode
ls -l  ~/.rnode/genesis 
