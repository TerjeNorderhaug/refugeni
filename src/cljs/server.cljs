(ns server
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]])
  (:require [cljs.nodejs :as nodejs]
            [cljs.core.async :refer [chan close! timeout put! map<]]))

(enable-console-print!)

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
  "Fetch some jokes from The Internet Chuck Norris Database up to given limit"
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

(defn handler [req res]
  (.writeHead res 200 {"Content-Type" "text/plain"})
  (let [jokes-chan (fetch-some-jokes 500)]
    (go
      (while
        (when-let [joke (<! jokes-chan)]
          (.write res joke)
          ;; (<! (timeout 2000))
          ;; curl http://127.0.0.1:1337/
          (.write res "\n\n")))
      (.end res))))

(defn server [handler port]
  (-> (.createServer http handler)
      (.listen port)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server handler port)
    (println (str "Server running at http://127.0.0.1:" port "/"))))

(set! *main-cli-fn* -main)
