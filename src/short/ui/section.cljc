(ns short.ui.section
  (:require [garden.core :refer [css]]))

(def section-style
  {:display "flex"
   :justify-content "center"
   :align-items "center"
   :height "100%"})

(def section-css
  (css [:.section section-style]))

(defn section
  [children]
  [:section
   {:class "section"}
   (into [:<> children])])
