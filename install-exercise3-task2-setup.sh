#!/bin/bash
 
# Bash script to install solutions
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
# Host that CQ runs on
git add *
git stash
git pull
sh cleanup.sh
git checkout exercise3-task2
sh prep_dependencies.sh
mvn eclipse:clean eclipse:eclipse
