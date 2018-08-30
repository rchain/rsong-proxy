#!/bin/bash
## usage ./startValidator.sh key
 rnode run -s -p 4000 --bootstrap rnode://c61769b39d368cbcbc9499634e030386c79d5b02@52.119.8.108:40400 --validator-private-key $1
