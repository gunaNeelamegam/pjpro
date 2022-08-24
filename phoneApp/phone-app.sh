#!/bin/bash

JAVAFX=/home/user/Downloads/javafx-sdk-18.0.1/lib
JAR=/usr/lib/

## zenity --info --text "############\Phone\n###########\n"


java --module-path $JAVAFX --add-modules javafx.controls,javafx.fxml -jar $JAR/phone.jar

exit 0

