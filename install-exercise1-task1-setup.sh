#!/bin/bash
 
# Bash script to automate git and maven commands
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
# Host that CQ runs on
git pull
mvn clean install -PautoInstallPackage 
