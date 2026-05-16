#!/usr/bin/env bash

docker container run --rm \
    --user $(id -u):$(id -g) \
    --volume .:/thesis \
    kpituke/latex:2025.09.4 \
    make "$@"
