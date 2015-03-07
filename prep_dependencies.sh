#!/bin/bash
 
# Bash script to copy across dependencies for maven into local repositories (as the dependencies are not avialble publicly yet)
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
# Echo external-dependencies
echo "Copying across files and folders from external-dependencies to ~/.m2/repository/" 

# Host that CQ runs on
cp -r external-dependencies/com ~/.m2/repository/


# Echo external-dependencies
echo "Copying Complete !!" 
