(ns short.ui.button
  (:require [garden.core :refer [css]]))

(def common-button-style
  {:outline "0"
   :cursor "pointer"
   :border-radius "4px"
   :border "none"
   :height "32px"
   :font-weight 700
   :background "#0166D6"
   :color "#FFF"
   :text-transform "uppercase"
   :width "100%"})

(def button-style
  {:padding "0.8%"})

(def button-outlined-style
  {:padding "0.8% 3%"
   :background "none"
   :color "#0166D6"
   :border "solid 1.5px #D3D3D3"
   :hover {:border "1.5px solid #0166D6"}})

(def button-css
  (css [:.button (merge common-button-style
                        button-style)
        [:&:hover {:color "#FFF"}]]))

(def outlined-button-css
  (css [:.button-outlined button-outlined-style
        [:&:hover {:border "1.5px solid #0166D6"}]]))

(defn button [{:keys [on-click text disabled title]}]
  [:button
   {:type "button"
    :class "button"
    :disabled disabled
    :title title
    :on-click #(on-click)}
   text])

(defn button-outlined [{:keys [on-click text disabled title]}]
  [:button
   {:type "button"
    :class "button button-outlined"
    :disabled disabled
    :title title
    :on-click #(on-click)}
   text])
