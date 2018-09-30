#!/usr/bin/env bash

curl --header "Content-Type: application/json"  --request POST  --data '{
  "reference":"ABCD123456",
  "parcels" : [
  {
    "weight":1,
    "width": 10,
    "height": 10,
    "lenght": 10
  },
  {
    "weight":2,
    "width": 20,
    "height": 20,
    "lenght": 20
  }
  ]
}'  http:/localhost:8085/api/register
