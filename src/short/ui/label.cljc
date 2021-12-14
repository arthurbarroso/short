(ns short.ui.label
  (:require [garden.core :refer [css]]))

(def label-style
  {:align-self "flex-start"})

(def label-css
  (css [:.label label-style]))

(defn label [text]
  [:label {:class "label"}
   text])
