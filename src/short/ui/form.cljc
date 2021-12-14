(ns short.ui.form
  (:require [garden.core :refer [css]]))

(def form-style
  {:display "flex"
   :flex-direction "column"
   :align-items "center"
   :max-width "450px"
   :width "100%"
   :padding "2%"
   :border-radius "8px"
   :box-shadow "0 0 5pt 2pt #ededed"})

(def form-css
  (css [:.form form-style]))

(defn form [children]
  [:form {:class "form"}
   (into [:<> children])])
