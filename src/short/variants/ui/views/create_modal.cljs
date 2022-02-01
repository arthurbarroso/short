(ns short.variants.ui.views.create-modal
  (:require [re-frame.core :as re-frame]
            [short.variants.ui.subs :as subs]
            [short.variants.ui.events :as events]
            [short.shared.ui.label :as label]
            [short.shared.ui.button :as button]
            [short.shared.ui.input :as input]
            [short.shared.ui.text :as text]
            [short.shared.ui.image-picker :as image-picker]
            [short.shared.ui.modal :as modal-wrapper]
            [short.shared :as shared]))

(defn generate-file-key [{:keys [product-id type]}]
  (str product-id "-" (shared/slugify type)))

(defn creation-handler [data input-id modal-open?]
  (let [image-data (image-picker/submit-image input-id
                                              (generate-file-key data))
        variant-data (merge data image-data)]
    (re-frame/dispatch
     [::events/create-product-variant (update variant-data :quantity #(js/parseInt %))])
    (modal-wrapper/close-modal modal-open?)))

(defn create-variant-modal
  [modal-open?]
  (let [variant-form (re-frame/subscribe [::subs/variant-form-values])
        input-id "variant-image"]
    (fn []
      [:<>
       [text/typography
        {:text (str "Adding variant to product: " (:product-name @variant-form))
         :variant "h2"}]
       [:form {:on-submit (fn [e]
                            (do (.preventDefault e)
                                (creation-handler @variant-form input-id modal-open?)))}
        [label/label {:text "Variant type (name)"
                      :extra-style "mt-1"}]
        [input/input {:value (:type @variant-form)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-variant-form-field-value :type %])
                      :placeholder "Variant type (name)"}]
        [label/label {:text "Variant quantity"
                      :extra-style "mt-1"}]
        [input/input {:value (:quantity @variant-form)
                      :type "number"
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-variant-form-field-value :quantity %])
                      :placeholder "1"}]
        [label/label {:text "Variant image"
                      :extra-style "mt-1"}]
        [image-picker/image-selector input-id]
        [button/button-outlined {:text "Create"
                                 :type "submit"
                                 :extra-style "mt-2"
                                 :on-click #()}]]])))

(defn modal [modal-open?]
  [modal-wrapper/modal {:modal-open? modal-open?}
   [create-variant-modal modal-open?]])
