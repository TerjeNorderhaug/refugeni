(ns app.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [app.json :refer [fetch-json]]
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [clojure.string :as string]
   [goog.dom :as dom]
   [goog.events :as events]))

(enable-console-print!)

(defn fetch-some-joke
  "Fetch joke from The Internet Chuck Norris Database"
  []
  (let [c (chan)]
    (fetch-json "http://api.icndb.com/jokes/random"
                (fn [response]
                  (let [value (get-in response ["value" "joke"])]
                    (put! c value)
                    (println "Joke:" value)
                    (close! c))))
    c))

(defn fetch-some-jokes [n]
  (async/merge (repeatedly n fetch-some-joke)))

(defn render-jokes [lines]
  (->> lines
       (map #(str "<p>" %))
       (string/join "\n")))

(defn init []
  (let [el (dom/getElement "jokes")
        refresh-chan (chan)]
    (events/listen el events/EventType.CLICK
                   (partial put! refresh-chan))
    (go-loop []
      (let [jokes-chan (async/into [] (fetch-some-jokes 5))
            [_ results] (<! (async/map vector [(async/take 1 refresh-chan) jokes-chan]))]
        (set! (.-innerHTML el)
              (render-jokes results))
        (recur)))))

(init)
