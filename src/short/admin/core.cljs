(ns short.admin.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [short.admin.config :as config]
            [short.admin.views.login :as login]
            [short.admin.css :as css]
            [short.admin.events :as events]))

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
