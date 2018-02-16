(ns gccg.assets
  (:require [clojure.data.xml :refer [parse-str]]
            [me.raynes.fs :as fs]
            [gccg.common.xml :as xml]
            [clojure.string :as s])
  (:import (java.io File)))

(defn read-file [file]
  (try
    (let [data-fn (condp = (fs/extension file)
                    ".xml" (fn [path]
                             (-> (slurp (fs/file path))
                                 (parse-str)
                                 (xml/entity->map)))
                    #(.getPath %))]
      (data-fn file))
    (catch Exception ex
      (println *err* (.getMessage ex))
      (.getPath file))))

(defn process-file [prefix path]
  (let [file (fs/file prefix path)
        root-idx (s/index-of (fs/absolute file) prefix)]
    [(.substring (.getPath file) root-idx)
     (read-file file)]))

(defn process-dir [exts prefix dir]
  (for [file (file-seq (fs/file prefix dir))
        :when (contains? exts (fs/extension file))]
    (let [root-idx (s/index-of (fs/absolute file) dir)]
      [(.substring (.getPath file) root-idx)
       (read-file file)])))

(defmacro bundle-xml [prefix path]
  (let [[root-path root] (process-file prefix path)
        files (for [card (:cardset root)]
                (process-file prefix (str (:dir root) "/" (:source card))))]
    `(into {} (list [~root-path ~root] ~@files))))
