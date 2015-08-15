# cljsnode

Clojurescript based SPA using node express on Heroku.

## Run Locally

To start a server on your own computer:

    lein do deps, compile
    lein npm start

Point your browser to the displayed local port.
Click on the displayed text to refresh.

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
