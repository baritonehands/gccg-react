(ns gccg.backend.handler
  (:require [hiccup.page :refer [include-js include-css html5]]
            [gccg.backend.middleware :refer [wrap-middleware]]
            [bidi.ring :refer [make-handler resources]]
            [ring.util.response :as response]
            [gccg.backend.api.games]))

(def mount-target
  [:div#app-container])

(defn head []
  [:head
   [:base {:href "/"}]
   [:meta {:charset "utf-8"}]
   [:meta {:name    "viewport"
           :content "width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"}]
   [:title "GCCG"]
   (include-css "/css/main.css")])

(def spa-page
  (html5
    (head)
    [:body
     mount-target
     (include-js "/js/web.js")]))

(def routes ["/"
             [["" (-> (response/response spa-page)
                      (response/content-type "text/html")
                      constantly)]
              ["api/" (vec (concat
                             gccg.backend.api.games/routes
                             [[["ping"] identity]]))]
              [true (constantly (response/not-found "Not Found"))]]])

(def app
  (wrap-middleware (make-handler routes)))
