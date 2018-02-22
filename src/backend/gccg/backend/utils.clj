(ns gccg.backend.utils)

(defn get-param [ctx k]
  (get-in ctx [:request :route-params k]))
