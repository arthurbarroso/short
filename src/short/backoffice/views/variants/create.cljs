(ns short.backoffice.views.variants.create
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.text :as text]))

(defn creation-handler [data]
  (re-frame/dispatch [::events/create-variant (update data :quantity #(js/parseInt %))]))

(defn create-variant []
  (let [variant-form (re-frame/subscribe [::subs/variant-form-values])]
    (fn []
      [template/layout
       ^{:key "create-variant"}
       [:<>
        [text/typography
         {:text (str "Adding variant to product: " (:product-name @variant-form))
          :variant "h2"}]
        [:form {:on-submit (fn [e]
                             (do (.preventDefault e)
                                 (creation-handler @variant-form)))}
         [label/label {:text "Variant type (name)"
                       :extra-style "mt-1"}]
         [input/input {:value (:type @variant-form)
                       :on-change
                       #(re-frame/dispatch
                         [::events/set-variant-form-field-value :type %])
                       :placeholder "Variant type (name)"}]
         [label/label {:text "Variant image URL"
                       :extra-style "mt-1"}]
         [input/input {:value (:image-url @variant-form)
                       :on-change
                       #(re-frame/dispatch
                         [::events/set-variant-form-field-value :image-url %])
                       :placeholder "Variant image URL"}]
         [label/label {:text "Variant quantity"
                       :extra-style "mt-1"}]
         [input/input {:value (:quantity @variant-form)
                       :type "number"
                       :on-change
                       #(re-frame/dispatch
                         [::events/set-variant-form-field-value :quantity %])
                       :placeholder "1"}]
         [button/button-outlined {:text "Create"
                                  :type "submit"
                                  :extra-style "mt-2"
                                  :on-click #()}]]]])))
