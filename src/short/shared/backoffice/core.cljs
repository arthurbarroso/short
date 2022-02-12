(ns short.shared.backoffice.core
  (:require [re-frame.core :as re-frame]
            [reagent.dom :as rdom]
            [short.shared.backoffice.config :as config]
            [short.shared.backoffice.css :as css]
            [short.shared.backoffice.router :as router]
            [short.shared.events :as events]
            ["react-modal" :as Modal]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (Modal/setAppElement root-el)
    (rdom/render [router/router-component] root-el)))

(defn init []
  ;; (initialize-styles)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/load-from-session-storage])
  (router/init-routes!)
  (dev-setup)
  (css/mount-ui-styles)
  (mount-root))
