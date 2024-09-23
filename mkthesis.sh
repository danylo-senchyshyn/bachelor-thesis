#!/bin/bash
docker container run --rm -it --user $(id -u):$(id -g) --volume .:/data kpituke/latex:2024.09.1 make "$@"
