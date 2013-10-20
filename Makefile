heroku-config:
	@heroku config:set BUILDPACK_URL='https://github.com/maio/heroku-buildpack-nodejs#clojurescript'

run: resources/js/server.js
	@bundle exec guard -i
