#!/bin/bash
 
# Bash script to install solutions
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
# Host that CQ runs on
git pull
sh cleanup.sh
git checkout exercise3-task1-solutions
mvn clean install -PautoInstallPackage 
