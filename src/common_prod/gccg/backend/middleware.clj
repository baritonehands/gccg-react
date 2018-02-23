(ns gccg.backend.middleware
  (:require [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.logger :refer [wrap-with-logger]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-resource "public")
      wrap-params
      wrap-gzip))
