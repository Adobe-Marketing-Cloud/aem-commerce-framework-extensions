#!/bin/bash
 
# Bash script to automate git and maven commands
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
git add *
git stash
git pull
git checkout exercise4
sh cleanup.sh
sh prep_dependencies.sh
sh install_ep_and_ref_impl_on_aem.sh
mvn eclipse:clean eclipse:eclipse
mvn clean install -PautoInstallPackage 