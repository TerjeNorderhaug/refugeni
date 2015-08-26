;; Entry point for node apps that needs the polyfill to be loaded
;; before modules are loaded.

(js/require "../goog/bootstrap/nodejs.js")

;; (These could possibly be resolved by adding a dependency to a core cljs module)
(.addDependency js/goog "../domina/support.js" #js ["domina.support"] #js ["polyfill.compat"])
(.addDependency js/goog "../hickory/core.js" #js ["hickory.core"] #js ["polyfill.compat"])

;; require(path.join(path.resolve("."),"..","main.js"));
;; (.require (.join js/path (.resolve js/path ".") ".." "main.js"))
(js/require "../../main.js")

;; ns is deliberately last to avoid requiring cljs.core:
(ns polyfill.boot)
