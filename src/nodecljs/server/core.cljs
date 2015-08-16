(ns server.core
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put!]]
            [clojure.string :as string]
            [shared.jokes :as jokes :refer [fresh-jokes]]
            [server.compat]))

(enable-console-print!)

(def express (nodejs/require "express"))

(defn render-page [lines]
  (->> lines
       (map #(str "<p>" %))
       (string/join "\n")
       (#(string/join "\n" ["<!DOCTYPE html>"
                            "<title>Jokes</title>"
                            "<main id='jokes'>" % "</main>"
                            "<script src='/js/app.js'></script>"])) ))

(defn jokes-handler [jokes req res]
  (if (= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (render-page (<! jokes))))))

(defn server [handler port success]
  (doto (express)
    (.get "/" (partial jokes-handler (fresh-jokes 5)))
    (.use (.static express "../resources/public"))
    (.listen port success)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port
            #(println (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
