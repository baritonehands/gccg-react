(ns gccg.platform.file.wrapper
  (:require-macros [gccg.assets :as assets]))

(def rn-fs (js/require "react-native-fs"))

(defn read
  ([filename cb]
   (read filename {:encoding "utf8"} cb))
  ([filename {:keys [encoding]} cb]
   (-> (.readFile rn-fs (str (.-MainBundlePath rn-fs) "/" filename) encoding)
       (.then (fn [res]
                (println "Fetched" filename (js->clj res))
                (cb nil res)))
       (.catch (fn [err]
                 (println "Error fetching" filename (js->clj err))
                 (cb err nil))))))

(defn write [filename data cb])
