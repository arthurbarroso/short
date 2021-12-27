(ns short.backoffice.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [short.backoffice.config :as config]
            [short.backoffice.views.login :as login]
            [short.backoffice.css :as css]
            [short.backoffice.events :as events]
            [short.backoffice.router :as router]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [router/router-component] root-el)))

(defn init []
  (router/init-routes!)
  ;; (initialize-styles)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/load-from-session-storage])
  (dev-setup)
  (css/mount-ui-styles)
  (mount-root))
