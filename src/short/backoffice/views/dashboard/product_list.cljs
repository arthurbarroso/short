(ns short.backoffice.views.dashboard.product-list
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]
            [short.ui.text :as text]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.backoffice.components.table :as table]
            [reagent.core :as reagent]
            ["react-modal" :as Modal]))

(def custom-modal-css
  {:content {:top "50%"
             :left "50%"
             :right "auto"
             :bottom "auto"
             :marginRight "-50%"
             :transform "translate(-50%, -50%)"
             :width "50%"}})

(def columns [{:path [:product/title]
               :header "Title"
               :key :title}
              {:path [:product/price]
               :header "Price"
               :key :price}
              {:path [:product/sku]
               :header "SKU"
               :key :sku}
              {:path [:product/slug]
               :header "Slug"
               :key :slug}
              {:path [:product/active]
               :header "Active"
               :format #(str %)
               :key :active}
              {:path [:product/created_at]
               :header "Created at"
               :key :created_at}
              {:path [:product/variant]
               :header "Variant count"
               :key :variant
               :format #(count %)
               :attrs (fn [_data]
                        {:style
                         {:text-align "center"
                          :display "block"}})}])

(defn open-modal [modal-open?]
  (reset! modal-open? true))

(defn close-modal [modal-open?]
  (reset! modal-open? false))

(defn create-product-modal
  [{:keys [title slug sku price]}]
  [:<>
   [:form
    [label/label {:text "Title"}]
    [input/input {:value title
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value
                     :title
                     %])
                  :placeholder "Product title"}]
    [label/label {:text "Slug"
                  :extra-style "mt-1"}]
    [input/input {:value slug
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value
                     :slug
                     %])
                  :placeholder "Product slug"}]
    [label/label {:text "Sku"
                  :extra-style "mt-1"}]
    [input/input {:value sku
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value
                     :sku
                     %])
                  :placeholder "Product sku"}]
    [label/label {:text "Price"
                  :extra-style "mt-1"}]
    [input/input {:value price
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value
                     :price
                     %])
                  :placeholder "Product price"}]
    [button/button-outlined {:text "Create"
                             :extra-style "mt-2"}]]])

(defn list-view []
  (let [products (re-frame/subscribe [::subs/products])
        product-data (re-frame/subscribe [::subs/product-form-values])
        modal-open? (reagent/atom false)]
    (fn []
      [template/layout
       ^{:key "panel"}
       [:<>
        [:> Modal {:isOpen @modal-open?
                   :style custom-modal-css
                   :onRequestClose #(close-modal modal-open?)}
         [create-product-modal @product-data]]
        [:div.product-list-container
         [:div.product-list-header
          [text/typography {:text "PRODUCT LIST"
                            :variant "h2"}]
          [button/button {:text "Create product"
                          :on-click #(open-modal modal-open?)}]]
         [table/table {:columns columns
                       :items products
                       :key [:product/title]}]]]])))
