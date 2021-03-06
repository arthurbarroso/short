(ns short.shared.backoffice.router
  (:require [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [re-frame.core :as re-frame]
            [short.shared.subs :as subs]
            [short.shared.events :as events]
            [short.products.ui.views.list :as product-list]
            [short.products.ui.events :as product-events]
            [short.users.ui.views.login :as login]
            [short.shared.ui.loader :as loader]
            [short.shared.ui.toast :as toast]))

(def routes
  ["/"
   [""
    {:name :panel
     :view product-list/list-view
     :requires-authentication? true
     :attached-event ::product-events/get-products}]
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
      [:<>
       [loader/loader-wrapper
        [toast/toast-wrapper
         [(-> current-route :data :view)]]]])))
