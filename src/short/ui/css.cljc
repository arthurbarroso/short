(ns short.ui.css
  (:require [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.label :as label]
            [short.ui.section :as section]
            [short.ui.template :as template]
            [short.ui.form :as form]
            [garden.core :refer [css]]))

(def reset-css
  (css [:body {:margin 0
               :outline 0
               :padding 0
               :-webkit-font-smoothing "antialiased"
               :-moz-osx-font-smoothing "grayscale"
               :font-family "'Roboto', sans-serif"}]));

(def utilities
  (let [mt-3 (css [:.mt-3 {:margin-top "3%"}])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [mt-3 reset-css])))

(def ui-styles
  (str "\n"
       button/button-css "\n"
       button/outlined-button-css "\n"
       input/input-css "\n"
       input/invalid-input-css "\n"
       label/label-css "\n"
       form/form-css "\n"
       section/section-css "\n"
       template/layout-css "\n"
       utilities))
