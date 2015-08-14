(ns server.jokes
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :as async :refer [chan close! put!]]))

(def http (nodejs/require "http"))

(defn fetch-joke-async [cb]
  (.get http "http://api.icndb.com/jokes/random"
        (fn [res]
          (.on res "data" (fn [data]
                            (let [response (js->clj (.parse js/JSON data))]
                              (cb (get-in response ["value" "joke"]))))))))

(defn fetch-some-joke
  "Fetch joke from The Internet Chuck Norris Database"
  []
  (let [c (chan)]
    (fetch-joke-async
     #(do
        (put! c %)
        (close! c)))
    c))

(defn fetch-some-jokes
  "Fetch some jokes from The Internet Chuck Norris Database"
  [n]
  (async/merge (repeatedly n fetch-some-joke)))
