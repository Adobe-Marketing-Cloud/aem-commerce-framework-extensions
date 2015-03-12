#!/bin/bash
 
# Bash script to Install EP and EP reference implementations on AEM
 
# Author : Varun Venkataraman (Adobe Global Services)
# Version : 0.1
# Last updated : March 01, 2015
 
 
# Host that CQ runs on
HOST=localhost
 
# TCP port CQ listens on
PORT=4502 

# CQ Admin user ID
CQ_USER=admin
 
# CQ Admin user password
CQ_USER_PASSWORD=admin
  
# Uninstall and Delete packages from JCR
echo "Upload and Install EP Package on AEM" 
curl -u $CQ_USER:$CQ_USER_PASSWORD -F file=@"external-dependencies/ep-commerce-api-0-SNAPSHOT.zip" -F name="ep-commerce-api-0-SNAPSHOT.zip" -F force=true -F install=true http://$HOST:$PORT/crx/packmgr/service.jsp
curl -u $CQ_USER:$CQ_USER_PASSWORD -F file=@"external-dependencies/ep-geometrixx-content-0-SNAPSHOT.zip" -F name="ep-geometrixx-content-0-SNAPSHOT.zip" -F force=true -F install=true http://$HOST:$PORT/crx/packmgr/service.jsp