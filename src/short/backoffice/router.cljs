(ns short.backoffice.router
  (:require [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [short.backoffice.views.panel :as panel]
            [short.backoffice.views.login :as login]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]))

(def routes
  ["/"
   [""
    {:name :panel
     :view panel/panel-view
     :requires-authentication? true}]
   ["login"
    {:name :login
     :view login/login-view
     :requires-authentication? false}]])

(def router
  (rf/router routes {:data {:coercion rcm/coercion}}))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::events/navigated new-match])))

(defn init-routes! []
  (rfe/start! router on-navigate {:use-fragment false}))

(defn router-component []
  (let [current-route @(re-frame/subscribe [::subs/current-route])
        user-is-authenticated? @(re-frame/subscribe [::subs/is-authenticated?])
        current-route-requires-auth? (-> current-route :data :requires-authentication?)]
    (when current-route
      (if (and (true? current-route-requires-auth?)
               (or (false? user-is-authenticated?)
                   (nil? user-is-authenticated?)))
        [(login/login-view)]
        [(-> current-route :data :view)]))))
