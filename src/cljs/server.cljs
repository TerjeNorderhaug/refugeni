(ns server
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :refer [chan close! timeout put! map<]])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

(def http (nodejs/require "http"))

(defn fetch-some-joke
  "Fetch joke from The Internet Chuck Norris Database"
  []
  (let [c (chan)]
    (.get http "http://api.icndb.com/jokes/random"
          (fn [res]
            (.on res "data" (fn [data]
                              (let [response (js->clj (JSON/parse data))]
                                (put! c (get-in response ["value" "joke"])))))))
    c))

(defn fetch-some-jokes
  "Fetch some jokes from The Internet Chuck Norris Database up to given limit"
  [max-len]
  (go
    (loop [result (<! (fetch-some-joke))]
      (if (< (count result) max-len)
        (recur (str result "\n\n" (<! (fetch-some-joke))))
        result))))

(defn handler [req res]
  (.writeHead res 200 {"Content-Type" "text/plain"})
  (go (.end res (<! (fetch-some-jokes 100)))))

(defn server [handler port]
  (-> (.createServer http handler)
      (.listen port)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port)
    (println (str "Server running at http://127.0.0.1:" port "/"))))

(set! *main-cli-fn* -main)
