(ns short.ui.css
  (:require [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.label :as label]
            [short.ui.section :as section]))

(def ui-styles
  (str "\n"
       button/button-css "\n"
       button/outlined-button-css "\n"
       input/input-css "\n"
       input/invalid-input-css "\n"
       label/label-css "\n"
       section/section-css))
