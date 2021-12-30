(ns short.ui.button
  (:require [garden.core :refer [css]]
            #?@(:cljs [[nubank.workspaces.core :as ws]
                       [nubank.workspaces.card-types.react :as ct.react]
                       [reagent.core :as reagent]])))

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
   :border "solid 1.5px #D3D3D3"})

(def button-css
  (css [:.button (merge common-button-style
                        button-style)
        [:&:hover {:color "#FFF"}]]))

(def outlined-button-css
  (css [:.button-outlined button-outlined-style
        [:&:hover {:border "1.5px solid #0166D6"}]]))

(defn button [{:keys [on-click text disabled title extra-style]}]
  [:button
   {:type "button"
    :class (str "button" " " extra-style)
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

#?(:cljs
   (declare button-card))
#?(:cljs
   (ws/defcard button-card
     (ct.react/react-card
      (reagent/as-element [button {:text "Button"
                                   :title "Click for some action"}]))))

#?(:cljs
   (declare button-outlined-card))
#?(:cljs
   (ws/defcard button-outlined-card
     (ct.react/react-card
      (reagent/as-element [button-outlined {:text "Button"
                                            :title "Click for some action"}]))))
