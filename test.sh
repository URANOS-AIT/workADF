#!/bin/bash

cd `dirname $0`
./gradlew build || exit
./gradlew start -Pargs='adf.sample.AgentLoader -at:-1 -fb:-1'

