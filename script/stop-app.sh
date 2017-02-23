#!/bin/bash

kill -9 `cat script/front.pid`
kill -9 `cat script/broadcaster.pid`
sudo service couchbase-server stop