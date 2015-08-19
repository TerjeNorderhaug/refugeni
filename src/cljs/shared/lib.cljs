(ns shared.lib
  (:require
   [cljs.core.async :as async :refer [chan close! put!]]))

(defn callback-chan
  "Calls a function that takes a callback, putting the value from the callback into a returned channel"
  [f]
  (let [c (chan)]
    (f
     (fn [response]
       (let [value response]
         (put! c value)
         (close! c))))
    c))
