(ns shared.views
  (:require
   [reagent.core :as reagent :refer [atom]]))

(defn html5 [content]
  (str "<!DOCTYPE html>\n"
       (reagent/render-to-static-markup content)))

(defn jokes-view [jokes]
  [:div (for [content jokes]
          ^{:key content}
          [:p content])])

(defn jokes-page [lines & {:keys [scripts]}]
  [:html
   [:title "Jokes"]
   [:main {:id "jokes"}
    [jokes-view lines]]
   (for [ref scripts]
     [:script {:src ref}]) ])
