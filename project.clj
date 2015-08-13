(defproject cljsnode "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :npm {:dependencies [[express "4.13.3"]]
        :root :target-path}

  :plugins [[org.bodil/lein-noderepl "0.1.11"]
            [lein-npm "0.6.1"]
            [lein-cljsbuild "1.0.6"]]

  :min-lein-version "2.0.0"

  :hooks [leiningen.cljsbuild]

  :main "server.js"

  :clean-targets [^{:protect false}
                  [:cljsbuild :builds 0 :compiler :output-to]
                  :target-path :compile-path]

  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:target :nodejs
                           :output-to "target/server.js"
                           :output-dir "out"
                           :optimizations :simple
                           :pretty-print true}}]})
