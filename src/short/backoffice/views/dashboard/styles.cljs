(ns short.backoffice.views.dashboard.styles
  (:require [garden.core :refer [css]]))

(def product-list-header {:display "flex"
                          :flex-direction "row"
                          :align-items "center"
                          :justify-content "space-between"
                          :width "100%"})

(def product-view-styles
  (let [product-container-header-css
        (css [:.product-list-header product-list-header])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [product-container-header-css])))
