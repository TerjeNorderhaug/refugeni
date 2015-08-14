(ns server.core
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put! map<]]
            [clojure.string :as string]
            [server.jokes :refer [fetch-some-jokes]]))

(enable-console-print!)

(def express (nodejs/require "express"))

(defn handler [req res]
  (let [jokes-chan (fetch-some-jokes 5)]
    (go-loop [content nil]
      (let [joke (<! jokes-chan)]
        (if (not (nil? joke))
          (recur (cons joke content))
          (do
            (.set res "Content-Type" "text/plain")
            (.send res (string/join "\n\n" content))))))))

(defn server [handler port cb]
  (let [app (express)]
    (.get app "/" handler)
    (.listen app port cb)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port
            #(println (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
