#!/bin/bash

sudo apt-get -qq update
sudo apt-get install libssl0.9.8
wget -O couchbase-server-community_4.5.0.deb https://packages.couchbase.com/releases/4.5.0/couchbase-server-community_4.5.0-ubuntu14.04_amd64.deb
sudo dpkg -i couchbase-server-community_4.5.0.deb
sleep 10
/opt/couchbase/bin/couchbase-cli cluster-init -c 127.0.0.1:8091 -u Administrator -p administrator --cluster-name=reactivity --cluster-password=administrator --cluster-ramsize=256 --cluster-index-ramsize=256 --services=data,index,query,fts --index-storage-setting=default
/opt/couchbase/bin/couchbase-cli bucket-create -c 127.0.0.1:8091 -u Administrator -p administrator --bucket=artifact --bucket-ramsize=256