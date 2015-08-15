(ns app.json
  (:import [goog.net Jsonp]
           [goog Uri]))

(defn fetch-json [uri cb]
  (let [req (Jsonp. (Uri. uri))]
    (.send req nil
           (fn [res]
             (cb (js->clj res))))))
