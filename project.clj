(defproject cljsnode "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1934"]
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]]
  :plugins [[org.bodil/lein-noderepl "0.1.10"]
            [lein-cljsbuild "0.3.4"]]
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:target :nodejs
                           :output-to "resources/js/server.js"
                           :optimizations :simple
                           :pretty-print true}}]})
