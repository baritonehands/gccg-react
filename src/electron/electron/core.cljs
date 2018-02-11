(ns electron.core
  (:require [electron.menu :as main-menu]))

(def electron (js/require "electron"))
(def app (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def crash-reporter (.-crashReporter electron))
(def menu (.-Menu electron))
(def os (js/require "os"))

(def main-window (atom nil))
(def player-window (atom nil))

(defn init-window [win-atom name size]
  (reset! win-atom (browser-window.
                        (clj->js size)))
  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL ^js/electron.BrowserWindow @win-atom (str "file://" js/__dirname "/../../" name ".html"))
  (.on ^js/electron.BrowserWindow @win-atom "closed" #(reset! win-atom nil)))

(defn init-browser []

  (init-window main-window "index" {:width  800
                                    :height 600})

  (.setApplicationMenu
    menu
    (.buildFromTemplate
      menu
      (clj->js (main-menu/template app)))))

; CrashReporter can just be omitted
(.start crash-reporter
        (clj->js
          {:companyName "MyAwesomeCompany"
           :productName "MyAwesomeApp"
           :submitURL   "https://example.com/submit-url"
           :autoSubmit  false}))

(.on app "window-all-closed" #(when-not (= (.platform os) "darwin")
                                (.quit app)))
(.on app "ready" init-browser)
