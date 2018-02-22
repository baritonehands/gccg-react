(ns gccg.platform.file.wrapper)

(def fs (js/require "fs"))

(defn read
  ([filename cb]
   (read filename {:encoding "latin1"} cb))
  ([filename {:keys [encoding]} cb]
   (.readFile fs filename
              (fn [err data]
                (if err
                  (cb err nil)
                  (cb nil (.toString data encoding)))))))

(defn write [filename data cb]
  (.writeFile fs filename data cb))
