(ns server.jokes
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put! map<]]
            [clojure.string :as string]))

(def http (nodejs/require "http"))

(defn fetch-some-joke
  "Fetch joke from The Internet Chuck Norris Database"
  []
  (let [c (chan)]
    (.get http "http://api.icndb.com/jokes/random"
          (fn [res]
            (.on res "data" (fn [data]
                              (let [response (js->clj (.parse js/JSON data))]
                                (put! c (get-in response ["value" "joke"])))))))
    c))

(defn fetch-some-jokes
  "Fetch some jokes from The Internet Chuck Norris Database up to given char limit"
  [max-len]
  (let [c (chan)]
    (go
      (loop [len 0]
        (if (< len max-len)
          (let [joke (<! (fetch-some-joke))]
            (put! c joke)
            (recur (+ len (count joke))))
          (close! c))))
    c))
