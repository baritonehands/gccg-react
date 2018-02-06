(ns gccg.test.core
  (:require [doo.runner :refer-macros [doo-all-tests]]
            [gccg.common.attrs-test]))

(enable-console-print!)

(doo-all-tests)
