(ns prod.core
  (:require [gccg.desktop.core :as core]))

(set-print-fn! (constantly nil))
(set-print-err-fn! (constantly nil))

(core/init)
