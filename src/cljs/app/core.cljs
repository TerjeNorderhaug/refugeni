(ns app.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [clojure.string :as string]
   [goog.dom :as dom]
   [goog.events :as events]
   [app.json :refer [fetch-json]]
   [shared.lib :as lib]))

(defn fresh-jokes
  "Channel buffering collections of n jokes from The Internet Chuck Norris Database"
  ([n buf & {:keys [concur] :or {concur n}}]
   (let [out (chan buf (comp
                        (map #(get-in % ["value" "joke"]))
                        (partition-all n)))]
     (async/pipeline-async concur out
                           #(fetch-json %1 (fn [v](put! %2 v (partial close! %2))))
                                        ; http://dev.clojure.org/jira/browse/ASYNC-108
                                        ; (async/to-chan (repeat "http://api.icndb.com/jokes/random"))
                           (let [ch (chan n)]
                             (async/onto-chan ch (repeat "http://api.icndb.com/jokes/random"))
                             ch))
     out)))

(defn render [lines]
  (->> lines
       (map #(str "<p>" %))
       (string/join "\n")))

(defn init! []
  (let [el (dom/getElement "jokes")
        jokes-buf (fresh-jokes 5 3 :concur 15)
        user-action (chan)]
    (events/listen el events/EventType.CLICK
                   (partial put! user-action))
    (go
      (while (some? (<! user-action))
        (let [jokes (<! jokes-buf)]
          (set! (.-innerHTML el)
                (render jokes)))))))

(init!)
