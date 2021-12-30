(ns short.ui.label
  (:require [garden.core :refer [css]]
            #?@(:cljs [[nubank.workspaces.core :as ws]
                       [nubank.workspaces.card-types.react :as ct.react]
                       [reagent.core :as reagent]])))

(def label-style
  {:align-self "flex-start"})

(def label-css
  (css [:.label label-style]))

(defn label [text]
  [:label {:class "label"}
   text])

#?(:cljs
   (declare label-card))
#?(:cljs
   (ws/defcard label-card
     (ct.react/react-card
      (reagent/as-element [label "some-label-text"]))))                                   :placeholder "some-text"
