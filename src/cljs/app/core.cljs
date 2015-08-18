(ns app.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]
   [clojure.string :as string]
   [goog.dom :as dom]
   [goog.events :as events]
   [reagent.core :as reagent :refer [atom]]
   [shared.jokes :refer [fresh-jokes]]))

(defn jokes-view [jokes]
  [:div (for [content @jokes]
          ^{:key content}
          [:p content])])

(defn init! []
  (let [el (dom/getElement "jokes")
        jokes-buf (fresh-jokes 5 3 :concur 15)
        jokes (atom nil)
        user-action (chan)]
    (events/listen el events/EventType.CLICK
                   (partial put! user-action))
    (go
      (while (some? (<! user-action))
        (when-not @jokes
          (reagent/render [jokes-view jokes] el))
        (reset! jokes (<! jokes-buf))))))

(init!)
