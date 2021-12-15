(ns short.ui.css
  (:require [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.label :as label]
            [short.ui.section :as section]
            [short.ui.form :as form]
            [garden.core :refer [css]]))

(def utilities
  (let [mt-3 (css [:.mt-3 {:margin-top "3%"}])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [mt-3])))

(def ui-styles
  (str "\n"
       button/button-css "\n"
       button/outlined-button-css "\n"
       input/input-css "\n"
       input/invalid-input-css "\n"
       label/label-css "\n"
       form/form-css "\n"
       section/section-css "\n"
       utilities))
