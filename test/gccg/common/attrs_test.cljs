(ns gccg.common.attrs-test
  (:require [cljs.test :refer-macros [deftest testing is are]]
            [gccg.common.attrs :refer [points]]))

(def test-points
  [[0 0]
   [100 200]
   [500 1000]])

(deftest points-test
  (testing "stringifies points"
    (is (= (apply points test-points) "0,0 100,200 500,1000"))
    (is (= (apply points []) ""))))
