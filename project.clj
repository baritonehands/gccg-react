(defproject gccg "0.1.0-SNAPSHOT"
  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}
  :source-paths ["src/backend" "src/build"]
  :resource-paths ["resources" "target/cljsbuild"]
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
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]
            [lein-doo "0.1.8"]]

  :aliases {"prod-build" ^{:doc "Recompile code with prod profile."}
                         ["do" "clean" ["prod-electron"] ["prod-mobile"]]
            "prod-electron" ^{:doc "Compile electron with prod profile."}
                         ["do"
                          ["with-profile" "prod" "cljsbuild" "once" "electron-main"]
                          ["with-profile" "prod" "cljsbuild" "once" "electron-renderer"]]
            "prod-mobile" ^{:doc "Compile mobile with prod profile."}
                         ["do"
                          ["with-profile" "prod" "cljsbuild" "once" "ios"]
                          ["with-profile" "prod" "cljsbuild" "once" "android"]]
            "node-test"  ^{:doc "Run unit tests in Node."}
                         ["do" "clean"
                          ["doo" "node" "test" "once"]]}

  :clean-targets ^{:protect false} ["target" "electron_app/gen" "index.ios.js" "index.android.js" "*-init.clj"]

  :minify-assets
  {:assets
   {"resources/public/css/main.min.css" ["resources/public/css/main.css"]
    "electron_app/gen/css/main.min.css" ["electron_app/css/main.css"]}}

  :uberjar-name "gccg.jar"

  :main gccg.backend.server

  :jvm-opts ["-Duser.timezone=UTC"]

  :profiles {:dev     {:dependencies   [[ring/ring-devel "1.6.1"]
                                        [ring-server "0.5.0"]
                                        [figwheel-sidecar "0.5.14"]
                                        [com.cemerick/piggieback "0.2.1"]]
                       :source-paths   ["src/common_dev" "src/backend_dev"]
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
                                                  :compiler     {:output-to     "target/cljsbuild/ios/not-used.js"
                                                                 :main          env.ios.main
                                                                 :output-dir    "target/cljsbuild/ios"
                                                                 :optimizations :none}}
                                                 {:id           "android"
                                                  :source-paths ["src/android" "src/android_dev" "src/mobile" "src/mobile_dev" "src/common"]
                                                  :figwheel     true
                                                  :compiler     {:output-to     "target/cljsbuild/android/not-used.js"
                                                                 :main          env.android.main
                                                                 :output-dir    "target/cljsbuild/android"
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
             :prod    {:dependencies [[react-native-externs "0.1.0"]]
                       :cljsbuild {:builds [{:source-paths ["src/electron_main"]
                                             :id           "electron-main"
                                             :compiler     {:output-to     "electron_app/gen/js/main.js"
                                                            :output-dir    "target/cljsbuild/electron-release"
                                                            :optimizations :advanced
                                                            :static-fns    true
                                                            :infer-externs true}}
                                            {:source-paths ["src/electron_renderer" "src/desktop" "src/desktop_prod" "src/common"]
                                             :id           "electron-renderer"
                                             :compiler     {:output-to     "electron_app/gen/js/electron-renderer.js"
                                                            :output-dir    "target/cljsbuild/electron-renderer"
                                                            :optimizations :advanced
                                                            :static-fns    true
                                                            :infer-externs true
                                                            :main          prod.core}}
                                            {:source-paths ["src/web" "src/desktop" "src/desktop_prod" "src/common"]
                                             :id           "web"
                                             :compiler     {:output-to     "target/cljsbuild/public/js/web.js"
                                                            :output-dir    "target/uberjar"
                                                            :optimizations :advanced
                                                            :static-fns    true
                                                            :infer-externs true
                                                            :main          prod.core}}
                                            {:source-paths ["src/ios" "src/ios_prod" "src/mobile" "src/common"]
                                             :id           "ios"
                                             :compiler     {:output-to          "index.ios.js"
                                                            :main               env.ios.main
                                                            :output-dir         "target/cljsbuild/ios"
                                                            :static-fns         true
                                                            :optimize-constants true
                                                            :optimizations      :simple
                                                            :closure-defines    {"goog.DEBUG" false}}}
                                            {:source-paths ["src/android" "src/android_prod" "src/mobile" "src/common"]
                                             :id           "android"
                                             :compiler     {:output-to          "index.android.js"
                                                            :main               env.android.main
                                                            :output-dir         "target/cljsbuild/android"
                                                            :static-fns         true
                                                            :optimize-constants true
                                                            :optimizations      :simple
                                                            :closure-defines    {"goog.DEBUG" false}}}]}}
             :uberjar {:hooks        [minify-assets.plugin/hooks]
                       :source-paths ["src/backend_prod"]
                       :prep-tasks   ["compile" ["with-profile" "prod" "cljsbuild" "once" "web"]]
                       :aot          :all
                       :omit-source  true}})
