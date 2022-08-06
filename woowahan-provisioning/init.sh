#!/bin/bash
#########################
echo "Start to deploy the ES configuration"

echo "Remove elasticsearch container if exists"
docker-compose down

echo "Run the elasticsearch"
docker-compose up -d

###### Wait Elasticsearch Start
echo "Check Elasticsearch Status"
STATUS=-1
CHECK_CNT=1

while [[ ("$STATUS" -ne 200) && (CHECK_CNT -lt 10) ]]; do
  echo "Checking the ${CHECK_CNT}th.. wait for elasticsearch start."

  sleep 20
  CHECK_CNT=$((CHECK_CNT+1))
  STATUS=$(curl -o /dev/null -w "%{http_code}" "http:/127.0.0.1:9200/")
done

echo "Elasticsearch Run Success!"
###### Create index
echo "Create shop index"
MAPPING=$(cat ./mappings.txt)
echo "mappings : ${MAPPING}"

curl -XPUT "http://127.0.0.1:9200/baemin-shop" -H 'Content-Type: application/json' -d "${MAPPING}"
echo "Finish provisioning"