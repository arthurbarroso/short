(ns short.backoffice.router
  (:require [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [short.backoffice.views.dashboard.product-list :as product-list]
            [short.backoffice.views.login :as login]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]))

(def routes
  ["/"
   [""
    {:name :panel
     :view product-list/list-view
     :requires-authentication? true
     :attached-event ::events/get-products}]
   ["login"
    {:name :login
     :view login/login-view
     :requires-authentication? false
     :attached-event nil}]])

(def router
  (rf/router routes {:data {:coercion rcm/coercion}}))

(defn on-navigate [new-match authenticated?]
  (when new-match
    (let [{:keys [attached-event requires-authentication?]} (-> new-match :data)]
      (if (and requires-authentication? (not @authenticated?))
        (re-frame/dispatch [::events/navigate :login])
        (do
          (when attached-event
            (re-frame/dispatch [attached-event]))
          (re-frame/dispatch [::events/navigated new-match]))))))

(defn init-routes! []
  (let [authenticated? (re-frame/subscribe [::subs/is-authenticated?])]
    (rfe/start! router #(on-navigate % authenticated?) {:use-fragment false})))

(defn router-component []
  (let [current-route @(re-frame/subscribe [::subs/current-route])]
    (when current-route
      [(-> current-route :data :view)])))
