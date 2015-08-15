(ns server.jokes
  (:require
   [cljs.core.async :as async :refer [chan close! put!]]
   [server.json :refer [fetch-json]]))

(defn fetch-some-joke
  "Fetch joke from The Internet Chuck Norris Database"
  []
  (let [c (chan)]
    (fetch-json
     "http://api.icndb.com/jokes/random"
     (fn [response]
       (let [value (get-in response ["value" "joke"])]
         (put! c value)
         (close! c))))
    c))

(defn fetch-some-jokes
  "Fetch some jokes from The Internet Chuck Norris Database"
  [n]
  (async/merge (repeatedly n fetch-some-joke)))
