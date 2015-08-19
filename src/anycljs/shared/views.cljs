(ns shared.views)

(defn jokes-view [jokes]
  [:div (for [content jokes]
          ^{:key content}
          [:p content])])
