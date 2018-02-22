(defproject gccg "0.1.0-SNAPSHOT"
  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :source-paths ["src/backend"]
  :description "A cross-platform client for GCCG"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.9"]
                 [liberator "0.15.1"]
                 [ring/ring-jetty-adapter "1.6.1"]
                 [ring/ring-core "1.6.1"]
                 [ring-logger "0.7.6"]
                 [bk/ring-gzip "0.1.1"]
                 [prone "1.1.1"]
                 [bidi "2.1.3"]
                 [me.raynes/fs "1.4.6"]

                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/data.xml "0.2.0-alpha5"]
                 [re-frame "0.9.2"]
                 [funcool/tubax "0.2.0"]
                 [cljs-ajax "0.7.3"]]
  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.14"]
            [lein-doo "0.1.8"]]

  :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
                         ["do" "clean"
                          ["with-profile" "prod" "cljsbuild" "once" "electron-release"]
                          ["with-profile" "prod" "cljsbuild" "once" "desktop-release"]]
            "node-test"  ^{:doc "Run unit tests in Node."}
                         ["do" "clean"
                          ["doo" "node" "test" "once"]]}

  :clean-targets ^{:protect false} ["target"
                                    "electron_app/gen"]

  :profiles {:dev  {:dependencies   [[ring/ring-devel "1.6.1"]
                                     [ring-server "0.5.0"]
                                     [figwheel-sidecar "0.5.14"]
                                     [com.cemerick/piggieback "0.2.1"]]
                    :source-paths   ["src/build" "src/common_dev" "src/web_dev"]
                    :resource-paths ["resources" "target/cljsbuild"]
                    :figwheel       {:css-dirs     ["electron_app/css" "resources/public/css"]
                                     :ring-handler gccg.backend.handler/app}
                    :cljsbuild      {:builds [{:source-paths ["src/electron_main"]
                                               :id           "electron-main"
                                               :compiler     {:output-to      "electron_app/gen/js/main.js"
                                                              :output-dir     "electron_app/gen/js/electron-main"
                                                              :optimizations  :simple
                                                              :pretty-print   true
                                                              :cache-analysis true}}
                                              {:source-paths ["src/electron_renderer" "src/desktop_dev" "src/desktop" "src/common"]
                                               :id           "electron-renderer"
                                               :figwheel     true
                                               :compiler     {:output-to      "electron_app/gen/js/electron-renderer.js"
                                                              :output-dir     "electron_app/gen/js/electron-renderer"
                                                              :source-map     true
                                                              :asset-path     "gen/js/electron-renderer"
                                                              :optimizations  :none
                                                              :cache-analysis true
                                                              :main           dev.core}}
                                              {:source-paths ["src/web" "src/desktop_dev" "src/desktop" "src/common"]
                                               :id           "web"
                                               :figwheel     true
                                               :compiler     {:asset-path     "/js/out"
                                                              :output-to      "target/cljsbuild/public/js/web.js"
                                                              :output-dir     "target/cljsbuild/public/js/out"
                                                              :source-map     true
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
                                              ;{:source-paths ["test" "src/desktop" "src/common"]
                                              ; :id           "test"
                                              ; :figwheel     true
                                              ; :compiler     {:output-to     "target/cljsbuild/test/out.js"
                                              ;                :output-dir    "target/cljsbuild/test/out"
                                              ;                :source-map    true
                                              ;                :optimizations :none
                                              ;                :target        :nodejs
                                              ;                :main          gccg.test.core}}
                                              ]}
                    :repl-options   {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}
             :prod {:source-paths ["src/build"]
                    :cljsbuild    {:builds [{:source-paths ["src/electron"]
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
                                             :compiler     {:output-to          "index.ios.js"
                                                            :main               "env.ios.main"
                                                            :output-dir         "target/ios"
                                                            :static-fns         true
                                                            :optimize-constants true
                                                            :optimizations      :simple
                                                            :closure-defines    {"goog.DEBUG" false}}}
                                            {:id           "android"
                                             :source-paths ["src" "env/prod"]
                                             :compiler     {:output-to          "index.android.js"
                                                            :main               "env.android.main"
                                                            :output-dir         "target/android"
                                                            :static-fns         true
                                                            :optimize-constants true
                                                            :optimizations      :simple
                                                            :closure-defines    {"goog.DEBUG" false}}}]}}})
