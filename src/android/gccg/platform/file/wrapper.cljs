(ns gccg.platform.file.wrapper)

(def rn-fs (js/require "react-native-fs"))

(defn read
  ([filename cb]
   (read filename {:encoding "utf8"} cb))
  ([filename {:keys [encoding]} cb]
   (-> (.readFileAssets rn-fs filename encoding)
       (.then (fn [res]
                (cb nil res)))
       (.catch (fn [err]
                 (println "Error fetching" filename (js->clj err))
                 (cb err nil))))))

(defn write [filename data cb])
