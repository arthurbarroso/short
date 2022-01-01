(ns short.backoffice.views.panel
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.ui.text :as text]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.form :as form]
            [short.ui.input :as input]
            [short.backoffice.components.table :as table]
            [reagent.core :as reagent]
            [garden.core :refer [css]]
            ["react-modal" :as Modal]))

(def product-modal-css
  {:height "100%"})

(def product-modal-styles
  (str (css [:.product-modal product-modal-css])))

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
               :attrs (fn [_data] {:style {:text-align "center" :display "block"}})}])

(defn open-modal [modal-open?]
  (reset! modal-open? true))

(defn close-modal [modal-open?]
  (reset! modal-open? false))

(defn create-product-modal []
  [:<>
   [form/form
    [:<>
     [label/label {:text "Title"}]
     [input/input {:value ""
                   :on-change #()
                   :placeholder "Product title"}]
     [label/label {:text "Slug"
                   :extra-style "mt-1"}]
     [input/input {:value ""
                   :on-change #()
                   :placeholder "Product slug"}]
     [label/label {:text "Sku"
                   :extra-style "mt-1"}]
     [input/input {:value ""
                   :on-change #()
                   :placeholder "Product sku"}]
     [label/label {:text "Price"
                   :extra-style "mt-1"}]
     [input/input {:value ""
                   :on-change #()
                   :placeholder "Product price"}]
     [button/button-outlined {:text "Create"
                              :extra-style "mt-2"}]]]])

(defn panel-view []
  (let [products (re-frame/subscribe [::subs/products])
        modal-open? (reagent/atom false)]
    (fn []
      [template/layout
       ^{:key "panel"}
       [:<>
        [:> Modal {:isOpen @modal-open?
                   :onRequestClose #(close-modal modal-open?)}
         [create-product-modal]]
        [text/typography {:text "Product list"
                          :variant "h2"}]
        [table/table {:columns columns
                      :items products
                      :key [:product/title]}]
        [button/button {:text "Create product"
                        :on-click #(open-modal modal-open?)}]]])))
