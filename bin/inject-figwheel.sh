#!/usr/bin/env bash

# Figwheel 0.3.7 no longer generates a connection script for Node, failing to
# require "figwheel.connect". The cause is that require-connection-script-js
# explicitly bypass when target is nodejs. This flaw was introduced on Jun 26
# in commit 9bc615b. A work-around until the next fiwheel release
# is to trim down the script command leaving
# goog.require("figwheel.connect");

sed -i -e 's/document.write("<script>if (typeof goog != \\\"undefined\\\") { //g' "target/server/main.js"
sed -i -e 's/goog.require(\\\"figwheel.connect\\\")/goog.require("figwheel.connect")/g' "target/server/main.js"
sed -i -e 's/}<\/script>")//g' "target/server/main.js"
