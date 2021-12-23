(ns short.backoffice.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [short.backoffice.config :as config]
            [short.backoffice.views.login :as login]
            [short.backoffice.css :as css]
            [short.backoffice.events :as events]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [login/login-view] root-el)))

(defn init []
  ;; (init-routes!)
  ;; (initialize-styles)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (css/mount-ui-styles)
  (mount-root))
