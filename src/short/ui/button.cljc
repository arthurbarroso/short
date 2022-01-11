(ns short.ui.button
  (:require [garden.core :refer [css]]
            #?@(:cljs [[nubank.workspaces.core :as ws]
                       [nubank.workspaces.card-types.react :as ct.react]
                       [reagent.core :as reagent]])))

(def button-text-sizes
  {:sm "14px"
   :n "16px"})

(def button-sizes
  {:full "64px"
   :default "48px"
   :large "48px"
   :field "40"
   :small "32"})

(def button-variants-css
  (reduce #(str %1 "\n" %2)
          [(css [:.button-full {:font-size (:sm button-text-sizes)
                                :height (:full button-sizes)}])
           (css [:.button-default {:font-size (:sm button-text-sizes)
                                   :height (:default button-sizes)}])
           (css [:.button-large {:font-size (:n button-text-sizes)
                                 :height (:large button-sizes)}])
           (css [:.button-field {:font-size (:sm button-text-sizes)
                                 :height (:field button-sizes)}])
           (css [:.button-small {:font-size (:sm button-text-sizes)
                                 :height (:small button-sizes)}])]))

(def button-css
  (reduce #(str %1 "\n" %2)
          [button-variants-css
           (css [:button {:outline "0" :cursor "pointer"
                          :border-radius "4px" :border "none"
                          :font-weight 700 :background "#0166D6"
                          :color "#FFF" :text-transform "uppercase"}
                 [:&:hover {:color "#FFF"}]])
           (css [:.button-outlined {:background "none"
                                    :color "#0166D6"
                                    :border "solid 1.5px #D3D3D3"}
                 [:&:hover {:border "1.5px solid #0166D6"
                            :color "black"}]])]))

(def outlined-button-css
  (css [:.button-outlined {:background "none"
                           :color "#0166D6"
                           :border "solid 1.5px #D3D3D3"}
        [:&:hover {:border "1.5px solid #0166D6"
                   :color "black"}]]))

(defn get-button-variant [variant]
  (case variant
    "small" "button-small"
    "sm" "button-small"
    "large" "button-large"
    "lg" "button-large"
    "field" "button-field"
    "fd" "button-field"
    "full" "button-full"
    "fl" "button-full"
    "button-default"))

(defn get-button-type [type]
  (if type
    type
    "button"))

(defn button [{:keys [on-click text disabled title extra-style variant type]}]
  [:button
   {:type (get-button-type type)
    :class (str (get-button-variant variant) " " extra-style)
    :disabled disabled
    :title title
    :on-click #(on-click)}
   text])

(defn button-outlined [{:keys [on-click text disabled title extra-style variant type]}]
  [:button
   {:type (get-button-type type)
    :class (str (get-button-variant variant) " button-outlined" " " extra-style)
    :disabled disabled
    :title title
    :on-click #(on-click)}
   text])

#?(:cljs
   (declare button-default-card))
#?(:cljs
   (ws/defcard button-default-card
     (ct.react/react-card
      (reagent/as-element [button {:text "Button"
                                   :title "Click for some action"}]))))

#?(:cljs
   (declare button-large-card))
#?(:cljs
   (ws/defcard button-large-card
     (ct.react/react-card
      (reagent/as-element [button {:text "Button"
                                   :title "Click for some action"
                                   :variant "lg"}]))))

#?(:cljs
   (declare button-full-card))
#?(:cljs
   (ws/defcard button-full-card
     (ct.react/react-card
      (reagent/as-element [button {:text "Button"
                                   :title "Click for some action"
                                   :variant "fl"}]))))

#?(:cljs
   (declare button-small-card))
#?(:cljs
   (ws/defcard button-small-card
     (ct.react/react-card
      (reagent/as-element [button {:text "Button"
                                   :title "Click for some action"
                                   :variant "sm"}]))))

#?(:cljs
   (declare button-outlined-default-card))
#?(:cljs
   (ws/defcard button-outlined-default-card
     (ct.react/react-card
      (reagent/as-element [button-outlined {:text "Button"
                                            :title "Click for some action"}]))))
