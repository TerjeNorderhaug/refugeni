(ns app.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require
   [cljs.core.async :as async :refer [chan close! timeout put!]]))

(enable-console-print!)
