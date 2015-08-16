(ns server.core
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put! map<]]
            [clojure.string :as string]
            [server.json :refer [fetch-json]]))

(enable-console-print!)

(def express (nodejs/require "express"))

(defn fetch-jokes
  "Fetch a single collection of jokes from The Internet Chuck Norris Database"
  [n]
  (let [out (chan 1 (comp (map #(get-in % ["value" "joke"]))
                          (partition-all n)))]
    (async/pipeline-async n out
                          #(fetch-json %1 (fn [v](put! %2 v (partial close! %2))))
                          (async/to-chan
                           (repeat n "http://api.icndb.com/jokes/random")))
    out))

(defn render-page [lines]
  (->> lines
       (map #(str "<p>" %))
       (string/join "\n")
       (#(string/join "\n" ["<!DOCTYPE html>"
                            "<title>Jokes</title>"
                            "<main id='jokes'>" % "</main>"
                            "<script src='/js/app.js'></script>"])) ))

(defn handler [req res]
  (if (= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (render-page (<! (fetch-jokes 5)))))))

(defn server [handler port cb]
  (doto (express)
    (.get "/" handler)
    (.use (.static express "../resources/public"))
    (.listen port cb)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port
            #(println (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
