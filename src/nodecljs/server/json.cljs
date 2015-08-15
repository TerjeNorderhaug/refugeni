(ns server.json
  (:require [cljs.nodejs :as nodejs]))

(def http (nodejs/require "http"))

(defn fetch-json [uri cb]
  (.get http uri
        (fn [res]
          (.on res "data" (fn [data]
                            (cb (js->clj (.parse js/JSON data))))))))
