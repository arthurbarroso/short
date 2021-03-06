(ns short.products.ui.views.edit
  (:require [re-frame.core :as re-frame]
            [short.shared.ui.label :as label]
            [short.shared.ui.button :as button]
            [short.shared.ui.input :as input]
            [short.shared.ui.text :as text]
            [short.shared.ui.modal :as modal-wrapper]
            [short.shared :as shared]
            [short.products.ui.events :as events]
            [short.products.ui.subs :as subs]
            [short.products.contracts :as c]))

(defn edit-product-handler [data modal-open?]
  (re-frame/dispatch [::events/edit-product data])
  (modal-wrapper/close-modal modal-open?))

(defn edit-product-modal
  [modal-open?]
  (let [form-data (re-frame/subscribe [::subs/edit-product-form-values])]
    (fn []
      [:<>
       [text/typography
        {:text "Editing product"
         :variant "h2"}]
       [:form {:on-submit (fn [e]
                            (.preventDefault e)
                            (edit-product-handler
                             {:sku (:sku @form-data)
                              :active (:active @form-data)
                              :slug (:slug @form-data)
                              :title (:title @form-data)
                              :price (js/parseFloat (:price @form-data))
                              :uuid (:uuid @form-data)}
                             modal-open?))}
        [label/label {:text "Title"}]
        [input/input {:value (:title @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-edit-product-form-field-value :title %])
                      :placeholder "Product title"}]
        (when-not (c/validate-title (:title @form-data))
          [:p "Invalid title"])
        [label/label {:text "Slug"
                      :extra-style "mt-1"}]
        [input/input {:value (:slug @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-edit-product-form-field-value :slug (shared/slugify %)])
                      :placeholder "Product slug"}]
        (when-not (c/validate-slug (:slug @form-data))
          [:p "Invalid slug"])
        [label/label {:text "Sku"
                      :extra-style "mt-1"}]
        [input/input {:value (:sku @form-data)
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-edit-product-form-field-value :sku %])
                      :placeholder "Product sku"}]
        (when-not (c/validate-sku (:sku @form-data))
          [:p "Invalid sku"])
        [label/label {:text "Price"
                      :extra-style "mt-1"}]
        [input/input {:value (:price @form-data)
                      :type "number"
                      :on-change
                      #(re-frame/dispatch
                        [::events/set-edit-product-form-field-value :price %])
                      :placeholder "Product price"}]
        [:div {:style {:display "flex"
                       :align-items "center"
                       :justify-content "space-between"}}
         [:p "Active"]
         [input/input {:type "checkbox"
                       :value (:active @form-data)
                       :checked (:active @form-data)
                       :on-change
                       #(re-frame/dispatch
                         [::events/set-edit-product-form-field-value :active
                          (not (:active @form-data))])}]]
        [button/button-outlined {:text "Edit"
                                 :type "submit"
                                 :extra-style "mt-2"
                                 :on-click #()}]]])))

(defn modal [modal-open?]
  [modal-wrapper/modal {:modal-open? modal-open?}
   [edit-product-modal modal-open?]])
