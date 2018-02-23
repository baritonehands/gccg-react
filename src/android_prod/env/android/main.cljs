(ns env.android.main
  (:require [gccg.android.core :as core]))

(set-print-fn! (constantly nil))
(set-print-err-fn! (constantly nil))

(core/init)
