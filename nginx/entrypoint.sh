#!/usr/bin/env bash

set -xeuo pipefail

/app/backend 8080 &
nginx -g "daemon off;" &

wait -n

exit $?
