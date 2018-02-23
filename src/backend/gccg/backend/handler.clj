(ns gccg.backend.handler
  (:require [hiccup.page :refer [include-js include-css html5]]
            [gccg.backend.middleware :refer [wrap-middleware]]
            [bidi.ring :refer [make-handler]]
            [ring.util.response :as response]
            [gccg.backend.api.games]))

;(def version
;  (json/read-str (slurp (io/resource "version.json"))))

(def mount-target
  [:div#app-container
   ;[:div.init
   ; [:div {:style {:textAlign "center"}}
   ;  [:div.s-loader-container.s-loader-bg-ghost
   ;   [:span.s-loader
   ;    [:div.s-loader-ring-primary]]]]]
   ])

;(defn prodify [s]
;  (if (env :dev)
;    s
;    (str s "?v=" (get version "version"))))

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

;(def cards-page
;  (html5
;    (head)
;    [:body
;     mount-target
;     (include-js "/js/app_devcards.js")]))

(def routes ["/"
             [["" (-> (response/response spa-page)
                      (response/content-type "text/html")
                      constantly)]
              ["api/" (into [["ping" (-> (response/response "Hello, World!")
                                         (response/content-type "text/plain")
                                         constantly)]]
                            (concat
                              gccg.backend.api.games/routes))]
              [true (response/not-found "Not Found")]]])

(def app
  (wrap-middleware (make-handler routes)))
