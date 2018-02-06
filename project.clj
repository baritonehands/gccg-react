(defproject gccg "0.1.0-SNAPSHOT"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :source-paths ["src/clj"]
  :description "A cross-platform client for GCCG"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [re-frame "0.9.2"]]
  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.14"]
            [lein-doo "0.1.8"]]

  :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
                         ["do" "clean"
                          ["with-profile" "prod" "cljsbuild" "once" "electron-release"]
                          ["with-profile" "prod" "cljsbuild" "once" "desktop-release"]]
            "node-test" ^{:doc "Run unit tests in Node."}
                         ["do" "clean"
                          ["doo" "node" "test" "once"]]}

  :clean-targets ^{:protect false} ["resources/main.js"
                                    "resources/public/js/desktop-core.js"
                                    "resources/public/js/desktop-core.js.map"
                                    "resources/public/js/desktop-out"]

  :profiles {:dev  {:dependencies [[figwheel-sidecar "0.5.14"]
                                   [com.cemerick/piggieback "0.2.1"]]
                    :source-paths ["src/common_dev"]
                    :figwheel     {:css-dirs ["resources/public/css"]}
                    :cljsbuild    {:builds [{:source-paths ["src/electron"]
                                             :id           "electron-dev"
                                             :compiler     {:output-to      "resources/main.js"
                                                            :output-dir     "resources/public/js/electron-dev"
                                                            :optimizations  :simple
                                                            :pretty-print   true
                                                            :cache-analysis true}}
                                            {:source-paths ["src/desktop_dev" "src/desktop" "src/common"]
                                             :id           "desktop-dev"
                                             :figwheel     true
                                             :compiler     {:output-to      "resources/public/js/desktop-core.js"
                                                            :output-dir     "resources/public/js/desktop-out"
                                                            :source-map     true
                                                            :asset-path     "js/desktop-out"
                                                            :optimizations  :none
                                                            :cache-analysis true
                                                            :main           dev.core}}
                                            {:source-paths ["test" "src/desktop" "src/common"]
                                             :id           "test"
                                             :figwheel     true
                                             :compiler     {:output-to     "target/cljsbuild/test/out.js"
                                                            :output-dir    "target/cljsbuild/test/out"
                                                            :source-map    true
                                                            :optimizations :none
                                                            :target        :nodejs
                                                            :main          gccg.test.core}}]}
                    :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
             :prod {:cljsbuild {:builds [{:source-paths ["src/electron"]
                                          :id           "electron-release"
                                          :compiler     {:output-to      "resources/main.js"
                                                         :output-dir     "resources/public/js/electron-release"
                                                         :optimizations  :advanced
                                                         :pretty-print   true
                                                         :cache-analysis true
                                                         :infer-externs  true}}
                                         {:source-paths ["src/desktop" "src/common"]
                                          :id           "desktop-release"
                                          :compiler     {:output-to      "resources/public/js/desktop-core.js"
                                                         :output-dir     "resources/public/js/desktop-release-out"
                                                         :source-map     "resources/public/js/desktop-core.js.map"
                                                         :optimizations  :advanced
                                                         :cache-analysis true
                                                         :infer-externs  true
                                                         :main           gccg.desktop.core}}]}}})
