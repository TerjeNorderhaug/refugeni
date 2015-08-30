(ns server.core
  (:require-macros
   [cljs.core.async.macros :as m :refer [go go-loop alt!]])
  (:require
   [polyfill.compat]
   [cljs.nodejs :as nodejs]
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [reagent.core :as reagent :refer [atom]]
   [app.jokes :as jokes :refer [fresh-jokes]]
   [app.views :refer [jokes-page html5]]))

(enable-console-print!)

(def express (nodejs/require "express"))

(def scripts [{:src "/js/out/app.js"}
              "main_cljs_fn()"])

(defn handler [jokes-chan req res]
  (if (= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (-> (<! jokes-chan)
                     (jokes-page :scripts scripts)
                     (html5))))))

(defn server [handler port success]
  (let [jokes (fresh-jokes 12 2)]
    (doto (express)
      (.get "/" #(handler jokes %1 %2))
      (.use (.static express "resources/public"))
      (.listen port success))))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port
            #(println (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
