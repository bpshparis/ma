#!/bin/sh

export SVC_NAME=dsc0
export KEY_NAME=user0
export ENV_NAME=env0

echo DSC_COLL_LANG=$DSC_COLL_LANG
echo DSC_VERSION=$DSC_VERSION
echo DSC_COLL_NAME=$DSC_COLL_NAME

#Store Discovery url in URL environment variable
URL=$(ibmcloud service key-show $SVC_NAME $KEY_NAME | awk 'NR >= 4 {print}' | jq -r '.url')
echo URL=$URL

#Store Discovery APIKEY in CRED environment variable
CRED=$(ibmcloud service key-show $SVC_NAME $KEY_NAME | awk 'NR >= 4 {print}' | jq -r '"apikey:" + .apikey')
echo CRED=$CRED

#Create env0 environment for Discovery service and store its id in ENVID
ENVID=$(curl  -X POST -u ${CRED} -H 'Content-Type: application/json' -d '{"name": "'$ENV_NAME'"}' ${URL}'/v1/environments?version='${DSC_VERSION} | jq -r '.environment_id')
echo ENVID=$ENVID

#Get configuration for Discovery service and store its id in CONFID
CONFID=$(curl -u ${CRED} ${URL}'/v1/environments/'${ENVID}'/configurations?version='${DSC_VERSION} | jq -r '.configurations[0].configuration_id')
echo CONFID=$CONFID

#Create collection for Discovery service and store its id in COLLID
COLLID=$(curl -X POST -H 'Content-Type: application/json' -u ${CRED} -d '{"name": "'$DSC_COLL_NAME'", "configuration_id":"'${CONFID}'" , "language": "'${DSC_COLL_LANG}'"}' ${URL}'/v1/environments/'${ENVID}'/collections?version='${DSC_VERSION} | jq -r '.collection_id')
echo COLLID=$COLLID


