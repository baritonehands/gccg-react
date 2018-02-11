(ns gccg.common.file.wrapper)

(def fs (js/require "fs"))

(defn read [filename cb]
  (.readFile fs filename cb))

(defn write [filename data cb]
  (.writeFile fs filename data cb))
