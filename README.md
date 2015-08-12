# cljsnode

Clojurescript web server using node on Heroku.

## Run Locally

To start a server on your own computer:

    lein cljsbuild once
    node resources/js/server.js

Point your browser to the displayed local port..

## Deploy to Heroku

To start a server on Heroku:

    heroku apps:create
    git push heroku master
    heroku open

This will open the site in your browser.

## License

Copyright © 2015 Terje Norderhaug

Copyright © 2013 Marian Schubert

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
