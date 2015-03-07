#!/bin/bash
 
# Bash script to Cleanup AEM using CURL
 
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
curl -u $CQ_USER:$CQ_USER_PASSWORD -X POST http://$HOST:$PORT/crx/packmgr/service/.json/etc/packages/com.adobe.cq.commerce/aem-commerce-framework-extensions-content-1.0-SNAPSHOT.zip?cmd=uninstall
curl -u $CQ_USER:$CQ_USER_PASSWORD -X POST http://$HOST:$PORT/crx/packmgr/service/.json/etc/packages/com.adobe.cq.commerce/aem-commerce-framework-extensions-content-1.0-SNAPSHOT.zip?cmd=replicate
curl -u $CQ_USER:$CQ_USER_PASSWORD -X POST http://$HOST:$PORT/crx/packmgr/service/.json/etc/packages/com.adobe.cq.commerce/aem-commerce-framework-extensions-content-1.0-SNAPSHOT.zip?cmd=delete
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/etc/commerce/products/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/etc/scaffolding/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/etc/tags/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/content/catalogs/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/content/dam/Summit2015
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/content/dam/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/content/summit-geometrixx-outdoors
curl -u $CQ_USER:$CQ_USER_PASSWORD -X DELETE http://$HOST:$PORT/apps/commerce-extentions