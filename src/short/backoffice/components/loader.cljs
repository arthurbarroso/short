(ns short.backoffice.components.loader
  (:require [reagent.core :as r]
            ["react-loading-overlay" :default LoadingOverlay]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]))

(defn loader-wrapper
  []
  (let [loading? (re-frame/subscribe [::subs/loading?])]
    (fn []
      (into [:> LoadingOverlay {:active @loading?
                                :spinner true
                                :text "..."}]
            (r/children (r/current-component))))))
