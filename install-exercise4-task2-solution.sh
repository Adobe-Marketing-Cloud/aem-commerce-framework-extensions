#!/bin/bash
 
# Bash script to automate git and maven commands
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
git add *
git stash
git pull
git checkout exercise4-task2-solutions
sh cleanup.sh
sh prep_dependencies.sh
mvn eclipse:clean eclipse:eclipse
mvn clean install -PautoInstallPackage 
