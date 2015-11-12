#!/bin/sh

./gradlew build

./gradlew start -Pargs="adf.sample.ambulance.tactics.MyTacticsAmbulance adf.sample.fire.tactics.MyTacticsFire adf.sample.police.tactics.MyTacticsPolice null null null"


