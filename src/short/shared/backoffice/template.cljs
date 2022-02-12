(ns short.shared.backoffice.template
  (:require [reagent.core :as r]
            [short.shared.ui.text :as text]))

(defn layout [_children]
  (r/create-class
   {:reagent-render
    (fn [children]
      [:div {:class "base"}
       [:div {:class "content"}
        [:div {:class "container"}
         (into [:<> children])]]])}))
