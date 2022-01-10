(ns short.backoffice.views.dashboard.product-list
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]
            [short.ui.text :as text]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.table :as table]
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

(defn open-modal [modal-open?]
  (reset! modal-open? true))

(defn close-modal [modal-open?]
  (reset! modal-open? false))

(defn create-product-modal
  [{:keys [title slug sku price]} modal-open?]
  [:<>
   [:form
    [label/label {:text "Title"}]
    [input/input {:value title
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :title %])
                  :placeholder "Product title"}]
    [label/label {:text "Slug"
                  :extra-style "mt-1"}]
    [input/input {:value slug
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :slug %])
                  :placeholder "Product slug"}]
    [label/label {:text "Sku"
                  :extra-style "mt-1"}]
    [input/input {:value sku
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :sku %])
                  :placeholder "Product sku"}]
    [label/label {:text "Price"
                  :extra-style "mt-1"}]
    [input/input {:value price
                  :type "number"
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :price %])
                  :placeholder "Product price"}]
    [button/button-outlined {:text "Create"
                             :extra-style "mt-2"
                             :on-click #(do
                                          (re-frame/dispatch
                                           [::events/create-product
                                            {:sku sku
                                             :active true
                                             :slug slug
                                             :title title
                                             :price (js/parseFloat price)}])
                                          (close-modal modal-open?))}]]])

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
         [create-product-modal @product-data modal-open?]]
        [:div.product-list-container
         [:div.product-list-header
          [text/typography {:text "PRODUCT LIST"
                            :variant "h2"}]
          [button/button {:text "Create product"
                          :on-click #(open-modal modal-open?)}]]
         [table/table {:columns ["Title" "Price" "Variants"]
                       :items @products
                       :item-keys [{:key :product/title}
                                   {:key :product/price}
                                   {:key :product/variant
                                    :fun count}]
                       :key :product/uuid}]]]])))
