(ns shared.jokes
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [shared.json :as json :refer [fetch-json]]))

(defn fresh-jokes
  "Channel buffering collections of n jokes from The Internet Chuck Norris Database"
  ([n] (fresh-jokes n 1))
  ([n buf & {:keys [concur] :or {concur n}}]
   (let [out (chan buf (comp
                        (map #(get-in % ["value" "joke"]))
                        (partition-all n)))]
     (async/pipeline-async concur out
                           #(fetch-json %1 (fn [v](put! %2 v (partial close! %2))))
                           ; Preferable but cannot do yet due to bug in core.async:
                           ; http://dev.clojure.org/jira/browse/ASYNC-108
                           ; (async/to-chan (repeat "http://api.icndb.com/jokes/random")
                           (let [ch (chan n)]
                             (async/onto-chan ch (repeat "http://api.icndb.com/jokes/random"))
                             ch))
     out)))
