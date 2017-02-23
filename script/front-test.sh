#!/bin/bash

./script/run-app.sh
cd front
npm run webdriver:update
xvfb-run npm test
xvfb-run npm run e2e
cd ..