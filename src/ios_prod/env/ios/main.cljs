(ns env.ios.main
  (:require [gccg.ios.core :as core]))

(set-print-fn! (constantly nil))
(set-print-err-fn! (constantly nil))

(core/init)
