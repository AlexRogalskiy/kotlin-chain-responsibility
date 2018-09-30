#!/usr/bin/env bash


A='{
  "status":"WAITING_IN_HUB",
  "parcels":2,
  "weight":null,
  "reference":"ABCD123456"
}'

B='{
  "status":"WAITING_IN_HUB",
  "parcels":2,
  "weight":2,
  "reference":"ABCD123456"
}'

C='{
  "status":"WAITING_IN_HUB",
  "parcels":1,
  "weight":15,
  "reference":"ABCD123456"
}'

D='{
  "status":"WAITING_IN_HUB",
  "parcels":2,
  "weight":30,
  "reference":"ABCD123456"
}'

E='{
  "status":"DELIVERED",
  "parcels":2,
  "weight":2,
  "reference":"ABCD123456"
}'

F='{
  "status":"DELIVERED",
  "parcels":2,
  "weight":30,
  "reference":"ABCD123456"
}'

G='{
  "status":"DELIVERED",
  "parcels":2,
  "weight":30,
  "reference":"EFGH123456"
}'

H='{
  "status":"DELIVERED",
  "weight":30,
  "reference":"ABCD123456"
}'

case $1 in
    ([A-H])
       JSON=${!1}
       ;;
    (*)
       echo "Allowed first argument in the A..H range"
       exit 1
       ;;
esac

curl --header "Content-Type: application/json"  \
    --request PUT  --data "${JSON}"  http:/localhost:8085/api/push
