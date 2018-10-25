#!/usr/bin/env bash
ab -n 120000 -c 2 http://localhost:8080/persons
# wrk -t2 -c50 -d10m http://localhost:8080/persons