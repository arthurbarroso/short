(ns short.shared.ui.css
  (:require [short.shared.ui.button :as button]
            [short.shared.ui.input :as input]
            [short.shared.ui.label :as label]
            [short.shared.ui.section :as section]
            [short.shared.ui.template :as template]
            [short.shared.ui.text :as text]
            [short.shared.ui.table :as table]
            [garden.core :refer [css]]))

(def reset-css
  (css [:body {:margin 0
               :outline 0
               :padding 0
               :-webkit-font-smoothing "antialiased"
               :-moz-osx-font-smoothing "grayscale"
               :font-family "'Roboto', sans-serif"
               :box-sizing "border-box"
               :-moz-box-sizing "border-box"}]))

(def form-style
  {:display "flex"
   :flex-direction "column"
   ;; :padding "2%"
   :border-radius "8px"})
   ;; :box-shadow "0 0 5pt 2pt #ededed"})

(def utilities
  (let [mt-3 (css [:.mt-3 {:margin-top "3%"}])
        mt-2 (css [:.mt-2 {:margin-top "2%"}])
        mt-1 (css [:.mt-1 {:margin-top "1%"}])
        form-base (css [:form form-style])]
    (reduce (fn [acc style]
              (str acc "\n" style))
            [mt-3 mt-2 mt-1 reset-css
             form-base])))

(def ui-styles
  (str "\n"
       button/button-css "\n"
       button/outlined-button-css "\n"
       input/input-css "\n"
       input/invalid-input-css "\n"
       label/label-css "\n"
       section/section-css "\n"
       template/layout-css "\n"
       text/text-css "\n"
       table/table-css "\n"
       utilities))
