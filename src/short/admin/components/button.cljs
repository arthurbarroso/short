(ns short.admin.components.button
  (:require [stylefy.core :as stylefy :refer [use-style]]))

(def common-button-style
  {:outline "0"
   :cursor "pointer"
   :border-radius "4px"
   :border "none"
   :height "32px"
   :font-weight 700
   :background "#0166D6"
   :color "#FFF"
   :text-transform "uppercase"})

(def button-style
  (merge common-button-style
         {:padding "0.8%"
          ::stylefy/mode {:hover {:color "#FFF"}}}))

(def button-outlined-style
  (merge common-button-style
         {:padding "0.8% 3%"
          :background "none"
          :color "#0166D6"
          :border "solid 1.5px #D3D3D3"
          ::stylefy/mode {:hover {:border "1.5px solid #0166D6"}}}))

(defn button [{:keys [on-click text disabled title extra-styles]}]
  [:button
   (use-style
    (merge button-style extra-styles)
    {:type "button"
     :disabled disabled
     :title title
     :on-click #(on-click)})
   text])

(defn button-outlined [{:keys [on-click text disabled title extra-styles]}]
  [:button
   (use-style
    (merge button-outlined-style extra-styles)
    {:type "button"
     :disabled disabled
     :title title
     :on-click #(on-click)})
   text])
