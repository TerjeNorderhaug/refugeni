(ns shared.json
  (:import
   [goog.net XhrIo]
   [goog Uri]))

(defn fetch-json [uri cb]
  (.send XhrIo (Uri. uri)
         (fn [e]
           (let [xhr (.-target e)
                 res (.getResponseJson xhr)]
             (cb (js->clj res))))))
