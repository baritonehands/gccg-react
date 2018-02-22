(ns gccg.backend.middleware
  (:require [ring.middleware.params :refer [wrap-params]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.logger :refer [wrap-with-logger]]
            [ring.middleware.resource :refer [wrap-resource]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-resource "public")
      wrap-with-logger
      wrap-params
      wrap-exceptions
      wrap-reload
      wrap-gzip))
