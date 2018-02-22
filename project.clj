(defproject gccg "0.1.0-SNAPSHOT"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :source-paths ["src/build"]
  :description "A cross-platform client for GCCG"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/data.xml "0.2.0-alpha5"]
                 [re-frame "0.9.2"]
                 [funcool/tubax "0.2.0"]
                 [me.raynes/fs "1.4.6"]]
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

  :clean-targets ^{:protect false} ["target"
                                    "electron_app/gen"]

  :profiles {:dev  {:dependencies [[figwheel-sidecar "0.5.14"]
                                   [com.cemerick/piggieback "0.2.1"]]
                    :source-paths ["src/common_dev"]
                    :figwheel     {:css-dirs ["electron_app/css"]}
                    :cljsbuild    {:builds [{:source-paths ["src/electron"]
                                             :id           "electron-dev"
                                             :compiler     {:output-to      "electron_app/gen/js/main.js"
                                                            :output-dir     "electron_app/gen/js/electron-dev"
                                                            :optimizations  :simple
                                                            :pretty-print   true
                                                            :cache-analysis true}}
                                            {:source-paths ["src/desktop_dev" "src/desktop" "src/common"]
                                             :id           "desktop-dev"
                                             :figwheel     true
                                             :compiler     {:output-to      "electron_app/gen/js/desktop-core.js"
                                                            :output-dir     "electron_app/gen/js/desktop-dev"
                                                            :source-map     true
                                                            :asset-path     "gen/js/desktop-dev"
                                                            :optimizations  :none
                                                            :cache-analysis true
                                                            :main           dev.core}}
                                            {:id           "ios"
                                             :source-paths ["src/ios" "src/ios_dev" "src/mobile" "src/mobile_dev" "src/common"]
                                             :figwheel     true
                                             :compiler     {:output-to     "target/ios/not-used.js"
                                                            :main          "env.ios.main"
                                                            :output-dir    "target/ios"
                                                            :optimizations :none}}
                                            {:id           "android"
                                             :source-paths ["src/android" "src/android_dev" "src/mobile" "src/mobile_dev" "src/common"]
                                             :figwheel     true
                                             :compiler     {:output-to     "target/android/not-used.js"
                                                            :main          "env.android.main"
                                                            :output-dir    "target/android"
                                                            :optimizations :none}}
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
                                          :compiler     {:output-to      "electron_app/gen/js/main.js"
                                                         :output-dir     "electron_app/gen/js/electron-release"
                                                         :optimizations  :advanced
                                                         :pretty-print   true
                                                         :cache-analysis true
                                                         :infer-externs  true}}
                                         {:source-paths ["src/desktop" "src/common"]
                                          :id           "desktop-release"
                                          :compiler     {:output-to      "electron_app/gen/js/desktop-core.js"
                                                         :output-dir     "electron_app/gen/js/desktop-release"
                                                         :source-map     "electron_app/gen/js/desktop-core.js.map"
                                                         :optimizations  :advanced
                                                         :cache-analysis true
                                                         :infer-externs  true
                                                         :main           gccg.desktop.core}}
                                         {:id           "ios"
                                          :source-paths ["src" "env/prod"]
                                          :compiler     {:output-to     "index.ios.js"
                                                         :main          "env.ios.main"
                                                         :output-dir    "target/ios"
                                                         :static-fns    true
                                                         :optimize-constants true
                                                         :optimizations :simple
                                                         :closure-defines {"goog.DEBUG" false}}}
                                         {:id           "android"
                                          :source-paths ["src" "env/prod"]
                                          :compiler     {:output-to     "index.android.js"
                                                         :main          "env.android.main"
                                                         :output-dir    "target/android"
                                                         :static-fns    true
                                                         :optimize-constants true
                                                         :optimizations :simple
                                                         :closure-defines {"goog.DEBUG" false}}}]}}})
