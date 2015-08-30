(defproject cljsnode "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0"]
                 [enfocus "2.1.1"]
                 [kioo "0.4.1-SNAPSHOT"]]

  :npm {:dependencies [[express "4.13.3"]
                       [xmlhttprequest "*"]
                       [xmldom "0.1.19"]
                       [source-map-support "*"]
                       [react "0.13.3"]]
        :package {}
        :root :root}

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-npm "0.6.1"]
            [lein-figwheel "0.3.8-SNAPSHOT"]]

  :min-lein-version "2.1.2"

  :hooks [leiningen.cljsbuild]

  :aliases {"start" ["npm" "start"]}

  :main "main.js"

  :source-paths ["src/cljs"]

  :clean-targets ^{:protect false} [[:cljsbuild :builds :server :compiler :output-to]
                                    [:cljsbuild :builds :app :compiler :output-to]
                                    "node_modules"
                                    :target-path :compile-path]

  :figwheel {:http-server-root "public"
             :css-dirs ["resources/public/css"]
             :server-logfile "logs/figwheel.log"}

  :cljsbuild {:builds
              {:app
               {:source-paths ["src/browser" "src/cljs"]
                :compiler {:output-to "resources/public/js/out/app.js"
                           :output-dir "resources/public/js/out"
                           :asset-path "js/out"
                           :main app.start
                           :optimizations :none}}

               :server
               {; :target-path "target"
                :source-paths ["src/node" "src/cljs"]
                :compiler {:target :nodejs
                           :output-to "main.js"
                           :output-dir "target"
                          ; :asset-path "target"
                           :main server.core
                           :optimizations :none}
                }}}

  :profiles {:dev
             {:cljsbuild
              {:builds
               {:app
                {:compiler {:pretty-print true}
                 :figwheel true}
                :server
                {:compiler {:pretty-print true}
                 :figwheel {:heads-up-display false}}}}
              :npm {:dependencies [[ws "*"]]}}

             :prod
             {:env {:production true}
              :cljsbuild
              {:builds
               {:server
                {:compiler {:optimizations :simple
                            :foreign-libs [{:file "src/node/polyfill/simple.js"
                                            :provides ["polyfill.simple"]}]
                            :pretty-print false}}
                :app
                {:compiler {:output-dir "target/app/out"
                            :optimizations :advanced
                            :pretty-print false}}}}}})
