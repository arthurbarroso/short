(ns short.backoffice.views.products.styles
  (:require [garden.core :refer [css]]))

(def product-list-header {:display "flex"
                          :flex-direction "row"
                          :align-items "center"
                          :justify-content "space-between"
                          :width "100%"})

(def extra-table-style
  {:overflow "auto"
   :border "1px solid #eee"})

(def product-view-styles
  (let [product-container-header-css
        (css [:.product-list-header product-list-header])
        extra-table-css (css [:.table-wrapper extra-table-style])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [product-container-header-css
             extra-table-css])))
