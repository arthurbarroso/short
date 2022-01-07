(ns short.backoffice.views.dashboard.styles
  (:require [garden.core :refer [css]]))

(def product-list-container {:display "flex"
                             :flex-direction "column"
                             :align-items "center"
                             :width "50%"})

(def product-list-header {:display "flex"
                          :flex-direction "row"
                          :align-items "center"
                          :justify-content "space-between"
                          :width "100%"})

(def product-view-styles
  (let [product-container-css
        (css [:.product-list-container product-list-container])
        product-container-header-css
        (css [:.product-list-header product-list-header])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [product-container-css
             product-container-header-css])))
