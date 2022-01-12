(ns short.ui.input
  (:require [garden.core :refer [css]]
            #?@(:cljs [[nubank.workspaces.core :as ws]
                       [nubank.workspaces.card-types.react :as ct.react]
                       [reagent.core :as reagent]])))

(def input-base-style
  {:color "#757575"
   ;; :padding "0.8%"
   :border-radius "8px"
   :background-color "rgb(255, 255, 255)"
   :border "solid 1.5px #D3D3D3"
   :text-indent "5px"
   :font-size "16px"
   :height "48px"
   :box-sizing "border-box"})

(def input-css
  (css [:.input input-base-style
        [:&:focus {:box-shadow "0 0 5pt 2pt #ededed"
                   :outline-width "0px"}]]))

(def invalid-input-css
  (css [:.invalid-input {:box-shadow "0 0 1pt 1pt #D4A54D"
                         :border "solid 1.5px #D4A54D"
                         :outline-width "0px"}]))

(defn input
  [{:keys [value on-change type id disabled placeholder on-blur valid? extra-style]}]
  [:input
   {:type type
    :value value
    :class (str "input" " " extra-style)
    :id id
    :on-blur on-blur
    :placeholder placeholder
    :disabled disabled
    :on-change #(on-change (-> % .-target .-value))}])

#?(:cljs
   (declare input-card))
#?(:cljs
   (ws/defcard input-card
     (let [input-atom (reagent/atom "")]
       (ct.react/react-card
        input-atom
        (reagent/as-element [input {:value @input-atom
                                    :on-change #(reset! input-atom %)
                                    :type "text"
                                    :id "input"
                                    :placeholder "some-text"}])))))
