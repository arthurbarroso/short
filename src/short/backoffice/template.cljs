(ns short.backoffice.template
  (:require [reagent.core :as r]
            [short.ui.text :as text]))

(defn layout [_children]
  (r/create-class
   {:reagent-render
    (fn [children]
      [:div {:class "base"}
       [:div {:class "content"}
        [:header {:class "header"}
         [:a {:href "/"
              :class "header-link"}
          [text/typography {:text "short"
                            :variant "h1"
                            :sizing "text-xxxl"}]]]
        [:div {:class "container"}
         (into [:<> children])]]])}))
