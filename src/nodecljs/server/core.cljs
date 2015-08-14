(ns server.core
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :as async :refer [chan close! timeout put! map<]]
            [clojure.string :as string]
            [server.jokes :refer [fetch-some-jokes]]))

(enable-console-print!)

(def express (nodejs/require "express"))

(defn handler [req res]
  (let [jokes-chan (async/into [] (fetch-some-jokes 5))]
    (go
      (.set res "Content-Type" "text/html")
      (->> (<! jokes-chan)
           (map #(str "<p>" %))
           (string/join "\n")
           (#(string/join "\n" "<!html><main>" % "<script src='/js/app.js'>"))
           (.send res)))))

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
