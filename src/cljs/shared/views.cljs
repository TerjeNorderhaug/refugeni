(ns shared.views)

(defn jokes-view [jokes]
  [:div (for [content jokes]
          ^{:key content}
          [:p content])])

(defn jokes-page [lines]
  [:html
   [:title "Jokes"]
   [:main {:id "jokes"}
    [jokes-view lines]]
   [:script {:src "/js/app.js"}]])
