(ns app.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [clojure.string :as string]
   [goog.dom :as dom]
   [goog.events :as events]
   [shared.jokes :refer [fresh-jokes]]))

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
