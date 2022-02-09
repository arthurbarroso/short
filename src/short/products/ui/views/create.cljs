(ns short.products.ui.views.create
  (:require [re-frame.core :as re-frame]
            [short.shared.ui.label :as label]
            [short.shared.ui.button :as button]
            [short.shared.ui.input :as input]
            [short.shared.ui.modal :as modal-wrapper]
            [short.shared :as shared]
            [short.products.ui.events :as events]
            [short.products.ui.subs :as subs]
            [short.products.contracts :as c]))

(defn create-product-handler [data modal-open?]
  (if (c/validate-form data)
    (do
      (re-frame/dispatch [::events/create-product data])
      (modal-wrapper/close-modal modal-open?))
    (cljs.pprint/pprint {:failed "!"})))

(defn create-product-modal
  [modal-open?]
  (let [form-data (re-frame/subscribe [::subs/product-form-values])]
    (fn []
      [:<>
       [:form {:on-submit (fn [e]
                            (.preventDefault e)
                            (create-product-handler
                             {:sku (:sku @form-data)
                              :active true
                              :slug (:slug @form-data)
                              :title (:title @form-data)
                              :price (js/parseFloat (:price @form-data))}
                             modal-open?))}
        [label/label {:text "Title"}]
        [input/input {:value (:title @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-product-form-field-value :title %])
                      :placeholder "Product title"}]
        (when-not (c/validate-title (:title @form-data))
          [:p "Invalid title"])
        [label/label {:text "Slug"
                      :extra-style "mt-1"}]
        [input/input {:value (:slug @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-product-form-field-value :slug (shared/slugify %)])
                      :placeholder "Product slug"}]
        (when-not (c/validate-slug (:slug @form-data))
          [:p "Invalid slug"])
        [label/label {:text "Sku"
                      :extra-style "mt-1"}]
        [input/input {:value (:sku @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-product-form-field-value :sku %])
                      :placeholder "Product sku"}]
        (when-not (c/validate-sku (:sku @form-data))
          [:p "Invalid sku"])
        [label/label {:text "Price"
                      :extra-style "mt-1"}]
        [input/input {:value (:price @form-data)
                      :type "number"
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-product-form-field-value :price %])
                      :placeholder "Product price"}]
        [button/button-outlined {:text "Create"
                                 :type "submit"
                                 :extra-style "mt-2"
                                 :on-click #()}]]])))

(defn modal [modal-open?]
  [modal-wrapper/modal {:modal-open? modal-open?}
   [create-product-modal modal-open?]])
