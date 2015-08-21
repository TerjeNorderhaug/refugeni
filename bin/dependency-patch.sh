#!/usr/bin/env bash

# There was an error during development starting the server with
# lein npm start
# reported as:
# Error: ENOENT, no such file or directory 'target/server/lib/react.inc.js'
# The error appears to be due to the compiler somewhere using the root path
# instead of the target path. It could be worked around by creating a
# symbolic link in the target directory referring back to itself:

cd target
ln -s . target
