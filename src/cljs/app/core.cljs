(ns app.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [goog.dom :as dom]
   [goog.events :as events]
   [reagent.core :as reagent :refer [atom]]
   [app.jokes :refer [fresh-jokes]]
   [app.views :refer [jokes-view]]))

(defn activate []
  (let [el (dom/getElement "jokes")
        jokes-buf (fresh-jokes 8 3 :concur 24)
        jokes (atom nil)
        user-action (chan)]
    (events/listen el events/EventType.CLICK
                   (partial put! user-action))
    (go
      (while (some? (<! user-action))
        (when-not @jokes
          (reagent/render [#(jokes-view @jokes)] el))
          (reset! jokes (<! jokes-buf))))))
