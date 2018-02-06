(ns electron.menu)

(def os (js/require "os"))
(def electron (js/require "electron"))
(def global-shortcut (.-globalShortcut electron))
(def dialog (.-dialog electron))
(def browser-window (.-BrowserWindow electron))
(def ipc-main (.-ipcMain electron))

(def filters [{:name "App Builder Files" :extensions ["jab" "zab"]}])

(defn save []
  ;(.showSaveDialog dialog
  ;                 (.getFocusedWindow browser-window)
  ;                 (clj->js {:filters filters})
  ;                 (fn [files]
  ;                   (.log js/console "Opening" files)))
  (when-let [window (.getFocusedWindow browser-window)]
    (.send (.-webContents window) "save" "/Users/bgregg/Documents/test.jab")))

(def save-keystroke "Command+S")

(def open-keystroke "Command+O")

(defn open []
  (let [window (.getFocusedWindow browser-window)]
    (.showOpenDialog dialog
                     window
                     (clj->js {:properties ["openFile"]
                               :filters    filters})
                     (fn [files]
                       (.log js/console "Opening" files)
                       (.send (.-webContents window) "open" (aget files 0))))))

(defn template [app]
  (let [menu [
              {
               :label   "File",
               :submenu [
                         {:label       "Save"
                          :accelerator save-keystroke
                          :click       save}
                         {:label       "Open"
                          :accelerator open-keystroke
                          :click       open}
                         ]
               }
              {
               :label   "Edit",
               :submenu [
                         {:role "undo"},
                         {:role "redo"},
                         {:type "separator"},
                         {:role "cut"},
                         {:role "copy"},
                         {:role "paste"},
                         {:role "pasteandmatchstyle"},
                         {:role "delete"},
                         {:role "selectall"}
                         ]
               },
              {
               :label   "View",
               :submenu [
                         {:role "reload"},
                         {:role "toggledevtools"},
                         {:type "separator"},
                         {:role "resetzoom"},
                         {:role "zoomin"},
                         {:role "zoomout"},
                         {:type "separator"},
                         {:role "togglefullscreen"}
                         ]
               },
              {
               :role    "window",
               :submenu [
                         {:role "minimize"},
                         {:role "close"}
                         ]
               },
              {
               :role    "help",
               :submenu [
                         {
                          :label "Learn More",
                          :click (fn []
                                   (-> (js/require "electron")
                                       .-shell
                                       (.openExternal "https://electron.atom.io")))
                          }
                         ]
               }
              ]]
    (if (= (.platform os) "darwin")
      (as-> menu v
            (cons {
                   :label   (.getName app),
                   :submenu [
                             {:role "about"},
                             {:type "separator"},
                             {:role "services", :submenu []},
                             {:type "separator"},
                             {:role "hide"},
                             {:role "hideothers"},
                             {:role "unhide"},
                             {:type "separator"},
                             {:role "quit"}
                             ]
                   } v)
            (vec v)
            (update-in v [2 :submenu] conj {:type "separator"},
                       {
                        :label   "Speech",
                        :submenu [
                                  {:role "startspeaking"},
                                  {:role "stopspeaking"}
                                  ]
                        })
            (assoc-in v [4 :submenu] [{:role "close"},
                                      {:role "minimize"},
                                      {:role "zoom"},
                                      {:type "separator"},
                                      {:role "front"}]))
      menu)))
