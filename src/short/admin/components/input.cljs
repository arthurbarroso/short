(ns short.admin.components.input
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def input-base-style
  {:color "#757575"
   :padding "0.8%"
   :border-radius "8px"
   :background-color "rgb(255, 255, 255)"
   :border "solid 1.5px #D3D3D3"
   :text-indent "5px"
   :font-size "16px"
   :height "32px"
   ::stylefy/mode {:focus {:box-shadow "0 0 5pt 2pt #ededed"
                           :outline-width "0px"}}})

(def invalid-input-style
  (merge
   input-base-style
   {:box-shadow "0 0 1pt 1pt #D4A54D"
    :border "solid 1.5px #D4A54D"
    :outline-width "0px"}))

(defn get-input-valid-style [valid?]
  (case valid?
    nil input-base-style
    false invalid-input-style
    true input-base-style))

(defn input
  [{:keys [value on-change type id disabled placeholder extra-styles on-blur valid?]}]
  [:input
   (use-style (merge (get-input-valid-style valid?) extra-styles)
              {:type type
               :value value
               :id id
               :on-blur on-blur
               :placeholder placeholder
               :disabled disabled
               :on-change #(on-change (-> % .-target .-value))})])
